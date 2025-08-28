package dev.rollczi.litecommands.intellijplugin.suppressor;

import com.intellij.codeInspection.InspectionSuppressor;
import com.intellij.codeInspection.SuppressQuickFix;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import dev.rollczi.litecommands.intellijplugin.util.LiteAnnotationChecks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OptionalArgumentSuppressor implements InspectionSuppressor {

    @Override
    public boolean isSuppressedFor(@NotNull PsiElement element, @NotNull String toolId) {
        if (!toolId.equals(SuppressUtils.OPTIONAL_FIELD_OR_PARAMETER_TYPE)) {
            return false;
        }

        if (!(element.getParent() instanceof PsiParameter psiParameter)) {
            return false;
        }

        if (!(psiParameter.getParent() instanceof PsiParameterList parameterList)) {
            return false;
        }

        if (!(parameterList.getParent() instanceof PsiMethod psiMethod)) {
            return false;
        }

        return LiteAnnotationChecks.isCommandExecutor(psiMethod);
    }

    @Override
    public SuppressQuickFix[] getSuppressActions(@Nullable PsiElement element, @NotNull String toolId) {
        return SuppressQuickFix.EMPTY_ARRAY;
    }

}
