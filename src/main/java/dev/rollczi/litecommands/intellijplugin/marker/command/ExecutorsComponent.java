package dev.rollczi.litecommands.intellijplugin.marker.command;

import com.intellij.ide.HelpTooltip;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.intellijplugin.icon.LiteIcon;
import dev.rollczi.litecommands.intellijplugin.old.annotation.AnnotationFactory;
import dev.rollczi.litecommands.intellijplugin.old.annotation.AnnotationHolder;
import dev.rollczi.litecommands.intellijplugin.old.ui.*;
import panda.std.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

class ExecutorsComponent extends JPanel {

    public ExecutorsComponent(AnnotationHolder<Command> command, PsiClass psiClass) {
        super(new BorderLayout());
        List<AnnotationHolder<Execute>> executors = getExecutors(psiClass);

        if (executors.isEmpty()) {
            this.setVisible(false);
            return;
        }

        this.add(this.title(), BorderLayout.NORTH);
        this.add(this.content(command, executors), BorderLayout.CENTER);
    }

    private JComponent title() {
        return new LiteTitledSeparator(LiteIcon.EXECUTORS, "Executors");
    }

    private JComponent content(AnnotationHolder<Command> command, List<AnnotationHolder<Execute>> permissions) {
        JPanel content = new JPanel(new BorderLayout());
        content.add(commandList(command, permissions), BorderLayout.WEST);
        content.add(new AddButton(), BorderLayout.EAST);

        return content;
    }

    private JComponent commandList(AnnotationHolder<Command> command, List<AnnotationHolder<Execute>> permissions) {
        LiteBox box = new LiteBox(LiteColors.NONE);

        for (AnnotationHolder<Execute> executor : permissions) {
            box.with(executorBadge(command, executor));
        }

        return box;
    }

    private LiteComponent executorBadge(AnnotationHolder<Command> command, AnnotationHolder<Execute> executor) {
        String executorBase = executorStructure(command, executor);

        LiteActionBadge badge = new LiteActionBadge(
            executorBase,
            LiteColors.GRAY,
            LiteColors.GRAY_LIGHT,
            LiteIcon.EXECUTOR_ELEMENT,
            LiteMargin.SMALL
        );

        badge.addListener(() -> {
            if (executor.asPsi() instanceof Navigatable navigatable) {
                navigatable.navigate(true);
            }
        });

        badge.addHoverListener(mouseEvent -> {
            HelpTooltip tooltip = new HelpTooltip();

            tooltip.setTitle("Executor");
            tooltip.setLink(executorBase, () -> {
                if (executor.asPsi() instanceof Navigatable navigatable) {
                    navigatable.navigate(true);
                }
            });
            tooltip.setDescription(executorBase);
            tooltip.installOn(this);
        });

        return LiteBox.invisible(badge).margined(LiteMargin.TINY);
    }

    private String executorStructure(AnnotationHolder<Command> command, AnnotationHolder<Execute> executor) {
        Command commandAnnotation = command.asAnnotation();
        Execute annotation = executor.asAnnotation();

        String executorName = annotation.name() + " ";

        if (annotation.name().isEmpty()) {
            executorName = "";
        }

        String arguments = arguments(executor);
        return "/" + commandAnnotation.name() + " " + executorName + arguments;
    }

    private String arguments(AnnotationHolder<Execute> executor) {
        List<Pair<PsiParameter, Arg>> args = new ArrayList<>();

        PsiAnnotation psi = executor.asPsi();
        if (psi.getParent().getParent() instanceof PsiMethod executorMethod) {
            for (PsiParameter parameter : executorMethod.getParameterList().getParameters()) {
                List<AnnotationHolder<Arg>> list = AnnotationFactory.from(Arg.class, parameter);

                if (!list.isEmpty()) {
                    args.add(Pair.of(parameter, list.get(0).asAnnotation()));
                }
            }
        }

        StringJoiner joiner = new StringJoiner(" ");

        for (Pair<PsiParameter, Arg> arg : args) {
            String name = arg.getSecond().value();

            if (name.isEmpty()) {
                name = arg.getFirst().getName();
            }

            joiner.add("<" + name + ">");
        }

        return joiner.toString();
    }

    private List<AnnotationHolder<Execute>> getExecutors(PsiClass psiClass) {
        List<AnnotationHolder<Execute>> executors = new ArrayList<>();

        for (PsiMethod method : psiClass.getMethods()) {
            PsiAnnotation annotation = method.getAnnotation(Execute.class.getName());

            if (annotation != null) {
                List<AnnotationHolder<Execute>> holder = AnnotationFactory.from(Execute.class, method);

                if (!holder.isEmpty()) {
                    executors.add(holder.get(0));
                }
            }
        }

        return executors;
    }

}
