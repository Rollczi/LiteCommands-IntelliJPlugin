package dev.rollczi.litecommands.intellijplugin.marker.command.dialog;

import com.intellij.ui.table.TableView;
import com.intellij.util.ui.ListTableModel;
import dev.rollczi.litecommands.intellijplugin.api.CommandNode;
import dev.rollczi.litecommands.intellijplugin.icon.LiteIcons;
import dev.rollczi.litecommands.intellijplugin.table.LiteTableView;
import dev.rollczi.litecommands.intellijplugin.table.LiteToolbarDecorator;
import dev.rollczi.litecommands.intellijplugin.table.TextColumnInfo;
import dev.rollczi.litecommands.intellijplugin.ui.LiteTitledSeparator;
import dev.rollczi.litecommands.intellijplugin.util.IdeaTask;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;
import java.util.stream.Stream;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;

class EditDialogPanel extends Box {

    private static final TextColumnInfo<TextReference> NAMES_COLUMN = new TextColumnInfo.Builder<TextReference>("Name")
        .firstIcon(textReference -> LiteIcons.COMMAND_ELEMENT)
        .icon(textReference -> LiteIcons.COMMAND_ELEMENT_ALIAS)
        .valueOf(TextReference::getName)
        .setValue((textReference, input) -> textReference.setName(input))
        .build();

    private static final TextColumnInfo<TextReference> PERMISSIONS = new TextColumnInfo.Builder<TextReference>("Permission")
        .icon(textReference -> LiteIcons.PERMISSION_ELEMENT)
        .valueOf(TextReference::getName)
        .setValue((textReference, input) -> textReference.setName(input))
        .build();

    private final LiteTableView<TextReference> namesList = new LiteTableView<>();
    private final LiteTableView<TextReference> permissionsList = new LiteTableView<>();

    public EditDialogPanel() {
        super(BoxLayout.Y_AXIS);
        this.setAlignmentX(JComponent.LEFT_ALIGNMENT);
    }

    static IdeaTask<EditDialogPanel> create(CommandNode command) {
        EditDialogPanel editDialogPanel = new EditDialogPanel();

        return IdeaTask.start()
            .write(() -> editDialogPanel.commandStructure(command))
            .ui(jComponent -> editDialogPanel.add(jComponent))
            .write(() -> editDialogPanel.permissions(command))
            .ui(jComponent -> editDialogPanel.add(jComponent))
            .map(unused -> editDialogPanel);
    }

    private JComponent commandStructure(CommandNode command) {
        List<TextReference> items = Stream.concat(
                Stream.of(command.name()),
                command.aliases().stream()
            )
            .map(name -> new TextReference(name))
            .toList();

        return editor(LiteIcons.COMMAND_STRUCTURE, "Command Structure", NAMES_COLUMN, this.namesList, items);
    }

    private JComponent permissions(CommandNode command) {
        List<TextReference> items = command.permissionsDefinition().permissions()
            .stream()
            .map(permissionEntry -> permissionEntry.name())
            .map(name -> new TextReference(name))
            .toList();

        return editor(LiteIcons.PERMISSIONS, "Permissions", PERMISSIONS, this.permissionsList, items);
    }

    private JComponent editor(Icon icon, String name, TextColumnInfo<TextReference> column, LiteTableView<TextReference> tableView, List<TextReference> items) {
        ListTableModel<TextReference> model = column.toModel();

        model.addRows(items);
        tableView.setModel(model);

        JPanel jPanel = LiteToolbarDecorator.createDecorator(tableView)
            .setAddAction(anActionButton -> {
                tableView.stopEditing();

                Component editorComponent = tableView.getEditorComponent();
                if (model.getRowCount() == 0 || model.getItem(model.getRowCount() - 1).getName().isEmpty()) {
                    tableView.editCellAt(model.getRowCount() - 1, 0);
                    if (editorComponent != null) {
                        editorComponent.requestFocus();
                    }
                    return;
                }

                model.addRow(new TextReference(""));

                tableView.editCellAt(model.getRowCount() - 1, 0);
                if (editorComponent != null) {
                    editorComponent.requestFocus();
                }
            })
            .setEditAction(anActionButton -> {
                int selectedRow = tableView.getSelectedRow();

                if (selectedRow == -1) {
                    return;
                }

                tableView.editCellAt(selectedRow, 0);
                tableView.getEditorComponent().requestFocus();
            })
            .createPanel();

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new LiteTitledSeparator(icon, name), BorderLayout.NORTH);
        panel.add(jPanel, BorderLayout.CENTER);

        return panel;
    }

    public TableView<TextReference> getNamesList() {
        return namesList;
    }

    public TableView<TextReference> getPermissionsList() {
        return permissionsList;
    }

}
