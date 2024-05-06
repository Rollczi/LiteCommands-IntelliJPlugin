package dev.rollczi.litecommands.intellijplugin.inlay;

import com.intellij.codeInsight.hints.ChangeListener;
import com.intellij.codeInsight.hints.ImmediateConfigurable;
import dev.rollczi.litecommands.intellijplugin.settings.LiteSettingsBox;
import javax.swing.JComponent;
import org.jetbrains.annotations.NotNull;

class InlayHintsConfigurable implements ImmediateConfigurable {

    public InlayHintsConfigurable() {
    }

    @NotNull
    @Override
    public JComponent createComponent(@NotNull ChangeListener changeListener) {
        return new LiteSettingsBox();
    }

}
