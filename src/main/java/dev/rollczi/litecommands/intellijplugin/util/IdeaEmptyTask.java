package dev.rollczi.litecommands.intellijplugin.util;

import com.intellij.openapi.project.Project;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public interface IdeaEmptyTask {

    IdeaEmptyTask then(IdeaTaskType type, Runnable runnable);

    IdeaEmptyTask then(Runnable runnable);

    <R> IdeaTask<R> map(IdeaTaskType type, Supplier<R> function);

    <R> IdeaTask<R> flatMap(IdeaTaskType type, Supplier<IdeaTask<R>> function);

    <R> IdeaTask<R> flatMap(Supplier<IdeaTask<R>> function);

    <R> IdeaTask<R> ui(Supplier<R> supplier);

    <R> IdeaTask<R> read(Supplier<R> supplier);

    <R> IdeaTask<R> write(Supplier<R> supplier);

    <R> IdeaTask<R> async(Supplier<R> supplier);

    <R> IdeaTask<R> waitForSmart(Project project, Supplier<R> supplier);

    IdeaEmptyTask waitForSmart(Project project, Runnable supplier);

    IdeaEmptyTask waitForSmart(Project project);

    CompletableFuture<?> asFuture();

}
