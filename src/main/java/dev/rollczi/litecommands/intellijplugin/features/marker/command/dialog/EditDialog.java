package dev.rollczi.litecommands.intellijplugin.features.marker.command.dialog;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.components.JBTextField;
import dev.rollczi.litecommands.intellijplugin.api.CommandNode;
import dev.rollczi.litecommands.util.LiteCommandsUtil;
import javax.swing.JComponent;
import javax.swing.ListModel;
import org.jetbrains.annotations.Nullable;

public class EditDialog extends DialogWrapper {

    private final EditDialogPanel panel;

    public EditDialog(CommandNode command) {
        super(true);
        this.setTitle("Edit Command");

        this.setOKButtonText("Save");
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
        JBTextField nameField = panel.getNameField();
        String text = nameField.getText();

        try {
            LiteCommandsUtil.checkName(text);
        }
        catch (IllegalArgumentException exception) {
            return new ValidationInfo(exception.getMessage(), nameField);
        }

        ListModel<String> model = panel.getAliasesList().getModel();

        for (int i = 0; i < model.getSize(); i++) {
            String alias = model.getElementAt(i);

            try {
                LiteCommandsUtil.checkName(alias);
            }
            catch (IllegalArgumentException exception) {
                return new ValidationInfo(exception.getMessage(), panel.getAliasesList());
            }
        }

        return null;
    }
}
