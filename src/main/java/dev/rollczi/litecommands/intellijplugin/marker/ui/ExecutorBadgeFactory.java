package dev.rollczi.litecommands.intellijplugin.marker.ui;

import com.intellij.ide.HelpTooltip;
import dev.rollczi.litecommands.intellijplugin.api.ExecutorNode;
import dev.rollczi.litecommands.intellijplugin.icon.LiteIcon;
import dev.rollczi.litecommands.intellijplugin.ui.LiteActionBadge;
import dev.rollczi.litecommands.intellijplugin.ui.LiteBox;
import dev.rollczi.litecommands.intellijplugin.ui.LiteColors;
import dev.rollczi.litecommands.intellijplugin.ui.LiteComponent;
import dev.rollczi.litecommands.intellijplugin.ui.LiteMargin;
import javax.swing.JComponent;

public final class ExecutorBadgeFactory {

    private ExecutorBadgeFactory() {
    }

    public static LiteComponent create(ExecutorNode executor, JComponent component) {
        String structure = executor.structure();

        LiteActionBadge badge = new LiteActionBadge(
            structure,
            LiteColors.GRAY,
            LiteColors.GRAY_LIGHT,
            LiteIcon.EXECUTOR_ELEMENT,
            LiteMargin.SMALL
        );

        badge.addListener((event) -> executor.navigatable().highlight());

        badge.addHoverListener(mouseEvent -> {
            HelpTooltip tooltip = new HelpTooltip();

            tooltip.setTitle("Executor");
            tooltip.setLink(structure, () -> executor.navigatable().highlight());
            tooltip.setDescription(structure);
            tooltip.installOn(component);
        });

        return LiteBox.invisible(badge).margined(LiteMargin.TINY);
    }

}
