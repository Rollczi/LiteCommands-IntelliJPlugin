package dev.rollczi.litecommands.intellijplugin.features.marker.command;

import dev.rollczi.litecommands.intellijplugin.api.CommandNode;
import dev.rollczi.litecommands.intellijplugin.features.icon.LiteIcon;
import dev.rollczi.litecommands.intellijplugin.features.marker.command.dialog.EditDialog;
import dev.rollczi.litecommands.intellijplugin.old.ui.LiteActionBadge;
import dev.rollczi.litecommands.intellijplugin.old.ui.LiteColors;
import dev.rollczi.litecommands.intellijplugin.old.ui.LiteMargin;
import dev.rollczi.litecommands.intellijplugin.features.popup.LitePopupFactory;

class EditButton extends LiteMargin {

    EditButton(CommandNode command) {
        super(LiteMargin.TINY);

        LiteActionBadge badge = new LiteActionBadge(
            LiteColors.GRAY,
            LiteColors.GRAY_LIGHT,
            LiteIcon.EDIT,
            LiteMargin.SMALL
        );

        badge.addListener(() -> {
            LitePopupFactory.closePopups();

            boolean isOk = new EditDialog(command).showAndGet();

            if (isOk) {

            }
        });

        this.add(badge);
    }

}
