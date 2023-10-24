package dev.rollczi.litecommands.intellijplugin.util;

import com.intellij.psi.*;

import java.util.ArrayList;
import java.util.List;

public class PsiAnnotationUtil {

    private PsiAnnotationUtil() {
    }

    public static List<PsiValue<String>> stringArray(PsiAnnotation annotation, String attributeName) {
        PsiAnnotationMemberValue attributeValue = annotation.findDeclaredAttributeValue(attributeName);

        if (attributeValue instanceof PsiArrayInitializerMemberValue arrayInitializerMemberValue) {
            List<PsiValue<String>> values = new ArrayList<>();

            for (PsiAnnotationMemberValue initializer : arrayInitializerMemberValue.getInitializers()) {
                if (initializer instanceof PsiLiteralExpression literalExpression) {
                    values.add(new PsiValue<>(literalExpression, literalExpression.getText()));
                }
            }

            return values;
        }

        if (attributeValue instanceof PsiLiteralExpression literalExpression) {
            return List.of(new PsiValue<>(literalExpression, literalExpression.getText()));
        }

        return List.of();
    }


    public static void editAttribute(PsiAnnotation annotation, String attributeName, String newValue) {
        PsiAnnotationMemberValue value = annotation.findDeclaredAttributeValue(attributeName);
        if (value == null) {
            return;
        }

        if (value instanceof PsiArrayAccessExpression arrayAccessExpression) {
            PsiElement[] expressions = arrayAccessExpression.getArrayExpression().getChildren();
            if (expressions.length == 0) {
                return;
            }

            PsiElement expression = expressions[0];
            if (expression instanceof PsiLiteralExpression literalExpression) {
                literalExpression.replace(PsiElementFactory.getInstance(annotation.getProject()).createExpressionFromText(newValue, annotation));
            }

            return;
        }

        PsiElementFactory factory = JavaPsiFacade.getElementFactory(annotation.getProject());
        PsiAnnotationMemberValue newValueElement = factory.createExpressionFromText(newValue, annotation);
        value.replace(newValueElement);
    }

}
