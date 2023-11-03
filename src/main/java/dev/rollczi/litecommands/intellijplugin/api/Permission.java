package dev.rollczi.litecommands.intellijplugin.api;

import com.intellij.pom.Navigatable;
import dev.rollczi.litecommands.intellijplugin.navigatable.NavigatableReference;

public interface Permission {

    String name();

    NavigatableReference navigatable();

}
