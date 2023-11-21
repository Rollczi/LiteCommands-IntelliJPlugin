package dev.rollczi.litecommands.intellijplugin.marker.command;

import dev.rollczi.litecommands.intellijplugin.api.CommandNode;
import dev.rollczi.litecommands.intellijplugin.api.PermissionEntry;
import dev.rollczi.litecommands.intellijplugin.icon.LiteIcon;
import dev.rollczi.litecommands.intellijplugin.ui.LiteActionBadge;
import dev.rollczi.litecommands.intellijplugin.ui.LiteBox;
import dev.rollczi.litecommands.intellijplugin.ui.LiteColors;
import dev.rollczi.litecommands.intellijplugin.ui.LiteComponent;
import dev.rollczi.litecommands.intellijplugin.ui.LiteMargin;
import dev.rollczi.litecommands.intellijplugin.ui.LiteTitledSeparator;
import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

class PermissionsComponent extends JPanel {

    PermissionsComponent(CommandNode command) {
        super(new BorderLayout());

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

        for (PermissionEntry permissionEntry : command.permissionsDefinition().permissions()) {
            box.with(permissionBadge(permissionEntry));
        }

        return box;
    }

    private LiteComponent permissionBadge(PermissionEntry permissionEntry) {
        LiteActionBadge badge = new LiteActionBadge(
            permissionEntry.name(),
            LiteColors.GRAY,
            LiteColors.GRAY_LIGHT,
            LiteIcon.PERMISSION_ELEMENT,
            LiteMargin.SMALL
        );

        badge.addListener((event) -> permissionEntry.navigatable().highlight());

        return LiteBox.invisible(badge).margined(LiteMargin.TINY);
    }

}
