package dev.rollczi.litecommands.intellijplugin.marker.command;

import dev.rollczi.litecommands.intellijplugin.icon.LiteIcons;
import dev.rollczi.litecommands.intellijplugin.ui.LiteActionBadge;
import dev.rollczi.litecommands.intellijplugin.ui.LiteColors;
import dev.rollczi.litecommands.intellijplugin.ui.LiteMargin;

class AddButton extends LiteMargin {

    AddButton() {
        super(LiteMargin.TINY);

        LiteActionBadge badge = new LiteActionBadge(
            "",
            LiteColors.GRAY,
            LiteColors.GRAY_LIGHT,
            LiteIcons.ADD,
            LiteMargin.SMALL
        );

        this.add(badge);
    }

}
