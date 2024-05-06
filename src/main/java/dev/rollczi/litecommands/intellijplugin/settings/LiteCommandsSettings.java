package dev.rollczi.litecommands.intellijplugin.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

@Service
@State(
    name = "LiteCommandsSettings",
    storages = { @Storage("litecommands.xml") }
)
public final class LiteCommandsSettings implements PersistentStateComponent<LiteCommandsSettings> {

    public boolean showArgumentHints = true;

    @Override
    public LiteCommandsSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull LiteCommandsSettings liteCommandsSettings) {
        XmlSerializerUtil.copyBean(liteCommandsSettings, this);
    }

    public LiteCommandsSettings copy() {
        return XmlSerializerUtil.createCopy(this);
    }

    public static LiteCommandsSettings getInstance() {
        return ApplicationManager.getApplication().getService(LiteCommandsSettings.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LiteCommandsSettings that = (LiteCommandsSettings) o;
        return showArgumentHints == that.showArgumentHints;
    }

    @Override
    public int hashCode() {
        return Objects.hash(showArgumentHints);
    }

}