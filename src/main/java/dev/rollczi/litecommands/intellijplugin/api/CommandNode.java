package dev.rollczi.litecommands.intellijplugin.api;

import com.intellij.pom.Navigatable;
import java.util.List;

public interface CommandNode extends Node {

    @Override
    String name();

    @Override
    void name(String name);

    @Override
    Navigatable navigateToName();

    @Override
    List<String> aliases();

    @Override
    void aliases(List<String> aliases);

    @Override
    Navigatable navigateToAlias(String alias);

    @Override
    List<Permission> permissions();

    @Override
    void permissions(List<String> permissions);

    List<ExecutorNode> executors();

    boolean hasExecutors();

}
