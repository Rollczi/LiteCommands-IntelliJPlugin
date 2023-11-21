package dev.rollczi.litecommands.intellijplugin.suppressor;

import com.intellij.codeInspection.InspectionSuppressor;
import com.intellij.codeInspection.SuppressQuickFix;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiMethod;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.command.RootCommand;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClassUnusedInspectSuppressor implements InspectionSuppressor {

    @Override
    public boolean isSuppressedFor(@NotNull PsiElement element, @NotNull String toolId) {
        if (!toolId.equals(SuppressUtils.UNUSED_DECLARATION)) {
            return false;
        }

        if (!(element instanceof PsiIdentifier)) {
            return false;
        }

        PsiElement parent = element.getParent();

        if (!(parent instanceof PsiClass psiClass)) {
            return false;
        }

        return SuppressUtils.isInjectClass(psiClass);
    }

    @Override
    public SuppressQuickFix @NotNull [] getSuppressActions(@Nullable PsiElement element, @NotNull String toolId) {
        return SuppressQuickFix.EMPTY_ARRAY;
    }

}
