package dev.rollczi.litecommands.intellijplugin.api;

import dev.rollczi.litecommands.intellijplugin.navigatable.NavigatableReference;
import java.util.List;
import java.util.StringJoiner;

public interface ExecutorNode extends Node {

    @Override
    String name();

    @Override
    void name(String name);

    @Override
    NavigatableReference navigateToName();

    default String structure() {
        String executorName = this.name() + " ";

        if (this.name().isEmpty()) {
            executorName = "";
        }

        StringJoiner arguments = new StringJoiner(" ");

        for (Argument argument : this.arguments()) {
            arguments.add("<" + argument.name() + ">");
        }

        return "/" + this.parent().name() + " " + executorName + arguments;
    }

    @Override
    List<String> aliases();

    @Override
    void aliases(List<String> aliases);

    @Override
    NavigatableReference navigateToAlias(String alias);

    @Override
    List<Permission> permissions();

    @Override
    void permissions(List<String> permissions);

    CommandNode parent();

    List<Argument> arguments();

    NavigatableReference navigatable();

}
