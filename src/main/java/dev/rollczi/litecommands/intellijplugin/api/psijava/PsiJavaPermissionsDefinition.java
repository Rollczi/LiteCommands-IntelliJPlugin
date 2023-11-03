package dev.rollczi.litecommands.intellijplugin.api.psijava;

import com.intellij.lang.jvm.actions.AnnotationAttributeRequest;
import com.intellij.lang.jvm.actions.AnnotationAttributeValueRequest;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiModifierListOwner;
import dev.rollczi.litecommands.annotations.permission.Permission;
import dev.rollczi.litecommands.intellijplugin.api.PermissionEntry;
import dev.rollczi.litecommands.intellijplugin.api.PermissionsDefinition;
import dev.rollczi.litecommands.intellijplugin.navigatable.NavigatableReference;
import dev.rollczi.litecommands.intellijplugin.util.PsiAnnotationUtil;
import dev.rollczi.litecommands.intellijplugin.util.PsiValue;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
class PsiJavaPermissionsDefinition implements PermissionsDefinition {

    private static final String PERMISSION_ATTRIBUTE = "value";

    private final PsiModifierListOwner permissionOwner;

    PsiJavaPermissionsDefinition(PsiModifierListOwner psiModifierListOwner) {
        this.permissionOwner = psiModifierListOwner;
    }

    @Override
    public List<PermissionEntry> permissions() {
        List<PermissionEntry> entries = new ArrayList<>();

        for (PsiAnnotation annotation : getPermissionAnnotations()) {
            for (PsiValue<String> psiValue : PsiAnnotationUtil.getStringArray(annotation, PERMISSION_ATTRIBUTE)) {
                entries.add(new PsiJavaPermissionEntry(psiValue.value(), psiValue.source()));
            }
        }

        return entries;
    }

    @Override
    public void permissions(List<String> permissions) {
        Iterator<String> permissionIterator = permissions.iterator();
        List<PsiAnnotation> annotations = getPermissionAnnotations();

        if (annotations.isEmpty()) {

            List<String> values = new ArrayList<>();

            while (permissionIterator.hasNext()) {
                values.add(permissionIterator.next());
            }

            if (!values.isEmpty()) {
                PsiAnnotationUtil.addAnnotation(
                    Permission.class.getName(),
                    permissionOwner,
                    new AnnotationAttributeRequest(PERMISSION_ATTRIBUTE, new AnnotationAttributeValueRequest.ArrayValue(
                        values.stream()
                            .map(string -> new AnnotationAttributeValueRequest.StringValue(string))
                            .toList()
                    ))
                );
            }

            return;
        }

        int index = 0;
        for (PsiAnnotation annotation : annotations) {
            int size = PsiAnnotationUtil.getStringArray(annotation, PERMISSION_ATTRIBUTE).size();

            List<String> values = new ArrayList<>();

            for (int i = 0; i < size; i++) {
                if (permissionIterator.hasNext()) {
                    values.add(permissionIterator.next());
                }
            }

            // if is last annotation and there are more permissions
            if (index++ == annotations.size() - 1) {
                while (permissionIterator.hasNext()) {
                    values.add(permissionIterator.next());
                }
            }

            if (values.isEmpty()) {
                annotation.delete();
                continue;
            }

            PsiAnnotationUtil.setStringArray(annotation, PERMISSION_ATTRIBUTE, values);
        }
    }

    @Override
    public boolean isEmpty() {
        for (PsiAnnotation permissionAnnotation : getPermissionAnnotations()) {
            if (!PsiAnnotationUtil.getStringArray(permissionAnnotation, PERMISSION_ATTRIBUTE).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public NavigatableReference navigatable() {
        return NavigatableReference.ofPsiElement(() -> permissionOwner);
    }

    private List<PsiAnnotation> getPermissionAnnotations() {
        return PsiAnnotationUtil.getAnnotations(permissionOwner, Permission.class.getName());
    }

}
