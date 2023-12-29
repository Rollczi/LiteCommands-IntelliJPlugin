package dev.rollczi.litecommands.intellijplugin.suppressor;

import com.intellij.codeInspection.InspectionSuppressor;
import com.intellij.codeInspection.SuppressQuickFix;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.intellijplugin.util.LiteCommandsTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NullableProblemsPrimitiveSuppressor implements InspectionSuppressor {

    @Override
    public boolean isSuppressedFor(@NotNull PsiElement element, @NotNull String toolId) {
        if (!toolId.equals(SuppressUtils.NULLABLE_PROBLEMS)) {
            return false;
        }

        if (!(element.getParent() instanceof PsiModifierList modifierList)) {
            return false;
        }

        if (!(modifierList.getParent() instanceof PsiParameter psiParameter)) {
            return false;
        }

        if (!LiteCommandsTypes.isPrimitiveType(psiParameter.getType())) {
            return false;
        }

        if (!(psiParameter.getParent() instanceof PsiParameterList parameterList)) {
            return false;
        }

        if (!(parameterList.getParent() instanceof PsiMethod psiMethod)) {
            return false;
        }

        return isCommandExecutor(psiMethod);
    }

    private static boolean isCommandExecutor(PsiMethod psiMethod) {
        return psiMethod.getAnnotation(Execute.class.getName()) != null;
    }

    @Override
    public SuppressQuickFix @NotNull [] getSuppressActions(@Nullable PsiElement element, @NotNull String toolId) {
        return SuppressQuickFix.EMPTY_ARRAY;
    }

}
