package dev.rollczi.litecommands.intellijplugin.marker.command;

import com.intellij.openapi.project.Project;
import dev.rollczi.litecommands.intellijplugin.api.CommandNode;
import dev.rollczi.litecommands.intellijplugin.icon.LiteIcons;
import dev.rollczi.litecommands.intellijplugin.marker.command.dialog.EditDialog;
import dev.rollczi.litecommands.intellijplugin.ui.LiteActionBadge;
import dev.rollczi.litecommands.intellijplugin.ui.LiteColors;
import dev.rollczi.litecommands.intellijplugin.ui.LiteMargin;
import dev.rollczi.litecommands.intellijplugin.popup.LitePopupFactory;
import dev.rollczi.litecommands.intellijplugin.util.IdeaTask;
import dev.rollczi.litecommands.intellijplugin.util.IdeaTaskType;
import javax.swing.JPanel;

class EditButton extends LiteMargin {

    EditButton(CommandNode command) {
        super(LiteMargin.TINY);

        LiteActionBadge badge = new LiteActionBadge(
            LiteColors.GRAY,
            LiteColors.GRAY_LIGHT,
            LiteIcons.EDIT,
            LiteMargin.SMALL
        );

        badge.addListener(event -> {
            LitePopupFactory.showPopup(
                "Loading...",
                "Waiting for indexing...",
                LiteIcons.COMMAND_ELEMENT,
                new JPanel(),
                event
            );

            Project project = command.getFile().getProject();

            IdeaTask.startInSmart(project)
                .then(() -> LitePopupFactory.closePopups())
                .flatMap(() -> EditDialog.create(command))
                .filter(IdeaTaskType.UI, editDialog -> editDialog.showAndGet())
                .then(IdeaTaskType.writeCommand(project), dialog -> {
                    command.name(dialog.getName());
                    command.aliases(dialog.getAliases());
                    command.permissionsDefinition().permissions(dialog.getPermissions());
                });
        });

        this.add(badge);
    }

}
