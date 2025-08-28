package dev.rollczi.litecommands.intellijplugin.suppressor;

import com.intellij.codeInspection.InspectionSuppressor;
import com.intellij.codeInspection.SuppressQuickFix;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConstructorUnusedInspectSuppressor implements InspectionSuppressor {

    @Override
    public boolean isSuppressedFor(@NotNull PsiElement element, @NotNull String toolId) {
        if (!toolId.equals(SuppressUtils.UNUSED_DECLARATION)) {
            return false;
        }

        if (!(element instanceof PsiIdentifier)) {
            return false;
        }

        PsiElement parent = element.getParent();

        if (!(parent instanceof PsiMethod psiMethod)) {
            return false;
        }

        return SuppressUtils.isInjectConstructor(psiMethod);
    }

    @Override
    public SuppressQuickFix[] getSuppressActions(@Nullable PsiElement element, @NotNull String toolId) {
        return SuppressQuickFix.EMPTY_ARRAY;
    }

}
