package dev.rollczi.litecommands.intellijplugin.command;

import com.intellij.ui.Gray;
import com.intellij.util.ui.JBUI.Fonts;
import java.awt.*;
import javax.swing.*;

public class LiteBadge extends JComponent implements LiteComponent {

    public static final int MARGIN = 10;

    private String badgeText;
    private Color badgeColor;
    private Icon icon;

    private LiteBadge(String badgeText, Color badgeColor) {
        this.badgeText = badgeText;
        this.badgeColor = badgeColor;
        this.setFont(Fonts.label().deriveFont(Font.BOLD));
        this.setBorder(BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN));
    }

    private LiteBadge(String badgeText, Color badgeColor, Icon icon) {
        this.badgeText = badgeText;
        this.badgeColor = badgeColor;
        this.icon = icon;
        this.setFont(Fonts.label().deriveFont(Font.BOLD));
        this.setBorder(BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN));
    }

    public void setBadgeText(String badgeText) {
        this.badgeText = badgeText;
        repaint(); // Refresh the component when the text changes
    }

    public void setBadgeColor(Color badgeColor) {
        this.badgeColor = badgeColor;
        repaint(); // Refresh the component when the color changes
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
        repaint(); // Refresh the component when the icon changes
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
        g2d.fillRoundRect(0, 0, width, height, 15, 15);

        int nextX = 0;

        // Draw the icon if available
        if (icon != null) {
            int iconWidth = icon.getIconWidth();
            int iconHeight = icon.getIconHeight();
            int iconX = MARGIN;
            int iconY = (height - iconHeight) / 2;
            icon.paintIcon(this, g2d, iconX, iconY);

            nextX = iconX + iconWidth + MARGIN;
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
        int width = textWidth + 25;
        int height = textHeight + 15;

        // Consider the icon size for preferred size calculation
        if (icon != null) {
            width += icon.getIconWidth() + MARGIN;
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

    public static LiteComponent margined(Icon icon, String badgeText) {
        return LiteMargin.of(10, new LiteBadge(badgeText, LiteColors.GRAY, icon));
    }

    public static LiteComponent margined(Icon icon, String badgeText, Color color) {
        return LiteMargin.of(10, new LiteBadge(badgeText, color, icon));
    }

    public static LiteBadge of(Icon icon, String badgeText) {
        return new LiteBadge(badgeText, LiteColors.GRAY, icon);
    }

    public static LiteBadge of(String badgeText, Color badgeColor) {
        return new LiteBadge(badgeText, badgeColor);
    }

    public static LiteBadge of(String badgeText, Color badgeColor, Icon icon) {
        return new LiteBadge(badgeText, badgeColor, icon);
    }

    public static LiteMargin invisible(String badgeText, Icon icon) {
        return LiteMargin.of(new LiteBadge(badgeText, LiteColors.NONE, icon), 4, 0, 4, 0);
    }

}