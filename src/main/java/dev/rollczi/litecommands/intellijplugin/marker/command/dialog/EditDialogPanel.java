package dev.rollczi.litecommands.intellijplugin.marker.command.dialog;

import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBTextField;
import dev.rollczi.litecommands.annotations.command.Command;

import dev.rollczi.litecommands.intellijplugin.icon.LiteIcon;
import dev.rollczi.litecommands.intellijplugin.old.ui.LiteColors;
import dev.rollczi.litecommands.intellijplugin.old.ui.LiteTitledSeparator;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class EditDialogPanel extends JPanel {

    // command name
    private final JBTextField nameField = new JBTextField();
    // command aliases list
    private final JBList<JBTextField> aliasesList = new JBList<>();

    public EditDialogPanel(Command command) {
        super(new BorderLayout());
        this.add(this.title(), BorderLayout.CENTER);
        this.add(this.commandEditor(command), BorderLayout.CENTER);
    }

    private JComponent title() {
        return new LiteTitledSeparator(LiteIcon.COMMAND_STRUCTURE, "Edit Command");
    }

    private JComponent commandEditor(Command command) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(this.nameField, BorderLayout.NORTH);
        panel.add(this.aliasesList, BorderLayout.CENTER);

        this.nameField.setText(command.name());
        this.aliasesList.setListData(Arrays.stream(command.aliases()).map(JBTextField::new).toArray(JBTextField[]::new));

        return panel;
    }

    public JBTextField getNameField() {
        return nameField;
    }

    public JBList<JBTextField> getAliasesList() {
        return aliasesList;
    }

}
