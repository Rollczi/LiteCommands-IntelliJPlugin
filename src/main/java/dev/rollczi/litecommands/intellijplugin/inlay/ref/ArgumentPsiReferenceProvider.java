package dev.rollczi.litecommands.intellijplugin.inlay.ref;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationParameterList;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.PsiNameValuePair;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.util.ProcessingContext;
import dev.rollczi.litecommands.intellijplugin.util.LiteTypeChecks;
import org.jetbrains.annotations.NotNull;

class ArgumentPsiReferenceProvider extends PsiReferenceProvider {

    ArgumentPsiReferenceProvider() {
    }

    @Override
    public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        PsiLiteralExpression literal = (PsiLiteralExpression) element;

        if (!(literal.getValue() instanceof String)) {
            return PsiReference.EMPTY_ARRAY;
        }

        if (!(element.getParent() instanceof PsiNameValuePair nameValuePair)) {
            return PsiReference.EMPTY_ARRAY;
        }

        if (!(nameValuePair.getParent() instanceof PsiAnnotationParameterList annotationParameterList)) {
            return PsiReference.EMPTY_ARRAY;
        }

        if (!(annotationParameterList.getParent() instanceof PsiAnnotation annotation)) {
            return PsiReference.EMPTY_ARRAY;
        }

        if (!(annotation.getParent().getParent() instanceof PsiParameter parameter)) {
            return PsiReference.EMPTY_ARRAY;
        }

        if (LiteTypeChecks.isArgumentAnnotation(annotation)) {
            return new PsiReference[] {
                new SimpleReference(parameter.getType(), element, element.getTextRangeInParent())
            };
        }

        return PsiReference.EMPTY_ARRAY;
    }

}
