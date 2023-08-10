package dev.rollczi.litecommands.intellijplugin.command;

import java.awt.*;
import javax.swing.*;

public class LiteBox extends Box implements LiteComponent {

    private final int arcWidth;
    private final int arcHeight;
    private final Color color;

    public LiteBox() {
        this(40, 40, LiteColors.GRAY);
    }

    public LiteBox(Color color) {
        this(40, 40, color);
    }

    public LiteBox(int arcWidth, int arcHeight, Color color) {
        super(BoxLayout.Y_AXIS);
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        this.color = color;
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
        this.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // To avoid painting the default border of the container
    }

    public LiteMargin margined() {
        return LiteMargin.of(10, this);
    }

    public static LiteBox of(Component... components) {
        LiteBox box = new LiteBox();

        for (Component component : components) {
            box.add(component);
        }

        return box;
    }

    public static LiteBox invisible(Component... components) {
        LiteBox box = new LiteBox(LiteColors.NONE);

        for (Component component : components) {
            box.add(component);
        }

        return box;
    }

}
