package dev.rollczi.litecommands.intellijplugin.marker.command;

import dev.rollczi.litecommands.intellijplugin.api.CommandNode;
import dev.rollczi.litecommands.intellijplugin.api.ExecutorNode;
import dev.rollczi.litecommands.intellijplugin.icon.LiteIcons;
import dev.rollczi.litecommands.intellijplugin.marker.ui.ExecutorBadgeFactory;
import dev.rollczi.litecommands.intellijplugin.ui.*;

import javax.swing.*;
import java.awt.*;

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
        return new LiteTitledSeparator(LiteIcons.EXECUTORS, "Executors");
    }

    private JComponent content(CommandNode command) {
        JPanel content = new JPanel(new BorderLayout());
        content.add(commandList(command), BorderLayout.WEST);
//        content.add(new AddButton(), BorderLayout.EAST); TODO: add dialog for adding new executor

        return content;
    }

    private JComponent commandList(CommandNode command) {
        LiteBox box = new LiteBox(LiteColors.NONE);

        for (ExecutorNode executor : command.executors()) {
            box.with(ExecutorBadgeFactory.create(executor, this));
        }

        return box;
    }
}
