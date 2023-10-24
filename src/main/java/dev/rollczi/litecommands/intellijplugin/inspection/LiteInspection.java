package dev.rollczi.litecommands.intellijplugin.inspection;

import com.intellij.codeInspection.LocalInspectionTool;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public abstract class LiteInspection extends LocalInspectionTool {

    private final static String GROUP_NAME = "LiteCommands";

    private final String displayName;

    public LiteInspection(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Sentence) @NotNull String getDisplayName() {
        return this.displayName;
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Sentence) @NotNull String getGroupDisplayName() {
        return GROUP_NAME;
    }

    @Override
    public boolean isEnabledByDefault() {
        return true;
    }

}
