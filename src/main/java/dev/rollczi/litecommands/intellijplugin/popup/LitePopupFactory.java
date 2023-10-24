package dev.rollczi.litecommands.intellijplugin.popup;

import com.intellij.openapi.ui.popup.IconButton;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.awt.RelativePoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public final class LitePopupFactory {

    private static final Set<JBPopup> popups = Collections.newSetFromMap(new WeakHashMap<>());

    public static void closePopups() {
        for (JBPopup popup : popups) {
            popup.cancel();
        }

        popups.clear();
    }

    public static void showPopup(String name, String footer, Icon titleIcon, JComponent component, MouseEvent event) {
        JBPopup popup = JBPopupFactory.getInstance()
            .createComponentPopupBuilder(component, null)
            .setTitle(name)
            .setTitleIcon(new IconButton(name, titleIcon, titleIcon, titleIcon))
            .setAdText(footer)
            .setMovable(true)
            .createPopup();

        Dimension size = component.getPreferredSize();
        size.width += 40;
        popup.setMinimumSize(size);

        popup.setRequestFocus(true);
        popup.show(new RelativePoint(event));

        popups.add(popup);
    }

}
