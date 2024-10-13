package dev.rollczi.litecommands.intellijplugin.marker.command;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.ui.components.JBBox;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.intellijplugin.api.CommandNode;
import dev.rollczi.litecommands.intellijplugin.api.psijava.PsiJavaCommandNode;
import dev.rollczi.litecommands.intellijplugin.icon.LiteIcons;
import dev.rollczi.litecommands.intellijplugin.marker.LiteLineMarkerDescriptor;
import dev.rollczi.litecommands.intellijplugin.marker.LiteLineMarkerInfo;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UElementKt;
import org.jetbrains.uast.UastUtils;

public class CommandMarker extends LiteLineMarkerDescriptor {

    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        Pair<CommandNode, PsiElement> pair = findCommandNode(element);

        if (pair == null) {
            return null;
        }

        CommandNode commandNode = pair.getFirst();

        JBBox box = new JBBox(BoxLayout.Y_AXIS);
        box.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        box.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        box.add(new CommandComponent(commandNode));
        box.add(new PermissionsComponent(commandNode));
        box.add(new ExecutorsComponent(commandNode));

        return LiteLineMarkerInfo.builder(pair.getSecond())
            .name("Command base viewer")
            .viewIcon(LiteIcons.BETA)
            .lineIcon(LiteIcons.COMMAND_MARK_LINE)
            .component(box)
            .build();
    }

    @Nullable
    private static Pair<CommandNode, PsiElement> findCommandNode(@NotNull PsiElement element) {
        UElement uElement = UastUtils.getUParentForIdentifier(element);

        if (!(uElement instanceof UClass uClass)) {
            return null;
        }

        PsiElement identifier = UElementKt.getSourcePsiElement(uClass.getUastAnchor());

        if (identifier == null) {
            return null;
        }

        PsiClass javaPsi = uClass.getJavaPsi();
        PsiAnnotation commandAnnotation = javaPsi.getAnnotation(Command.class.getName());

        if (commandAnnotation == null) {
            return null;
        }

        return new Pair<>(new PsiJavaCommandNode(javaPsi), identifier);
    }

}
