package dev.rollczi.litecommands.intellijplugin.inlay;

import com.intellij.codeInsight.hints.ImmediateConfigurable;
import com.intellij.codeInsight.hints.InlayHintsCollector;
import com.intellij.codeInsight.hints.InlayHintsProvider;
import com.intellij.codeInsight.hints.InlayHintsSink;
import com.intellij.codeInsight.hints.SettingsKey;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiClassOwner;
import com.intellij.psi.PsiFile;
import dev.rollczi.litecommands.intellijplugin.settings.LiteCommandsSettings;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"UnstableApiUsage"})
public class LiteInlayHints implements InlayHintsProvider<LiteCommandsSettings> {

    public static final String KEY = "litecommands.argument";
    public static final String NAME = "LiteCommands argument";

    @NotNull
    @Override
    public SettingsKey<LiteCommandsSettings> getKey() {
        return new SettingsKey<>(KEY);
    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getName() {
        return NAME;
    }

    @Nullable
    @Override
    public String getPreviewText() {
        return NAME;
    }

    @NotNull
    @Override
    public ImmediateConfigurable createConfigurable(@NotNull LiteCommandsSettings settings) {
        return new InlayHintsConfigurable();
    }

    @NotNull
    @Override
    public LiteCommandsSettings createSettings() {
        return LiteCommandsSettings.getInstance();
    }

    @Nullable
    @Override
    public InlayHintsCollector getCollectorFor(@NotNull PsiFile psiFile, @NotNull Editor editor, @NotNull LiteCommandsSettings settings, @NotNull InlayHintsSink inlayHintsSink) {
        if (!(psiFile instanceof PsiClassOwner)) {
            return null;
        }

        if (!settings.showArgumentHints) {
            return null;
        }

        return new LiteInlayHintsCollector();
    }

}