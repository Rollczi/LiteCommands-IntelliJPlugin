package dev.rollczi.litecommands.intellijplugin.api;

import dev.rollczi.litecommands.intellijplugin.navigatable.NavigatableReference;
import java.util.List;

public interface PermissionsDefinition {

    List<PermissionEntry> permissions();

    void permissions(List<String> permissions);

    boolean isEmpty();

    NavigatableReference navigatable();

}
