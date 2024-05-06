package dev.rollczi.litecommands.intellijplugin.settings;

import com.intellij.ui.components.JBCheckBox;
import dev.rollczi.litecommands.intellijplugin.ui.LiteBox;
import java.util.function.Consumer;

public class LiteSettingsBox extends LiteBox {

    public LiteSettingsBox withCheckBox(String text, boolean selected, Consumer<Boolean> consumer) {
        JBCheckBox checkBox = new JBCheckBox(text);
        checkBox.setSelected(selected);
        checkBox.addActionListener(e -> consumer.accept(checkBox.isSelected()));
        add(checkBox);

        return this;
    }

}
