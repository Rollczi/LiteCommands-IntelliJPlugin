package dev.rollczi.litecommands.intellijplugin.icon;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public interface LiteIcons {

    Icon LOGO = loadIcon("/assets/icons/litecommandsLogo.svg");

    interface Template {
        Icon Java = loadIcon("/assets/icons/litecommandsIcon_class.svg");
        Icon JavaRoot = loadIcon("/assets/icons/litecommandsIcon_class_root.svg");
        Icon Kotlin = loadIcon("/assets/icons/litecommandsIcon_kotlin_class.svg");
        Icon KotlinRoot = loadIcon("/assets/icons/litecommandsIcon_kotlin_root_class.svg");
    }

    Icon COMMAND_MARK_LINE = loadIcon("/assets/icons/expUi/applicationRemote.svg");
    Icon COMMAND_STRUCTURE = loadIcon("/assets/icons/expUi/groupByModule.svg");
    Icon COMMAND_ELEMENT = loadIcon("/assets/icons/expUi/inlayRenameInCommentsActive.svg");
    Icon COMMAND_ELEMENT_ALIAS = loadIcon("/assets/icons/expUi/inlayRenameInComments.svg");

    Icon PERMISSIONS = loadIcon("/assets/icons/expUi/columnGoldKey.svg");
    Icon PERMISSION_ELEMENT = loadIcon("/assets/icons/expUi/mongoFieldGoldKey.svg");

    Icon EXECUTORS = loadIcon("/assets/icons/expUi/ocdRunConfiguration.svg");
    Icon EXECUTOR_ELEMENT = loadIcon("/assets/icons/expUi/runParatest.svg");

    Icon BETA = AllIcons.General.Beta;
    Icon ELEMENT = AllIcons.General.ChevronRight;
    Icon ADD = AllIcons.General.Add;
    Icon EDIT = AllIcons.General.Inline_edit;

    private static Icon loadIcon(String path) {
        return IconLoader.getIcon(path, LiteIcons.class);
    }

    List<Icon> list = Arrays.asList(

            // commands
            AllIcons.Actions.Run_anything,
            AllIcons.Nodes.Console,

            // argument
            AllIcons.Toolwindows.ToolWindowStructure,
            AllIcons.Toolwindows.ToolWindowChanges,
            AllIcons.Toolwindows.ToolWindowCommit,
            AllIcons.Nodes.IdeaModule,

            // general
            AllIcons.General.Beta,

            // sec
            AllIcons.General.RunWithCoverage,
            AllIcons.RunConfigurations.TrackCoverage,
            AllIcons.Ide.Readonly,
            AllIcons.Ide.Readwrite,

            // inspec
            AllIcons.General.InspectionsEye,

            // list
            AllIcons.General.ChevronRight,
            AllIcons.General.ChevronLeft,

            // docs
            AllIcons.General.ReaderMode
    );

}
