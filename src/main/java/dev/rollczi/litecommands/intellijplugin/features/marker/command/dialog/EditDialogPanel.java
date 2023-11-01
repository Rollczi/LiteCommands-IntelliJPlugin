package dev.rollczi.litecommands.intellijplugin.features.marker.command.dialog;

import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBTextField;

import dev.rollczi.litecommands.intellijplugin.api.CommandNode;
import dev.rollczi.litecommands.intellijplugin.features.icon.LiteIcon;
import dev.rollczi.litecommands.intellijplugin.old.ui.LiteTitledSeparator;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class EditDialogPanel extends JPanel {

    // command name
    private final JBTextField nameField = new JBTextField();
    // command aliases list
    private final JBList<String> aliasesList = new JBList<>();

    public EditDialogPanel(CommandNode command) {
        super(new BorderLayout());
        this.add(this.title(), BorderLayout.CENTER);
        this.add(this.commandEditor(command), BorderLayout.CENTER);
    }

    private JComponent title() {
        return new LiteTitledSeparator(LiteIcon.COMMAND_STRUCTURE, "Edit Command");
    }

    private JComponent commandEditor(CommandNode command) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(this.nameField, BorderLayout.NORTH);
        panel.add(this.aliasesList, BorderLayout.CENTER);

        this.nameField.setText(command.name());
        this.aliasesList.setListData(command.aliases().toArray(new String[0]));

        return panel;
    }

    public JBTextField getNameField() {
        return nameField;
    }

    public JBList<String> getAliasesList() {
        return aliasesList;
    }

}
