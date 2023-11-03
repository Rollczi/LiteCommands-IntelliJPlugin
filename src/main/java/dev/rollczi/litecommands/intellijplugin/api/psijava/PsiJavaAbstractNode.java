package dev.rollczi.litecommands.intellijplugin.api.psijava;

import com.intellij.lang.jvm.annotation.JvmAnnotationAttribute;
import com.intellij.lang.jvm.annotation.JvmAnnotationAttributeValue;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.PsiModifierListOwner;
import dev.rollczi.litecommands.annotations.permission.Permissions;
import dev.rollczi.litecommands.intellijplugin.api.Node;
import dev.rollczi.litecommands.intellijplugin.api.Permission;
import dev.rollczi.litecommands.intellijplugin.navigatable.NavigatableReference;
import dev.rollczi.litecommands.intellijplugin.old.annotation.AnnotationFactory;
import dev.rollczi.litecommands.intellijplugin.old.annotation.AnnotationHolder;
import dev.rollczi.litecommands.intellijplugin.util.PsiAnnotationUtil;
import dev.rollczi.litecommands.intellijplugin.util.PsiValue;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import panda.std.Pair;

public abstract class PsiJavaAbstractNode implements Node {

    protected final PsiModifierListOwner permissionOwner;
    protected final Class<? extends Annotation> nodeAnnotation;
    protected final String nodeNameAttribute;
    protected final String nodeAliasesAttribute;

    protected PsiJavaAbstractNode(PsiModifierListOwner permissionOwner, Class<? extends Annotation> nodeAnnotation, String nodeNameAttribute, String nodeAliasesAttribute) {
        this.permissionOwner = permissionOwner;
        this.nodeAnnotation = nodeAnnotation;
        this.nodeNameAttribute = nodeNameAttribute;
        this.nodeAliasesAttribute = nodeAliasesAttribute;
    }

    @Override
    public String name() {
        PsiAnnotation annotation = getNodeAnnotation();
        List<PsiValue<String>> names = PsiAnnotationUtil.getStringArray(annotation, nodeNameAttribute);

        if (names.size() != 1) {
            try {
                Method method = nodeAnnotation.getMethod(nodeNameAttribute);
                Object defaultValue = method.getDefaultValue();

                if (defaultValue instanceof String) {
                    return (String) defaultValue;
                }
            }
            catch (NoSuchMethodException ignore) {}

            throw new IllegalStateException("Invalid node name");
        }

        return names.get(0).value();
    }

    @Override
    public void name(String name) {
        PsiAnnotationUtil.setString(this.getNodeAnnotation(), nodeNameAttribute, name);
    }

    @Override
    public NavigatableReference navigateToName() {
        return NavigatableReference.ofPsiElement(() -> this.getNodeAnnotation().findDeclaredAttributeValue(nodeNameAttribute));
    }

    @Override
    public List<String> aliases() {
        return PsiAnnotationUtil.getStringArray(this.getNodeAnnotation(), nodeAliasesAttribute)
            .stream()
            .map(stringPsiValue -> stringPsiValue.value())
            .toList();
    }

    private PsiAnnotation getNodeAnnotation() {
        PsiAnnotation annotation = permissionOwner.getAnnotation(nodeAnnotation.getName());

        if (annotation == null) {
            throw new RuntimeException("Command annotation not found");
        }

        return annotation;
    }

    @Override
    public void aliases(List<String> aliases) {
        PsiAnnotationUtil.setStringArray(this.getNodeAnnotation(), nodeAliasesAttribute, aliases);
    }

    @Override
    public NavigatableReference navigateToAlias(String alias) {
        return PsiAnnotationUtil.getStringArray(this.getNodeAnnotation(), nodeAliasesAttribute)
            .stream()
            .filter(stringPsiValue -> stringPsiValue.value().equals(alias))
            .findFirst()
            .map(stringPsiValue -> NavigatableReference.ofPsiElement(() -> stringPsiValue.source()))
            .orElse(NavigatableReference.empty());
    }

    @Override
    public List<Permission> permissions() {
        return getPermissions().entrySet().stream()
            .<Permission>map(entry -> new PsiJavaPermission(entry.getKey(), entry.getValue()))
            .toList();
    }

    @Override
    public void permissions(List<String> permissions) {
        Collection<PsiAnnotation> annotations = getPermissions().values();

        for (PsiAnnotation annotation : annotations) {
            annotation.delete();
        }

        for (String permission : permissions) {
            PsiAnnotation annotation = PsiAnnotationUtil.addAnnotation(dev.rollczi.litecommands.annotations.permission.Permission.class, permissionOwner);
            PsiAnnotationUtil.setString(annotation, "value", permission);
        }
    }

    private Map<String, PsiAnnotation> getPermissions() {
        List<AnnotationHolder<dev.rollczi.litecommands.annotations.permission.Permission>> packed = AnnotationFactory.from(dev.rollczi.litecommands.annotations.permission.Permission.class, permissionOwner);

        for (AnnotationHolder<Permissions> holder : AnnotationFactory.from(Permissions.class, permissionOwner)) {
            packed.addAll(Arrays.stream(holder.asAnnotation().value()).map(permission -> new AnnotationHolder<>(holder.asPsi(), permission)).toList());
        }

        return packed.stream()
            .flatMap(holder -> Arrays.stream(holder.asAnnotation().value()).map(s -> Pair.of(s, holder.asPsi())))
            .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond, (a, b) -> a, LinkedHashMap::new));
    }

}
