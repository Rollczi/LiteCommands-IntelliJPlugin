package dev.rollczi.litecommands.intellijplugin.api;

import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.intellijplugin.old.annotation.AnnotationFactory;
import dev.rollczi.litecommands.intellijplugin.old.annotation.AnnotationHolder;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import panda.std.Pair;

public interface ExecutorNode extends Node {

    @Override
    String name();

    @Override
    void name(String name);

    @Override
    Navigatable navigateToName();

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
    Navigatable navigateToAlias(String alias);

    @Override
    List<Permission> permissions();

    @Override
    void permissions(List<String> permissions);

    CommandNode parent();

    List<Argument> arguments();

    Navigatable navigatable();

}
