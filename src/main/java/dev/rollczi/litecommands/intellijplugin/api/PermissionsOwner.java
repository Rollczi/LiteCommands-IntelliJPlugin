package dev.rollczi.litecommands.intellijplugin.api;

import java.util.List;

public interface PermissionsOwner {

    List<Permission> permissions();

    void permissions(List<String> permissions);

}
