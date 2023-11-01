package dev.rollczi.litecommands.intellijplugin.api;

import com.intellij.pom.Navigatable;

public interface Permission {

    String name();

    Navigatable navigatable();

}
