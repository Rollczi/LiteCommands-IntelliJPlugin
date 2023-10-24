package dev.rollczi.litecommands.intellijplugin.marker.command;

import dev.rollczi.litecommands.intellijplugin.icon.LiteIcon;
import dev.rollczi.litecommands.intellijplugin.old.ui.LiteActionBadge;
import dev.rollczi.litecommands.intellijplugin.old.ui.LiteColors;
import dev.rollczi.litecommands.intellijplugin.old.ui.LiteMargin;

class AddButton extends LiteMargin {

    AddButton() {
        super(LiteMargin.TINY);

        LiteActionBadge badge = new LiteActionBadge(
            "",
            LiteColors.GRAY,
            LiteColors.GRAY_LIGHT,
            LiteIcon.ADD,
            LiteMargin.SMALL
        );

        this.add(badge);
    }

}
