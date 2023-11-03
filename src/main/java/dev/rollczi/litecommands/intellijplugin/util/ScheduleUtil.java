package dev.rollczi.litecommands.intellijplugin.util;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiElement;

public final class ScheduleUtil {

    private ScheduleUtil() {
    }

    public static void invokeLater(PsiElement element, Runnable runnable) {
        ApplicationManager.getApplication().invokeLater(
            () -> WriteCommandAction.runWriteCommandAction(element.getProject(), runnable)
        );
    }

}
