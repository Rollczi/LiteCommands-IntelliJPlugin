package dev.rollczi.litecommands.intellijplugin.command;

import java.awt.*;
import javax.swing.*;

public class LiteMargin extends Box implements LiteComponent {
    private int topMargin;
    private int leftMargin;
    private int bottomMargin;
    private int rightMargin;

    public LiteMargin(int topMargin, int leftMargin, int bottomMargin, int rightMargin) {
        super(BoxLayout.Y_AXIS);
        this.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.topMargin = topMargin;
        this.leftMargin = leftMargin;
        this.bottomMargin = bottomMargin;
        this.rightMargin = rightMargin;

        this.setOpaque(false);
        this.setBorder(BorderFactory.createEmptyBorder(topMargin, leftMargin, bottomMargin, rightMargin));
    }

    public void setTopMargin(int topMargin) {
        this.topMargin = topMargin;
        updateBorder();
    }

    public void setLeftMargin(int leftMargin) {
        this.leftMargin = leftMargin;
        updateBorder();
    }

    public void setBottomMargin(int bottomMargin) {
        this.bottomMargin = bottomMargin;
        updateBorder();
    }

    public void setRightMargin(int rightMargin) {
        this.rightMargin = rightMargin;
        updateBorder();
    }

    private void updateBorder() {
        setBorder(BorderFactory.createEmptyBorder(topMargin, leftMargin, bottomMargin, rightMargin));
        revalidate(); // Revalidate the layout to reflect the changes
    }


    // Method to set the internal component
    public void setContent(Component component) {
        removeAll(); // Remove any existing component
        add(component); // Add the new component
        revalidate(); // Revalidate the layout to reflect the changes
    }

    public static LiteMargin of(JComponent component, int topMargin, int leftMargin, int bottomMargin, int rightMargin) {
        LiteMargin liteMargin = new LiteMargin(topMargin, leftMargin, bottomMargin, rightMargin);
        liteMargin.setContent(component);
        return liteMargin;
    }
    public static LiteMargin ofInside(JComponent component, int topMargin, int leftMargin, int bottomMargin, int rightMargin) {
        LiteMargin liteMargin = new LiteMargin(topMargin, leftMargin, bottomMargin, rightMargin);
        component.add(liteMargin);

        return liteMargin;
    }

    public static LiteMargin ofInside(JComponent component, int margin) {
        LiteMargin liteMargin = new LiteMargin(margin, margin, margin, margin);
        component.add(liteMargin);

        return liteMargin;
    }

    public static LiteMargin of(int margin) {
        return new LiteMargin(margin, margin, margin, margin);
    }

    public static LiteMargin of(int margin, LiteComponent... components) {
        LiteMargin liteMargin = new LiteMargin(margin, margin, margin, margin);
        for (LiteComponent component : components) {
            liteMargin.add(component);
        }
        return liteMargin;
    }

}