package dev.rollczi.litecommands.intellijplugin.features.marker.command;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.DumbService;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.DataIndexer;
import com.intellij.util.indexing.FileBasedIndex;
import com.intellij.util.indexing.FileBasedIndexExtension;
import com.intellij.util.indexing.ID;
import dev.rollczi.litecommands.intellijplugin.api.CommandNode;
import dev.rollczi.litecommands.intellijplugin.features.icon.LiteIcon;
import dev.rollczi.litecommands.intellijplugin.features.marker.command.dialog.EditDialog;
import dev.rollczi.litecommands.intellijplugin.old.ui.LiteActionBadge;
import dev.rollczi.litecommands.intellijplugin.old.ui.LiteColors;
import dev.rollczi.litecommands.intellijplugin.old.ui.LiteMargin;
import dev.rollczi.litecommands.intellijplugin.features.popup.LitePopupFactory;
import dev.rollczi.litecommands.intellijplugin.util.ScheduleUtil;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

class EditButton extends LiteMargin {

    EditButton(CommandNode command) {
        super(LiteMargin.TINY);

        LiteActionBadge badge = new LiteActionBadge(
            LiteColors.GRAY,
            LiteColors.GRAY_LIGHT,
            LiteIcon.EDIT,
            LiteMargin.SMALL
        );

        badge.addListener((event) -> {
            DumbService dumbService = DumbService.getInstance(command.getFile().getProject());

            dumbService.isAlternativeResolveEnabled();
            LitePopupFactory.showPopup(
                "Loading...",
                "Waiting for indexing...",
                LiteIcon.COMMAND_ELEMENT,
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
                    });
                }
            });
        });

        this.add(badge);
    }

}
