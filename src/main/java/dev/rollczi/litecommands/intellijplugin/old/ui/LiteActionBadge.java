package dev.rollczi.litecommands.intellijplugin.old.ui;

import javax.swing.Icon;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LiteActionBadge extends LiteBadge {

    private final List<Runnable> listeners = new ArrayList<>();
    private final List<Consumer<MouseEvent>> hoverListeners = new ArrayList<>();

    public LiteActionBadge(Color badgeColor, Color hover, Icon icon, int margin) {
        this("", badgeColor, hover, icon, margin);
    }

    public LiteActionBadge(String badgeText, Color badgeColor, Color hover, Icon icon, int margin) {
        super(badgeText, badgeColor, icon, margin);
        this.asComponent().setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.asComponent().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                listeners.forEach(Runnable::run);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setBadgeColor(hover);
                hoverListeners.forEach(consumer -> consumer.accept(e));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBadgeColor(badgeColor);
            }
        });
    }

    public void addListener(Runnable runnable) {
        listeners.add(runnable);
    }

    public void addHoverListener(Consumer<MouseEvent> consumer) {
        hoverListeners.add(consumer);
    }

}
