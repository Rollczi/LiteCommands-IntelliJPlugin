package dev.rollczi.litecommands.intellijplugin.features.marker.command.dialog;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.util.ui.ListTableModel;
import dev.rollczi.litecommands.intellijplugin.api.CommandNode;
import dev.rollczi.litecommands.util.LiteCommandsUtil;
import java.util.List;
import javax.swing.JComponent;
import org.jetbrains.annotations.Nullable;

public class EditDialog extends DialogWrapper {

    private final EditDialogPanel panel;

    public EditDialog(CommandNode command) {
        super(true);
        this.setTitle("Edit Command");

        this.setOKButtonText("Edit");
        this.setCancelButtonText("Cancel");
        this.setOKActionEnabled(true);
        this.setResizable(true);

        this.panel = new EditDialogPanel(command);
        this.init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return panel;
    }

    @Override
    protected @Nullable ValidationInfo doValidate() {
        ListTableModel<TextReference> model = panel.getNamesList().getListTableModel();

        if (model.getItems().isEmpty()) {
            return new ValidationInfo("There must be at least one name", panel.getNamesList());
        }

        for (TextReference item : model.getItems()) {
            try {
                boolean isNotEmpty = LiteCommandsUtil.checkName(item.getName());

                if (isNotEmpty) {
                    continue;
                }

                return new ValidationInfo("Command name cannot be empty", panel.getNamesList());
            }
            catch (IllegalArgumentException exception) {
                return new ValidationInfo(exception.getMessage(), panel.getNamesList());
            }
        }

        return null;
    }

    public String getName() {
        return panel.getNamesList().getListTableModel().getItems().get(0).getName();
    }

    public List<String> getAliases() {
        return panel.getNamesList().getListTableModel().getItems().stream()
            .skip(1)
            .map(TextReference::getName)
            .toList();
    }

    public List<String> getPermissions() {
        return panel.getPermissionsList().getListTableModel().getItems().stream()
            .map(TextReference::getName)
            .toList();
    }

}
