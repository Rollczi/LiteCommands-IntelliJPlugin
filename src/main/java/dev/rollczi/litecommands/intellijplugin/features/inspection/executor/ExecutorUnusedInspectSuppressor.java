package dev.rollczi.litecommands.intellijplugin.features.inspection.executor;

import com.intellij.codeInspection.*;
import com.intellij.psi.*;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExecutorUnusedInspectSuppressor implements InspectionSuppressor {

    private final static String INSPECTION = "UnusedDeclaration";

    @Override
    public boolean isSuppressedFor(@NotNull PsiElement element, @NotNull String toolId) {
        if (!toolId.equals(INSPECTION)) {
            return false;
        }

        if (!(element instanceof PsiIdentifier)) {
            return false;
        }

        PsiElement parent = element.getParent();

        if (!(parent instanceof PsiMethod psiMethod)) {
            return false;
        }

        PsiAnnotation execute = psiMethod.getAnnotation(Execute.class.getName());

        return execute != null;
    }

    @Override
    public SuppressQuickFix @NotNull [] getSuppressActions(@Nullable PsiElement element, @NotNull String toolId) {
        return SuppressQuickFix.EMPTY_ARRAY;
    }

}