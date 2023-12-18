package dev.rollczi.litecommands.intellijplugin.marker;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.openapi.editor.markup.GutterIconRenderer.Alignment;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import dev.rollczi.litecommands.LiteCommandsVariables;
import dev.rollczi.litecommands.intellijplugin.popup.LitePopupFactory;

import javax.swing.*;

public class LiteLineMarkerInfo<T extends PsiElement> extends LineMarkerInfo<T> {
    public LiteLineMarkerInfo(String name, String footer, T element, Icon icon, Icon titleIcon, JComponent component) {
        super(
            element,
            element.getTextRange(),
            icon,
            (psi) -> "Click to open " + name,
            (mouseEvent, elt) -> {
                LitePopupFactory.showPopup(name, footer, titleIcon, component, mouseEvent);
            },
            Alignment.RIGHT,
            () -> name
        );
    }

    public static <E extends PsiElement> Builder<E> builder(E psiElement) {
        return new Builder<>(psiElement);
    }

    public static class Builder<E extends PsiElement> {

        private final E psiElement;

        private String name;
        private String footer = "LiteCommands " + LiteCommandsVariables.VERSION + " (" + LiteCommandsVariables.COMMIT.substring(0, 7) + ")";
        private JComponent component;
        private Icon icon;
        private Icon viewIcon;

        public Builder(E psiElement) {
            this.psiElement = psiElement;
        }

        public Builder<E> name(String name) {
            this.name = name;
            return this;
        }

        public Builder<E> footer(String footer) {
            this.footer = footer;
            return this;
        }

        public Builder<E> component(JComponent component) {
            this.component = component;
            return this;
        }

        public Builder<E> lineIcon(Icon icon) {
            this.icon = icon;
            return this;
        }

        public Builder<E> viewIcon(Icon viewIcon) {
            this.viewIcon = viewIcon;
            return this;
        }

        public LiteLineMarkerInfo<E> build() {
            return new LiteLineMarkerInfo<>(name, footer, psiElement, icon, viewIcon, component);
        }

    }

}
