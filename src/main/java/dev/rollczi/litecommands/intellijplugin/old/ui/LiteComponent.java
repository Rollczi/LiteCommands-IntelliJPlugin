package dev.rollczi.litecommands.intellijplugin.old.ui;

import java.awt.*;
import javax.swing.*;

public interface LiteComponent {

    default LiteComponent with(LiteComponent liteComponent) {
        return this.withComponent((Component) liteComponent);
    }

    default LiteComponent withComponent(Component liteComponent) {
        if (!(this instanceof Container)) {
            return this;
        }

        this.asComponent().add(liteComponent);
        return this;
    }

    default JComponent asComponent() {
        if (!(this instanceof JComponent)) {
            throw new UnsupportedOperationException();
        }

        return (JComponent) this;
    }

}
