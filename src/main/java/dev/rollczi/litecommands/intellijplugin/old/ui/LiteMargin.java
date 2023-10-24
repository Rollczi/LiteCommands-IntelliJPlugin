package dev.rollczi.litecommands.intellijplugin.old.ui;

import org.intellij.lang.annotations.MagicConstant;

import java.awt.*;
import javax.swing.*;

public class LiteMargin extends Box implements LiteComponent {

    public static final int NONE = 0;
    public static final int TINY = 3;
    public static final int SMALLER = 5;
    public static final int SMALL = 10;
    public static final int NORMAL = 15;
    public static final int MEDIUM = 20;
    public static final int BIG = 25;
    public static final int LARGE = 30;

    public LiteMargin(int margin) {
        this(margin, margin, margin, margin);
    }

    public LiteMargin(int topBottomMargin, int leftRightMargin) {
        this(topBottomMargin, leftRightMargin, topBottomMargin, leftRightMargin);
    }

    public LiteMargin(int topMargin, int leftMargin, int bottomMargin, int rightMargin) {
        super(BoxLayout.Y_AXIS);
        this.setAlignmentX(Component.LEFT_ALIGNMENT);

        this.setOpaque(false);
        this.setBorder(BorderFactory.createEmptyBorder(topMargin, leftMargin, bottomMargin, rightMargin));
    }

    public static LiteMargin of(@MagicConstant(valuesFromClass = LiteMargin.class) int margin, LiteComponent... components) {
        LiteMargin liteMargin = new LiteMargin(margin, margin, margin, margin);
        for (LiteComponent component : components) {
            liteMargin.with(component);
        }
        return liteMargin;
    }

}