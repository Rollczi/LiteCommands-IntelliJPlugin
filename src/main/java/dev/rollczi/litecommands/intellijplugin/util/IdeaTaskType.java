package dev.rollczi.litecommands.intellijplugin.util;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import java.util.concurrent.Executor;

public class IdeaTaskType {

    public static final IdeaTaskType UI = new IdeaTaskType(command -> ApplicationManager.getApplication().invokeLater(command));
    public static final IdeaTaskType READ = new IdeaTaskType(command -> ApplicationManager.getApplication().runReadAction(command));
    public static final IdeaTaskType WRITE = new IdeaTaskType(command -> ApplicationManager.getApplication().runWriteAction(command));
    public static final IdeaTaskType WRITE_INTENT = new IdeaTaskType(command -> ApplicationManager.getApplication().runWriteIntentReadAction(() -> {
        ApplicationManager.getApplication().runWriteAction(command);
        return null;
    }));

    public static final IdeaTaskType ASYNC = new IdeaTaskType(command -> ApplicationManager.getApplication().executeOnPooledThread(command));

    public static IdeaTaskType smartMode(Project project) {
        return new IdeaTaskType(command -> DumbService.getInstance(project).runWhenSmart(command));
    }

    public static IdeaTaskType writeCommand(Project project) {
        return new IdeaTaskType(command -> WriteCommandAction.runWriteCommandAction(project, command));
    }

    public static IdeaTaskType command(Project project) {
        return new IdeaTaskType(command -> CommandProcessor.getInstance().executeCommand(project, command, null, null));
    }

    private final Executor executor;

    Executor getExecutor() {
        return executor;
    }

    public IdeaTaskType(Executor executor) {
        this.executor = executor;
    }

}
