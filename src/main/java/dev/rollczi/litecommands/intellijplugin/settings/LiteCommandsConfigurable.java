package dev.rollczi.litecommands.intellijplugin.settings;

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.codeInsight.daemon.impl.InlayHintsPassFactoryInternal;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.NlsContexts;
import javax.swing.JComponent;
import org.jetbrains.annotations.Nullable;

public class LiteCommandsConfigurable implements Configurable {

    private final LiteCommandsSettings settings;
    private final LiteCommandsSettings editedSettings;

    LiteCommandsConfigurable() {
        this.settings = LiteCommandsSettings.getInstance();
        this.editedSettings = settings.copy();
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "LiteCommands Settings";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return new LiteSettingsBox()
            .withCheckBox("Show argument hints", editedSettings.showArgumentHints, value -> editedSettings.showArgumentHints = value);
    }

    @Override
    public boolean isModified() {
        return !editedSettings.equals(settings);
    }

    @Override
    public void apply() {
        settings.loadState(editedSettings);

        // reload inlay hints
        InlayHintsPassFactoryInternal.Companion.forceHintsUpdateOnNextPass();

        for (Project openProject : ProjectManager.getInstance().getOpenProjects()) {
            DaemonCodeAnalyzer.getInstance(openProject).restart();
        }
    }

    @Override
    public void reset() {
        editedSettings.loadState(settings.getState());
    }

    public LiteCommandsSettings getEdited() {
        return editedSettings;
    }

    public static LiteCommandsConfigurable createConfigurable() {
        return new LiteCommandsConfigurable();
    }

}
