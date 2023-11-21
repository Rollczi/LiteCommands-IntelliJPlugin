package dev.rollczi.litecommands.intellijplugin.api;

import dev.rollczi.litecommands.intellijplugin.navigatable.NavigatableReference;

public interface PermissionEntry {

    String name();

    NavigatableReference navigatable();

}
