package dev.rollczi.litecommands.intellijplugin.api;

import com.intellij.psi.PsiFile;
import dev.rollczi.litecommands.intellijplugin.navigatable.NavigatableReference;
import java.util.List;

public interface CommandNode extends Node {

    @Override
    String name();

    @Override
    void name(String name);

    @Override
    NavigatableReference navigateToName();

    @Override
    List<String> aliases();

    @Override
    void aliases(List<String> aliases);

    @Override
    NavigatableReference navigateToAlias(String alias);

    @Override
    PermissionsDefinition permissionsDefinition();

    List<ExecutorNode> executors();

    boolean hasExecutors();

    PsiFile getFile();

}
