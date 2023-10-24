package dev.rollczi.litecommands.intellijplugin.marker.command;

import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.permission.Permission;
import dev.rollczi.litecommands.annotations.permission.Permissions;
import dev.rollczi.litecommands.intellijplugin.icon.LiteIcon;
import dev.rollczi.litecommands.intellijplugin.old.annotation.AnnotationFactory;
import dev.rollczi.litecommands.intellijplugin.old.annotation.AnnotationHolder;
import dev.rollczi.litecommands.intellijplugin.old.ui.*;
import panda.std.Pair;


import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class PermissionsComponent extends JPanel {

    PermissionsComponent(AnnotationHolder<Command> command, PsiClass psiClass) {
        super(new BorderLayout());

        List<Pair<String, PsiAnnotation>> permissions = getPermissions(psiClass);

        if (permissions.isEmpty()) {
            this.setVisible(false);
            return;
        }

        this.add(this.title(), BorderLayout.NORTH);
        this.add(this.content(command, permissions), BorderLayout.CENTER);
    }

    private JComponent title() {
        return new LiteTitledSeparator(LiteIcon.PERMISSIONS, "Permissions");
    }

    private JComponent content(AnnotationHolder<Command> command, List<Pair<String, PsiAnnotation>> permissions) {
        JPanel content = new JPanel(new BorderLayout());
        content.add(permissionsList(permissions), BorderLayout.WEST);
        content.add(new EditButton(command.asAnnotation()), BorderLayout.EAST);

        return content;
    }

    private JComponent permissionsList(List<Pair<String, PsiAnnotation>> permissions) {
        LiteBox box = new LiteBox(LiteColors.NONE);

        for (Pair<String, PsiAnnotation> permission : permissions) {
            box.with(permissionBadge(permission.getFirst(), permission.getSecond()));
        }

        return box;
    }

    private LiteComponent permissionBadge(String name, PsiAnnotation annotation) {
        LiteActionBadge badge = new LiteActionBadge(
            name,
            LiteColors.GRAY,
            LiteColors.GRAY_LIGHT,
            LiteIcon.PERMISSION_ELEMENT,
            LiteMargin.SMALL
        );

        badge.addListener(() -> {
            if (annotation instanceof Navigatable navigatable) {
                navigatable.navigate(true);
            }
        });

        return LiteBox.invisible(badge).margined(LiteMargin.TINY);
    }

    private List<Pair<String, PsiAnnotation>> getPermissions(PsiClass psiClass) {
        List<AnnotationHolder<Permission>> packed = AnnotationFactory.from(Permission.class, psiClass);

        for (AnnotationHolder<Permissions> holder : AnnotationFactory.from(Permissions.class, psiClass)) {
            packed.addAll(Arrays.stream(holder.asAnnotation().value()).map(permission -> new AnnotationHolder<>(holder.asPsi(), permission)).toList());
        }

        return packed.stream()
            .flatMap(holder -> Arrays.stream(holder.asAnnotation().value()).map(s -> Pair.of(s, holder.asPsi())))
            .collect(Collectors.toList());
    }

}
