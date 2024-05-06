package dev.rollczi.litecommands.intellijplugin.inlay;

import com.intellij.codeInsight.hints.presentation.BasePresentation;
import com.intellij.codeInsight.hints.presentation.InputHandler;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiType;
import com.intellij.ui.awt.RelativePoint;
import java.awt.AlphaComposite;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;

class InlayHintsIconPresentation extends BasePresentation implements InputHandler {

    private Icon icon;
    private final PsiType psiType;
    private final PsiElement element;
    private final Editor editor;

    public InlayHintsIconPresentation(Icon icon, PsiType psiType, PsiElement element, Editor editor) {
        this.icon = icon;
        this.psiType = psiType;
        this.element = element;
        this.editor = editor;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
        fireContentChanged(new Rectangle(getWidth(), getHeight()));
    }

    public Icon getIcon() {
        return this.icon;
    }

    @Override
    public int getWidth() {
        return icon.getIconWidth();
    }

    @Override
    public int getHeight() {
        return icon.getIconHeight();
    }

    @Override
    public void mouseClicked(@NotNull MouseEvent event, @NotNull Point translated) {
        if (event.getClickCount() == 0) {
            return;
        }

        JBPopupFactory.getInstance()
            .createListPopup(new InlayPopupNewActions(event.getPoint(), psiType, editor, element))
            .show(new RelativePoint(event));
    }

    @Override
    public void mouseExited() {
        editor.getContentComponent().setCursor(Cursor.getDefaultCursor());
    }

    @Override
    public void mouseMoved(@NotNull MouseEvent event, @NotNull Point translated) {
        editor.getContentComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void paint(Graphics2D g, TextAttributes attributes) {
        Graphics2D graphics = (Graphics2D) g.create();
        graphics.setComposite(AlphaComposite.SrcAtop.derive(1.0f));
        icon.paintIcon(editor.getComponent(), graphics, 0, 0);
        graphics.dispose();
    }

    @Override
    public String toString() {
        return "<image>";
    }

}