package dev.rollczi.litecommands.intellijplugin.features.marker.command;

import dev.rollczi.litecommands.intellijplugin.api.CommandNode;
import dev.rollczi.litecommands.intellijplugin.api.Permission;
import dev.rollczi.litecommands.intellijplugin.features.icon.LiteIcon;
import dev.rollczi.litecommands.intellijplugin.old.ui.LiteActionBadge;
import dev.rollczi.litecommands.intellijplugin.old.ui.LiteBox;
import dev.rollczi.litecommands.intellijplugin.old.ui.LiteColors;
import dev.rollczi.litecommands.intellijplugin.old.ui.LiteComponent;
import dev.rollczi.litecommands.intellijplugin.old.ui.LiteMargin;
import dev.rollczi.litecommands.intellijplugin.old.ui.LiteTitledSeparator;
import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

class PermissionsComponent extends JPanel {

    PermissionsComponent(CommandNode command) {
        super(new BorderLayout());

        if (command.permissions().isEmpty()) {
            this.setVisible(false);
            return;
        }

        this.add(this.title(), BorderLayout.NORTH);
        this.add(this.content(command), BorderLayout.CENTER);
    }

    private JComponent title() {
        return new LiteTitledSeparator(LiteIcon.PERMISSIONS, "Permissions");
    }

    private JComponent content(CommandNode command) {
        JPanel content = new JPanel(new BorderLayout());
        content.add(permissionsList(command), BorderLayout.WEST);
        content.add(new EditButton(command), BorderLayout.EAST);

        return content;
    }

    private JComponent permissionsList(CommandNode command) {
        LiteBox box = new LiteBox(LiteColors.NONE);

        for (Permission permission : command.permissions()) {
            box.with(permissionBadge(permission));
        }

        return box;
    }

    private LiteComponent permissionBadge(Permission permission) {
        LiteActionBadge badge = new LiteActionBadge(
            permission.name(),
            LiteColors.GRAY,
            LiteColors.GRAY_LIGHT,
            LiteIcon.PERMISSION_ELEMENT,
            LiteMargin.SMALL
        );

        badge.addListener(() -> permission.navigatable().navigate(true));

        return LiteBox.invisible(badge).margined(LiteMargin.TINY);
    }

}
