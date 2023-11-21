package dev.rollczi.litecommands.intellijplugin.marker;

import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor;

public abstract class LiteLineMarkerDescriptor extends LineMarkerProviderDescriptor {

    private final static String NAME = "LiteCommands";

    @Override
    public final String getName() {
        return NAME;
    }

}
