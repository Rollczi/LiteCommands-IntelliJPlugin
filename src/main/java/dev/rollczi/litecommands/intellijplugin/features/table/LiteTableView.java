package dev.rollczi.litecommands.intellijplugin.features.table;

import com.intellij.ui.JBColor;
import com.intellij.ui.table.TableView;
import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.BorderFactory;

public class LiteTableView<Item> extends TableView<Item> {

    public LiteTableView() {
        this.setShowGrid(false);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.setFillsViewportHeight(true);
        this.setIntercellSpacing(new Dimension(0, 0));
        this.getTableHeader().setBorder(BorderFactory.createLineBorder(JBColor.background()));
        this.getTableHeader().setReorderingAllowed(false);
    }

}
