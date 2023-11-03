package dev.rollczi.litecommands.intellijplugin.api.psijava;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiModifierListOwner;
import dev.rollczi.litecommands.intellijplugin.api.Node;
import dev.rollczi.litecommands.intellijplugin.api.PermissionsDefinition;
import dev.rollczi.litecommands.intellijplugin.navigatable.NavigatableReference;
import dev.rollczi.litecommands.intellijplugin.util.PsiAnnotationUtil;
import dev.rollczi.litecommands.intellijplugin.util.PsiValue;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

abstract class PsiJavaAbstractNode implements Node {

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
    public PermissionsDefinition permissionsDefinition() {
        return new PsiJavaPermissionsDefinition(permissionOwner);
    }

}
