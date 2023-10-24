package dev.rollczi.litecommands.intellijplugin.marker.command;

import com.intellij.pom.Navigatable;
import com.intellij.psi.*;
import com.intellij.ui.components.JBBox;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.intellijplugin.icon.LiteIcon;
import dev.rollczi.litecommands.intellijplugin.old.annotation.AnnotationHolder;
import dev.rollczi.litecommands.intellijplugin.old.ui.*;
import dev.rollczi.litecommands.intellijplugin.util.PsiAnnotationUtil;
import dev.rollczi.litecommands.intellijplugin.util.PsiValue;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;

class CommandComponent extends JPanel {

    CommandComponent(AnnotationHolder<Command> command) {
        super(new BorderLayout());
        this.add(title(), BorderLayout.NORTH);
        this.add(content(command), BorderLayout.CENTER);
    }

    private JComponent title() {
        return new LiteTitledSeparator(LiteIcon.COMMAND_STRUCTURE, "Command Structure");
    }

    private JComponent content(AnnotationHolder<Command> command) {
        JPanel content = new JPanel(new BorderLayout());
        content.add(list(command), BorderLayout.WEST);
        content.add(new EditButton(command.asAnnotation()), BorderLayout.EAST);

        return content;
    }

    private JComponent list(AnnotationHolder<Command> command) {
        JBBox box = new JBBox(BoxLayout.Y_AXIS);
        box.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        PsiAnnotation psi = command.asPsi();

        Command annotation = command.asAnnotation();
        String name = annotation.name();

        box.add(createBadge(name, false, psi.findDeclaredAttributeValue("name")));
        List<PsiValue<String>> aliases = PsiAnnotationUtil.stringArray(psi, "aliases");

        for (PsiValue<String> psiValue : aliases) {
            box.add(createBadge(psiValue.value(), true, psiValue.source()));
        }

        return box;
    }

    private JComponent createBadge(String name, boolean isAlias, PsiElement source) {
        LiteActionBadge badge = new LiteActionBadge(
            "/" + name + " ...",
            LiteColors.GRAY,
            LiteColors.GRAY_LIGHT,
            isAlias ? LiteIcon.COMMAND_ELEMENT_ALIAS : LiteIcon.COMMAND_ELEMENT,
            LiteMargin.SMALL
        );

        badge.addListener(() -> {
            if (source instanceof Navigatable navigatable) {
                navigatable.navigate(true);
            }
        });

        return LiteBox.invisible(badge).margined(LiteMargin.TINY);
    }

}
