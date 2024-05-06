package dev.rollczi.litecommands.intellijplugin.util;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.command.RootCommand;
import dev.rollczi.litecommands.annotations.execute.Execute;

public final class LiteAnnotationChecks {

    public static boolean isCommand(PsiClass psiClass) {
        return psiClass.getAnnotation(Command.class.getName()) != null || psiClass.getAnnotation(RootCommand.class.getName()) != null;
    }

    public static boolean isCommandExecutor(PsiMethod psiMethod) {
        return psiMethod.getAnnotation(Execute.class.getName()) != null;
    }

}
