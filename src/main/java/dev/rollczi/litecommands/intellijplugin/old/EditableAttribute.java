package dev.rollczi.litecommands.intellijplugin.old;

import com.intellij.icons.AllIcons;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.IncorrectOperationException;
import dev.rollczi.litecommands.intellijplugin.old.ui.*;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.function.Supplier;

public class EditableAttribute extends LiteBox {


    public EditableAttribute(String value, Supplier<PsiAnnotationMemberValue> attribute) {
        LiteActionBadge badge = new LiteActionBadge(value, LiteColors.GRAY, LiteColors.GRAY_LIGHT, null, LiteMargin.SMALL);

        this.add(badge);

        badge.addListener((event) -> {
            this.removeAll();

            Project project = attribute.get().getProject();

            PsiExpressionCodeFragment code = JavaCodeFragmentFactory.getInstance(project).createExpressionCodeFragment(attribute.get().getText(), attribute.get(), null, true);
            Document document = PsiDocumentManager.getInstance(project).getDocument(code);
            EditorTextField editorTextField = new EditorTextField(document, project, JavaFileType.INSTANCE);
            editorTextField.addDocumentListener(new AttributeUpdater(project, editorTextField, attribute));
            editorTextField.getComponent().setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            this.add(editorTextField);
        });
    }

    private static class AttributeUpdater implements DocumentListener {

        private final Project project;
        private final EditorTextField editorTextField;
        private final Supplier<PsiAnnotationMemberValue> attribute;

        public AttributeUpdater(Project project, EditorTextField editorTextField, Supplier<PsiAnnotationMemberValue> attribute) {
            this.project = project;
            this.editorTextField = editorTextField;
            this.attribute = attribute;
        }

        @Override
        public void documentChanged(@NotNull DocumentEvent event) {

            PsiElementFactory factory = JavaPsiFacade.getElementFactory(project);
            try {
                PsiExpression newValue = factory.createExpressionFromText(editorTextField.getText(), null);
                attribute.get().replace(newValue);
            }
            catch (IncorrectOperationException e) {
                editorTextField.setToolTipText(e.getMessage());
            }

        }
    }

}
