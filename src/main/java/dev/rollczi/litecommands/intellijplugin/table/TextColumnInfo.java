package dev.rollczi.litecommands.intellijplugin.table;

import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import dev.rollczi.litecommands.intellijplugin.ui.LiteActionBadge;
import dev.rollczi.litecommands.intellijplugin.ui.LiteColors;
import dev.rollczi.litecommands.intellijplugin.ui.LiteMargin;
import dev.rollczi.litecommands.shared.Preconditions;
import java.util.function.BiConsumer;
import java.util.function.Function;
import javax.swing.Icon;
import javax.swing.table.TableCellRenderer;
import org.jetbrains.annotations.NotNull;

public class TextColumnInfo<T> extends ColumnInfo<T, String> {

    private final Function<T, Icon> firstIcon;
    private final Function<T, Icon> icon;
    private final Function<T, String> valueOf;
    private final BiConsumer<T, String> setValue;

    private TextColumnInfo(Builder<T> builder) {
        super(builder.name);
        this.firstIcon = builder.firstIcon;
        this.icon = builder.icon;
        this.valueOf = builder.valueOf;
        this.setValue = builder.setValue;

    }

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

    public <Item> ListTableModel<Item> toModel() {
        return new ListTableModel<>(this);
    }

    public static class Builder<T> {
        private final String name;
        private Function<T, Icon> firstIcon;
        private Function<T, Icon> icon;
        private Function<T, String> valueOf;
        private BiConsumer<T, String> setValue;

        public Builder(String name) {
            this.name = name;
        }

        public Builder<T> firstIcon(Function<T, Icon> firstIcon) {
            this.firstIcon = firstIcon;
            return this;
        }

        public Builder<T> icon(Function<T, Icon> icon) {
            this.icon = icon;
            return this;
        }

        public Builder<T> valueOf(Function<T, String> valueOf) {
            this.valueOf = valueOf;
            return this;
        }

        public Builder<T> setValue(BiConsumer<T, String> setValue) {
            this.setValue = setValue;
            return this;
        }

        public TextColumnInfo<T> build() {
            Preconditions.notNull(icon, "icon");
            Preconditions.notNull(valueOf, "valueOf");
            Preconditions.notNull(setValue, "setValue");

            return new TextColumnInfo<>(this);
        }

    }
}
