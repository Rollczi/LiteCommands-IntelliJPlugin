package dev.rollczi.litecommands.intellijplugin.ui;

import com.intellij.ui.Gray;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.JBUI.Fonts;
import dev.rollczi.litecommands.shared.Preconditions;
import org.intellij.lang.annotations.MagicConstant;

import java.awt.*;
import javax.swing.*;

public class LiteBadge extends JBLabel implements LiteComponent {

    public static final int ICON_WIDTH_MARGIN = 5;
    private final String badgeText;
    private Icon icon;
    private final int margin;

    private Color badgeColor;

    protected LiteBadge(String badgeText, Color badgeColor) {
        this(badgeText, badgeColor, null, LiteMargin.SMALL);
    }

    protected LiteBadge(String badgeText, Color badgeColor, Icon icon) {
        this(badgeText, badgeColor, icon, LiteMargin.NORMAL);
    }

    protected LiteBadge(String badgeText, Color badgeColor, Icon icon, int margin) {
        Preconditions.notNull(badgeText, "badgeText");
        Preconditions.notNull(badgeColor, "badgeColor");

        this.badgeText = badgeText;
        this.badgeColor = badgeColor;
        this.icon = icon;
        this.setFont(Fonts.label().deriveFont(Font.BOLD));
        this.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));
        this.margin = margin;
    }

    public void setBadgeColor(Color badgeColor) {
        this.badgeColor = badgeColor;
        repaint();
    }

    @Override
    public void setIcon(Icon icon) {
        this.icon = icon;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Dimension size = getSize();
        int width = size.width;
        int height = size.height;

        // Draw the badge background
        g2d.setColor(badgeColor);
        g2d.fillRoundRect(0, 0, width, height, LiteArc.SMALL, LiteArc.SMALL);

        int nextX = margin;

        // Draw the icon if available
        if (icon != null) {
            int iconWidth = icon.getIconWidth();
            int iconHeight = icon.getIconHeight();
            int iconX = nextX;
            int iconY = (height - iconHeight) / 2;
            icon.paintIcon(this, g2d, iconX, iconY);

            nextX = iconX + ICON_WIDTH_MARGIN + iconWidth;
        }

        FontMetrics fontMetrics = g2d.getFontMetrics();
        int textHeight = fontMetrics.getHeight();
        int x = nextX;
        int y = (height - textHeight) / 2 + fontMetrics.getAscent();

        // Draw the badge text in the center
        g2d.setColor(Gray._190);
        g2d.drawString(badgeText, x, y);
    }

    @Override
    public Dimension getPreferredSize() {
        FontMetrics fontMetrics = getFontMetrics(getFont());
        int textWidth = fontMetrics.stringWidth(badgeText);
        int textHeight = fontMetrics.getHeight();
        int width = textWidth + margin + margin;
        int height = textHeight + margin;

        // Consider the icon size for preferred size calculation
        if (icon != null) {
            width += ICON_WIDTH_MARGIN + icon.getIconWidth();
        }

        if (badgeText.isEmpty()) {
            width -= ICON_WIDTH_MARGIN;
        }

        return new Dimension(width, height);
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    public LiteMargin wrapMargin() {
        return LiteBox.invisible(this).margined(LiteMargin.TINY);
    }

    public static LiteComponent of(Icon icon, String badgeText) {
        return of(icon, badgeText, LiteColors.NONE);
    }

    public static LiteComponent of(Icon icon, String badgeText, Color badgeColor) {
        return of(icon, badgeText, badgeColor, LiteMargin.NORMAL);
    }

    public static LiteComponent of(Icon icon, String badgeText, Color badgeColor, @MagicConstant(valuesFromClass = LiteMargin.class) int margin) {
        return new LiteBox(LiteColors.NONE, LiteArc.SMALL)
                .with(new LiteBadge(badgeText, badgeColor, icon, margin));
    }

    public static LiteBadge raw(Icon icon, String badgeText, Color badgeColor) {
        return new LiteBadge(badgeText, badgeColor, icon);
    }

    public static LiteBadge raw(Icon icon, String badgeText, Color badgeColor, @MagicConstant(valuesFromClass = LiteMargin.class) int margin) {
        return new LiteBadge(badgeText, badgeColor, icon, margin);
    }


}