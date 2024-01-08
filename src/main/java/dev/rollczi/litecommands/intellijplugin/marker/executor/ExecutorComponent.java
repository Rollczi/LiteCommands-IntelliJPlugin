package dev.rollczi.litecommands.intellijplugin.marker.executor;

import dev.rollczi.litecommands.intellijplugin.api.ExecutorNode;
import dev.rollczi.litecommands.intellijplugin.icon.LiteIcons;
import dev.rollczi.litecommands.intellijplugin.marker.ui.ExecutorBadgeFactory;
import dev.rollczi.litecommands.intellijplugin.ui.LiteBox;
import dev.rollczi.litecommands.intellijplugin.ui.LiteColors;
import dev.rollczi.litecommands.intellijplugin.ui.LiteTitledSeparator;
import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

class ExecutorComponent extends JPanel {

    public ExecutorComponent(ExecutorNode executorNode) {
        super(new BorderLayout());
        this.add(this.title(), BorderLayout.NORTH);
        this.add(this.content(executorNode), BorderLayout.CENTER);
    }

    private JComponent title() {
        return new LiteTitledSeparator(LiteIcons.EXECUTORS, "Executor structure");
    }

    private JComponent content(ExecutorNode command) {
        JPanel content = new JPanel(new BorderLayout());
        content.add(commandList(command), BorderLayout.WEST);
//        content.add(new AddButton(), BorderLayout.EAST); TODO: add dialog for adding new executor

        return content;
    }

    private JComponent commandList(ExecutorNode executorNode) {
        LiteBox box = new LiteBox(LiteColors.NONE);

        box.with(ExecutorBadgeFactory.create(executorNode, this));

        return box;
    }

}
