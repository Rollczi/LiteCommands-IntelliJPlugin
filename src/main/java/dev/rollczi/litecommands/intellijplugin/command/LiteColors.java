package dev.rollczi.litecommands.intellijplugin.command;

import com.intellij.ui.JBColor;
import java.awt.*;

public final class LiteColors {

    private LiteColors() {
    }


    public static final Color NONE = new JBColor(new Color(0, 0, 0, 0), new Color(0, 0, 0, 0));
    public static final Color GRAY = new JBColor(new Color(0, 0, 0, 45), new Color(0, 0, 0, 20));
    public static final Color RED = new JBColor(new Color(140, 0, 0, 20), new Color(140, 0, 0, 20));

}
