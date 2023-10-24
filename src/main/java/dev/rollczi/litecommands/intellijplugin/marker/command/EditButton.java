package dev.rollczi.litecommands.intellijplugin.marker.command;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.intellijplugin.icon.LiteIcon;
import dev.rollczi.litecommands.intellijplugin.marker.LiteLineMarkerInfo;
import dev.rollczi.litecommands.intellijplugin.marker.command.dialog.EditDialog;
import dev.rollczi.litecommands.intellijplugin.old.ui.LiteActionBadge;
import dev.rollczi.litecommands.intellijplugin.old.ui.LiteColors;
import dev.rollczi.litecommands.intellijplugin.old.ui.LiteMargin;
import dev.rollczi.litecommands.intellijplugin.popup.LitePopupFactory;

class EditButton extends LiteMargin {

    EditButton(Command command) {
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
