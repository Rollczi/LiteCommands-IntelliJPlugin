package dev.rollczi.litecommands.intellijplugin.marker.command;

import com.intellij.ui.components.JBBox;
import dev.rollczi.litecommands.intellijplugin.api.CommandNode;
import dev.rollczi.litecommands.intellijplugin.icon.LiteIcon;
import dev.rollczi.litecommands.intellijplugin.navigatable.NavigatableReference;
import dev.rollczi.litecommands.intellijplugin.ui.*;

import javax.swing.*;
import java.awt.*;

class CommandComponent extends JPanel {

    CommandComponent(CommandNode command) {
        super(new BorderLayout());
        this.add(title(), BorderLayout.NORTH);
        this.add(content(command), BorderLayout.CENTER);
    }

    private JComponent title() {
        return new LiteTitledSeparator(LiteIcon.COMMAND_STRUCTURE, "Command Structure");
    }

    private JComponent content(CommandNode command) {
        JPanel content = new JPanel(new BorderLayout());
        content.add(list(command), BorderLayout.WEST);
        content.add(new EditButton(command), BorderLayout.EAST);

        return content;
    }

    private JComponent list(CommandNode command) {
        JBBox box = new JBBox(BoxLayout.Y_AXIS);
        box.setAlignmentX(JComponent.LEFT_ALIGNMENT);

        box.add(createBadge(command.name(), false, command.navigateToName()));

        for (String alias : command.aliases()) {
            box.add(createBadge(alias, true, command.navigateToAlias(alias)));
        }

        return box;
    }

    private JComponent createBadge(String name, boolean isAlias, NavigatableReference source) {
        LiteActionBadge badge = new LiteActionBadge(
            "/" + name + " ...",
            LiteColors.GRAY,
            LiteColors.GRAY_LIGHT,
            isAlias ? LiteIcon.COMMAND_ELEMENT_ALIAS : LiteIcon.COMMAND_ELEMENT,
            LiteMargin.SMALL
        );

        badge.addListener((event) -> source.highlight());

        return LiteBox.invisible(badge).margined(LiteMargin.TINY);
    }

}
