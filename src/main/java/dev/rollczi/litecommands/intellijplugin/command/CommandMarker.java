package dev.rollczi.litecommands.intellijplugin.command;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor;
import com.intellij.icons.AllIcons.Actions;
import com.intellij.icons.AllIcons.General;
import com.intellij.icons.AllIcons.Ide;
import com.intellij.icons.AllIcons.Nodes;
import com.intellij.icons.AllIcons.RunConfigurations;
import com.intellij.icons.AllIcons.Toolwindows;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.permission.Permission;
import dev.rollczi.litecommands.annotations.permission.Permissions;
import dev.rollczi.litecommands.intellijplugin.marker.LiteLineMarkerInfo;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import org.jetbrains.annotations.NotNull;

public class CommandMarker extends LineMarkerProviderDescriptor {

    @Override
    public String getName() {
        return "LiteCommands";
    }

    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        if (!(element instanceof PsiClass)) {
            return null;
        }

        PsiClass psiClass = (PsiClass) element;
        List<Command> commands = AnnotationFactory.fromAnnotatedPsi(Command.class, psiClass);

        if (commands.isEmpty()) {
            return null;
        }

        Command command = commands.get(0);
        String name = command.name();

        LiteComponent box = LiteMargin.ofInside(new LiteBox(), 10)
            .with(LiteBadge.margined(Toolwindows.ToolWindowStructure, "/" + command.name()))
            .with(permissions(psiClass));

/*
        JBBox validations = new JBBox(BoxLayout.Y_AXIS);
        validations.add(LiteBadge.margined(General.InspectionsEye, "ValidatorA"));
        securityPane.addTab("Validations", validations);

        box.add(securityPane);*/


        List<Icon> list = Arrays.asList(

            // commands
            Actions.Run_anything,
            Nodes.Console,

            // argument
            Toolwindows.ToolWindowStructure,
            Toolwindows.ToolWindowChanges,
            Toolwindows.ToolWindowCommit,
            Nodes.IdeaModule,

            // general
            General.Beta,

            // sec
            General.RunWithCoverage,
            RunConfigurations.TrackCoverage,
            Ide.Readonly,
            Ide.Readwrite,

            // inspec
            General.InspectionsEye,

            // list
            General.ChevronRight,
            General.ChevronLeft,

            // docs
            General.ReaderMode
        );



    /*    JBMenuItem item = new JBMenuItem("/" + name, Actions.RunAll);

        item.setBackground(new Color(68, 255, 110, 20));
        item.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 8, true));
        box.add(item);

        JBComboBoxLabel boxLabel = new JBComboBoxLabel();
        boxLabel.setIcon(Nodes.Services);
        boxLabel.setText("boxLabel");

        JBLayeredPane jbLayeredPane = new JBLayeredPane();
        jbLayeredPane.setName("jbLayeredPane");

        box.add(pane);
        box.add(jbLayeredPane);
        box.add(boxLabel);

        JBSplitter splitter = new JBSplitter();
        splitter.setDividerWidth(1);
        splitter.setToolTipText("splitter");
        splitter.setDividerPositionStrategy(DividerPositionStrategy.KEEP_FIRST_SIZE);
        splitter.setFirstComponent(new JBLabel("firstComponent", ComponentStyle.LARGE));
        splitter.setSecondComponent(new JBLabel("secondComponent", ComponentStyle.REGULAR));
        splitter.setHonorComponentsMinimumSize(true);
        splitter.setProportion(0.5f);

        box.add(splitter);
        box.add(new BadgeComponent("badgeComponent", new Color(200, 100, 100)));*/

        return LiteLineMarkerInfo.builder(psiClass.getNameIdentifier())
            .name("Command viewer")
            .icon(Nodes.Console)
            .viewIcon(General.Beta)
            .component(box.asComponent())
            .build();
    }

    private LiteComponent permissions(PsiClass psiClass) {
        LiteComponent permissionsContent = new LiteBox();

        AnnotationFactory.fromAnnotatedPsi(Permission.class, psiClass).forEach(annotation -> {
            for (String permission : annotation.value()) {
                permissionsContent.add(LiteBadge.of(permission, LiteColors.NONE, Ide.Readwrite));
            }
        });

        AnnotationFactory.fromAnnotatedPsi(Permissions.class, psiClass).forEach(annotation -> {
            for (Permission permissionAnnotation : annotation.value()) {
                for (String permission : permissionAnnotation.value()) {
                    permissionsContent.add(LiteBadge.of(permission, LiteColors.NONE, Ide.Readwrite));
                }
            }
        });

        return new LiteBox()
            .with(LiteMargin.of(10,
                LiteBox.invisible(LiteBadge.of(RunConfigurations.TrackCoverage, "Permissions")),
                permissionsContent
            ))
            ;
    }


}
