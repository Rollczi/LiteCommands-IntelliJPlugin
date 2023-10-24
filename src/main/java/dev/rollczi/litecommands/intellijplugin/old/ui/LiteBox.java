package dev.rollczi.litecommands.intellijplugin.old.ui;

import com.intellij.ui.JBColor;
import org.intellij.lang.annotations.MagicConstant;

import java.awt.*;
import javax.swing.*;

public class LiteBox extends Box implements LiteComponent {

    private final int arcWidth;
    private final int arcHeight;
    private final Color color;

    public LiteBox() {
        this(LiteColors.NONE, LiteArc.SMALL, LiteArc.SMALL);
    }

    public LiteBox(Color color) {
        this(color, LiteArc.SMALL, LiteArc.SMALL);
    }

    public LiteBox(Color color, @MagicConstant(valuesFromClass = LiteArc.class) int arc) {
        this(color, arc, arc);
    }

    public LiteBox(Color color, @MagicConstant(valuesFromClass = LiteArc.class) int arcHeight, @MagicConstant(valuesFromClass = LiteArc.class) int arcWidth) {
        super(BoxLayout.Y_AXIS);
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        this.color = color;

        this.setOpaque(false);
        this.setBackground(LiteColors.NONE);
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

    public LiteMargin margined(@MagicConstant(valuesFromClass = LiteMargin.class) int margin) {
        return LiteMargin.of(margin, this);
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
