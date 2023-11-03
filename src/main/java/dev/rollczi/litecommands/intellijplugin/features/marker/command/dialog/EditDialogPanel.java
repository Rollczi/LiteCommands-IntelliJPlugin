package dev.rollczi.litecommands.intellijplugin.features.marker.command.dialog;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.lang.Language;
import com.intellij.lang.LanguageAnnotators;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.actionSystem.ActionToolbarPosition;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.editor.impl.ImaginaryEditor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.JavaCodeFragmentFactory;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpressionCodeFragment;
import com.intellij.psi.PsiFile;
import com.intellij.ui.EditorCustomization;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.ErrorStripeEditorCustomization;
import com.intellij.ui.JBColor;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.TableView;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import dev.rollczi.litecommands.intellijplugin.api.CommandNode;
import dev.rollczi.litecommands.intellijplugin.features.icon.LiteIcon;
import dev.rollczi.litecommands.intellijplugin.features.table.TextColumnBuilder;
import dev.rollczi.litecommands.intellijplugin.old.ui.LiteBadge;
import dev.rollczi.litecommands.intellijplugin.old.ui.LiteColors;
import dev.rollczi.litecommands.intellijplugin.old.ui.LiteTitledSeparator;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import org.jetbrains.annotations.NotNull;

class EditDialogPanel extends JPanel {

    private static final ColumnInfo<NameReference, String> ALIAS_NAME_COLUMN = new TextColumnBuilder<NameReference>("Name")
        .firstIcon(nameReference -> LiteIcon.COMMAND_ELEMENT)
        .icon(nameReference -> LiteIcon.COMMAND_ELEMENT_ALIAS)
        .valueOf(NameReference::getName)
        .setValue((nameReference, input) -> nameReference.setName(input))
        .build();

    private final TableView<NameReference> namesList = new TableView<>(new ListTableModel<>(ALIAS_NAME_COLUMN));

    public EditDialogPanel(CommandNode command) {
        super(new BorderLayout());
        this.add(this.title(), BorderLayout.NORTH);
        this.add(this.commandEditor(command), BorderLayout.CENTER);
    }

    private JComponent title() {
        return new LiteTitledSeparator(LiteIcon.COMMAND_STRUCTURE, "Command Structure");
    }

    private JComponent commandEditor(CommandNode command) {
        ListTableModel<NameReference> model = this.namesList.getListTableModel();

        model.addRow(new NameReference(command.name()));
        model.addRows(command.aliases().stream().map(NameReference::new).toList());

        this.namesList.setShowGrid(false);
        this.namesList.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.namesList.setFillsViewportHeight(true);
        this.namesList.setIntercellSpacing(new Dimension(0, 0));
        this.namesList.getTableHeader().setBorder(BorderFactory.createLineBorder(JBColor.background()));
        this.namesList.getTableHeader().setReorderingAllowed(false);

        JPanel jPanel = ToolbarDecorator.createDecorator(this.namesList)
            .setPanelBorder(BorderFactory.createLineBorder(JBColor.background()))
            .setToolbarBorder(BorderFactory.createLineBorder(JBColor.background()))
            .setScrollPaneBorder(BorderFactory.createLineBorder(JBColor.background()))
            .setToolbarPosition(ActionToolbarPosition.RIGHT)

            .setMinimumSize(new Dimension(300, 0))

            .setAddAction(anActionButton -> {
                model.addRow(new NameReference(""));

                this.namesList.editCellAt(model.getRowCount() - 1, 0);
                this.namesList.getEditorComponent().requestFocus();
            })

            .createPanel();


        JPanel panel = new JPanel(new BorderLayout());
        panel.add(jPanel, BorderLayout.CENTER);

        return panel;
    }

    public TableView<NameReference> getNamesList() {
        return namesList;
    }

}
