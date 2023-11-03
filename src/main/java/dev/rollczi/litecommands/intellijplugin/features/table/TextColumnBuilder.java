package dev.rollczi.litecommands.intellijplugin.features.table;

import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.JBUI;
import dev.rollczi.litecommands.intellijplugin.features.icon.LiteIcon;
import dev.rollczi.litecommands.intellijplugin.old.ui.LiteActionBadge;
import dev.rollczi.litecommands.intellijplugin.old.ui.LiteBox;
import dev.rollczi.litecommands.intellijplugin.old.ui.LiteColors;
import dev.rollczi.litecommands.intellijplugin.old.ui.LiteMargin;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.swing.Icon;
import javax.swing.border.Border;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TextColumnBuilder<T> {

    private final String name;
    private Function<T, Icon> firstIcon;
    private Function<T, Icon> icon;
    private Function<T, String> valueOf;
    private BiConsumer<T, String> setValue;

    public TextColumnBuilder(String name) {
        this.name = name;
    }

    public TextColumnBuilder<T> firstIcon(Function<T, Icon> firstIcon) {
        this.firstIcon = firstIcon;
        return this;
    }

    public TextColumnBuilder<T> icon(Function<T, Icon> icon) {
        this.icon = icon;
        return this;
    }

    public TextColumnBuilder<T> valueOf(Function<T, String> valueOf) {
        this.valueOf = valueOf;
        return this;
    }

    public TextColumnBuilder<T> setValue(BiConsumer<T, String> setValue) {
        this.setValue = setValue;
        return this;
    }

    public ColumnInfo<T, String> build() {
        return new ColumnInfo<>(name) {
            @Override
            public String valueOf(T t) {
                return valueOf.apply(t);
            }

            @Override
            public @NotNull TableCellRenderer getRenderer(T item) {
                return (table, value, isSelected, hasFocus, row, column) -> {
                    Icon itemIcon = firstIcon != null && row == 0 ? firstIcon.apply(item) : icon.apply(item);

                    LiteActionBadge badge = new LiteActionBadge(
                        (String) value,
                        LiteColors.GRAY,
                        LiteColors.GRAY_LIGHT,
                        itemIcon,
                        LiteMargin.SMALL
                    );

                    if (table.getSelectedRow() == row) {
                        badge.setOpaque(true);
                    }

                    return badge.wrapMargin();
                };
            }

            @Override
            public boolean isCellEditable(T item) {
                return true;
            }

            @Override
            public void setValue(T item, String value) {
                setValue.accept(item, value);
            }
        };
    }

}
