package dev.rollczi.litecommands.intellijplugin.marker.command;

import com.intellij.openapi.project.DumbService;
import dev.rollczi.litecommands.intellijplugin.api.CommandNode;
import dev.rollczi.litecommands.intellijplugin.icon.LiteIcons;
import dev.rollczi.litecommands.intellijplugin.marker.command.dialog.EditDialog;
import dev.rollczi.litecommands.intellijplugin.ui.LiteActionBadge;
import dev.rollczi.litecommands.intellijplugin.ui.LiteColors;
import dev.rollczi.litecommands.intellijplugin.ui.LiteMargin;
import dev.rollczi.litecommands.intellijplugin.popup.LitePopupFactory;
import dev.rollczi.litecommands.intellijplugin.util.ScheduleUtil;
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

        badge.addListener((event) -> {
            DumbService dumbService = DumbService.getInstance(command.getFile().getProject());

            dumbService.isAlternativeResolveEnabled();
            LitePopupFactory.showPopup(
                "Loading...",
                "Waiting for indexing...",
                LiteIcons.COMMAND_ELEMENT,
                new JPanel(),
                event
            );

            dumbService.runWhenSmart(() -> {
                LitePopupFactory.closePopups();

                EditDialog dialog = new EditDialog(command);
                boolean isOk = dialog.showAndGet();

                if (isOk) {
                    ScheduleUtil.invokeLater(command.getFile(), () -> {
                        command.name(dialog.getName());
                        command.aliases(dialog.getAliases());
                        command.permissionsDefinition().permissions(dialog.getPermissions());
                    });
                }
            });
        });

        this.add(badge);
    }

}
