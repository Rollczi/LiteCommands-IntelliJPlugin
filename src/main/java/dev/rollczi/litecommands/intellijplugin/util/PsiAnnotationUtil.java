package dev.rollczi.litecommands.intellijplugin.util;

import com.intellij.psi.*;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PsiAnnotationUtil {

    private PsiAnnotationUtil() {
    }

    public static List<PsiValue<String>> getStringArray(PsiAnnotation annotation, String attributeName) {
        PsiAnnotationMemberValue attributeValue = annotation.findDeclaredAttributeValue(attributeName);

        if (attributeValue instanceof PsiArrayInitializerMemberValue arrayInitializerMemberValue) {
            List<PsiValue<String>> values = new ArrayList<>();

            for (PsiAnnotationMemberValue initializer : arrayInitializerMemberValue.getInitializers()) {
                if (initializer instanceof PsiLiteralExpression literalExpression) {
                    values.add(new PsiValue<>(literalExpression, (String) literalExpression.getValue()));
                }
            }

            return values;
        }

        if (attributeValue instanceof PsiLiteralExpression literalExpression) {
            return List.of(new PsiValue<>(literalExpression, (String) literalExpression.getValue()));
        }

        return List.of();
    }


    public static void setString(PsiAnnotation annotation, String attributeName, String newValue) {
        setStringArray(annotation, attributeName, List.of(newValue));
    }

    public static void setStringArray(PsiAnnotation annotation, String attributeName, Collection<String> list) {
        PsiElementFactory factory = JavaPsiFacade.getElementFactory(annotation.getProject());
        PsiAnnotationMemberValue value = annotation.findDeclaredAttributeValue(attributeName);

        String array = toArray(list);

        if (value == null) {
            PsiAnnotationMemberValue newValueElement = factory.createExpressionFromText(array, annotation);
            annotation.setDeclaredAttributeValue(attributeName, newValueElement);
            return;
        }

        PsiAnnotationMemberValue newValueElement = factory.createExpressionFromText(array, annotation);
        value.replace(newValueElement);
    }

    private static String toArray(Collection<String> list) {
        if (list.isEmpty()) {
            return "{}";
        }

        if (list.size() == 1) {
            return "\"" + list.iterator().next() + "\"";
        }

        return list.stream()
            .map(s -> "\"" + s + "\"")
            .collect(Collectors.joining(", ", "{", "}"));
    }

    public static PsiAnnotation addAnnotation(Class<? extends Annotation> annotation, PsiModifierListOwner owner) {
        return addAnnotation(annotation.getSimpleName(), annotation.getName(), owner);
    }

    public static PsiAnnotation addAnnotation(String annotationName, String qName, PsiModifierListOwner owner) {
        PsiElementFactory factory = JavaPsiFacade.getElementFactory(owner.getProject());
        PsiAnnotation annotation = factory.createAnnotationFromText("@" + annotationName, owner);
        PsiElement psiElement = owner.getModifierList().addAfter(annotation, null);

        PsiImportUtil.importClass(owner, qName);

        return (PsiAnnotation) psiElement;
    }

}
