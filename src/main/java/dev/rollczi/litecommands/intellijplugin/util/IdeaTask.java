package dev.rollczi.litecommands.intellijplugin.util;

import com.intellij.openapi.project.Project;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class IdeaTask<T> implements IdeaEmptyTask {

    private final CompletableFuture<T> future;

    public IdeaTask(CompletableFuture<T> future) {
        this.future = future;
    }

    public IdeaTask<T> filter(IdeaTaskType type, Function<T, Boolean> function) {
        return flatMap(type, t -> function.apply(t) ? this : notCompleted());
    }

    public IdeaTask<T> then(IdeaTaskType type, Consumer<T> consumer) {
        return map(type, t -> {
            consumer.accept(t);
            return t;
        });
    }

    @Override
    public IdeaTask<T> then(IdeaTaskType type, Runnable runnable) {
        return then(type, unused -> runnable.run());
    }

    public IdeaTask<T> then(Consumer<T> consumer) {
        return map(t -> {
            consumer.accept(t);
            return t;
        });
    }

    @Override
    public IdeaTask<T> then(Runnable runnable) {
        return then(unused -> runnable.run());
    }

    public <R> IdeaTask<R> map(IdeaTaskType type, Function<T, R> function) {
        return new IdeaTask<>(future.thenApplyAsync(function, type.getExecutor()));
    }

    @Override
    public <R> IdeaTask<R> map(IdeaTaskType type, Supplier<R> function) {
        return map(type, unused -> function.get());
    }

    public <R> IdeaTask<R> map(Function<T, R> supplier) {
        return new IdeaTask<>(future.thenApply(supplier));
    }

    public <R> IdeaTask<R> flatMap(Function<T, IdeaTask<R>> function) {
        return new IdeaTask<>(future.thenCompose(t -> function.apply(t).asFuture()));
    }

    public <R> IdeaTask<R> flatMap(IdeaTaskType type, Function<T, IdeaTask<R>> function) {
        return new IdeaTask<>(future.thenCompose(t -> {
            IdeaTask<IdeaTask<R>> task = this.map(type, function);
            CompletableFuture<IdeaTask<R>> future = task.asFuture();

            return future.thenCompose(ideaTask -> ideaTask.asFuture());
        }));
    }

    @Override
    public <R> IdeaTask<R> flatMap(IdeaTaskType type, Supplier<IdeaTask<R>> function) {
        return flatMap(type, unused -> function.get());
    }

    @Override
    public <R> IdeaTask<R> flatMap(Supplier<IdeaTask<R>> function) {
        return flatMap(unused -> function.get());
    }

    @Override
    public <R> IdeaTask<R> ui(Supplier<R> supplier) {
        return ui(unused -> supplier.get());
    }

    public <R> IdeaTask<R> ui(Function<T, R> supplier) {
        return map(IdeaTaskType.UI, supplier);
    }

    @Override
    public <R> IdeaTask<R> read(Supplier<R> supplier) {
        return read(unused -> supplier.get());
    }

    public <R> IdeaTask<R> read(Function<T, R> supplier) {
        return map(IdeaTaskType.READ, supplier);
    }

    @Override
    public <R> IdeaTask<R> write(Supplier<R> supplier) {
        return write(unused -> supplier.get());
    }

    public <R> IdeaTask<R> write(Function<T, R> supplier) {
        return map(IdeaTaskType.WRITE, supplier);
    }

    @Override
    public <R> IdeaTask<R> async(Supplier<R> supplier) {
        return async(unused -> supplier.get());
    }

    public <R> IdeaTask<R> async(Function<T, R> supplier) {
        return map(IdeaTaskType.ASYNC, supplier);
    }

    @Override
    public <R> IdeaTask<R> waitForSmart(Project project, Supplier<R> supplier) {
        return waitForSmart(project, unused -> supplier.get());
    }

    public <R> IdeaTask<R> waitForSmart(Project project, Function<T, R> supplier) {
        return map(IdeaTaskType.smartMode(project), supplier);
    }

    @Override
    public IdeaTask<T> waitForSmart(Project project, Runnable runnable) {
        return waitForSmart(project, unused -> {
            runnable.run();
            return null;
        });
    }

    @Override
    public IdeaTask<T> waitForSmart(Project project) {
        return waitForSmart(project, () -> {});
    }

    public CompletableFuture<T> asFuture() {
        return future;
    }

    public static IdeaEmptyTask start() {
        return new IdeaTask<>(CompletableFuture.completedFuture(null));
    }

    public static IdeaEmptyTask startInSmart(Project project) {
        return start().waitForSmart(project);
    }

    public static <T> IdeaTask<T> supply(IdeaTaskType type, Supplier<T> supplier) {
        return start().map(type, supplier);
    }

    public static <T> IdeaTask<T> notCompleted() {
        return new IdeaTask<>(new CompletableFuture<>());
    }

}
