package dev.rollczi.litecommands.intellijplugin.api;

import com.intellij.pom.Navigatable;
import java.util.List;

public interface Node extends PermissionsOwner {

    String name();

    void name(String name);

    Navigatable navigateToName();

    List<String> aliases();

    void aliases(List<String> aliases);

    Navigatable navigateToAlias(String alias);

}
