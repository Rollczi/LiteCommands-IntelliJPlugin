package dev.rollczi.litecommands.intellijplugin.ui;

import com.intellij.openapi.util.NlsContexts;
import com.intellij.ui.TitledSeparator;
import com.intellij.util.ui.JBFont;

import javax.swing.*;

public class LiteTitledSeparator extends TitledSeparator {

    public LiteTitledSeparator(Icon icon, @NlsContexts.Separator String text) {
        super(text);
        getLabel().setIcon(icon);
        getLabel().setIconTextGap(10);
        setTitleFont(JBFont.regular().asBold());
    }


}
