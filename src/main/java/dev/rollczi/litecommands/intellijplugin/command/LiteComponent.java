package dev.rollczi.litecommands.intellijplugin.command;

import java.awt.*;
import java.util.function.Supplier;
import javax.swing.*;

public interface LiteComponent {

    default LiteComponent with(Supplier<LiteComponent> provider) {
        if (!(this instanceof Container)) {
            return this;
        }

        LiteComponent liteComponent = provider.get();

        this.asComponent().add((Component) liteComponent);
        return this;
    }

    default LiteComponent with(LiteComponent component) {
        return this.with(() -> component);
    }

    default Component add(LiteComponent component) {
        if (!(component instanceof Component)) {
            throw new UnsupportedOperationException();
        }

        return this.asComponent().add((Component) component);
    }

    default JComponent asComponent() {
        if (!(this instanceof JComponent)) {
            throw new UnsupportedOperationException();
        }

        return (JComponent) this;
    }

}
