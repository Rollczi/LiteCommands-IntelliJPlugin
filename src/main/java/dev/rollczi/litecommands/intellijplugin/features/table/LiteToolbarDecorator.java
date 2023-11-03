package dev.rollczi.litecommands.intellijplugin.features.table;

import com.intellij.openapi.actionSystem.ActionToolbarPosition;
import com.intellij.ui.JBColor;
import com.intellij.ui.ToolbarDecorator;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JTable;

public final class LiteToolbarDecorator {

    private LiteToolbarDecorator() {
    }

    public static ToolbarDecorator createDecorator(JTable table) {
        return ToolbarDecorator.createDecorator(table)
            .setPanelBorder(BorderFactory.createLineBorder(JBColor.background()))
            .setToolbarBorder(BorderFactory.createLineBorder(JBColor.background()))
            .setScrollPaneBorder(BorderFactory.createLineBorder(JBColor.background()))
            .setToolbarPosition(ActionToolbarPosition.RIGHT)

            .setMinimumSize(new Dimension(250, 0));
    }

}
