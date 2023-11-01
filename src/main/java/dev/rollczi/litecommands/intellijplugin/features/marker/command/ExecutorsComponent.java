package dev.rollczi.litecommands.intellijplugin.features.marker.command;

import com.intellij.ide.HelpTooltip;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.intellijplugin.api.CommandNode;
import dev.rollczi.litecommands.intellijplugin.api.ExecutorNode;
import dev.rollczi.litecommands.intellijplugin.features.icon.LiteIcon;
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

    public ExecutorsComponent(CommandNode command) {
        super(new BorderLayout());

        if (!command.hasExecutors()) {
            this.setVisible(false);
            return;
        }

        this.add(this.title(), BorderLayout.NORTH);
        this.add(this.content(command), BorderLayout.CENTER);
    }

    private JComponent title() {
        return new LiteTitledSeparator(LiteIcon.EXECUTORS, "Executors");
    }

    private JComponent content(CommandNode command) {
        JPanel content = new JPanel(new BorderLayout());
        content.add(commandList(command), BorderLayout.WEST);
        content.add(new AddButton(), BorderLayout.EAST);

        return content;
    }

    private JComponent commandList(CommandNode command) {
        LiteBox box = new LiteBox(LiteColors.NONE);

        for (ExecutorNode executor : command.executors()) {
            box.with(executorBadge(executor));
        }

        return box;
    }

    private LiteComponent executorBadge(ExecutorNode executor) {
        String structure = executor.structure();

        LiteActionBadge badge = new LiteActionBadge(
            structure,
            LiteColors.GRAY,
            LiteColors.GRAY_LIGHT,
            LiteIcon.EXECUTOR_ELEMENT,
            LiteMargin.SMALL
        );

        badge.addListener(() -> executor.navigatable().navigate(true));

        badge.addHoverListener(mouseEvent -> {
            HelpTooltip tooltip = new HelpTooltip();

            tooltip.setTitle("Executor");
            tooltip.setLink(structure, () -> executor.navigatable().navigate(true));
            tooltip.setDescription(structure);
            tooltip.installOn(this);
        });

        return LiteBox.invisible(badge).margined(LiteMargin.TINY);
    }

}
