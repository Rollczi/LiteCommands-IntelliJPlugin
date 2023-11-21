package dev.rollczi.litecommands.intellijplugin.icon;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public class LiteIcon {

    public static final Icon COMMAND_MARK_LINE = loadIcon("/assets/icons/expUi/applicationRemote.svg");
    public static final Icon COMMAND_STRUCTURE = loadIcon("/assets/icons/expUi/groupByModule.svg");
    public static final Icon COMMAND_ELEMENT = loadIcon("/assets/icons/expUi/inlayRenameInCommentsActive.svg");
    public static final Icon COMMAND_ELEMENT_ALIAS = loadIcon("/assets/icons/expUi/inlayRenameInComments.svg");

    public static final Icon PERMISSIONS = loadIcon("/assets/icons/expUi/columnGoldKey.svg");
    public static final Icon PERMISSION_ELEMENT = loadIcon("/assets/icons/expUi/mongoFieldGoldKey.svg");

    public static final Icon EXECUTORS = loadIcon("/assets/icons/expUi/ocdRunConfiguration.svg");
    public static final Icon EXECUTOR_ELEMENT = loadIcon("/assets/icons/expUi/runParatest.svg");

    public static final Icon BETA = AllIcons.General.Beta;
    public static final Icon ELEMENT = AllIcons.General.ChevronRight;
    public static final Icon ADD = AllIcons.General.Add;
    public static final Icon EDIT = AllIcons.General.Inline_edit;

    private static Icon loadIcon(String path) {
        return IconLoader.getIcon(path, LiteIcon.class);
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
