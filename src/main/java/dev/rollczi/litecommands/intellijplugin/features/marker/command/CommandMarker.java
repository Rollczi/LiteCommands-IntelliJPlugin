package dev.rollczi.litecommands.intellijplugin.features.marker.command;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.ui.components.JBBox;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.intellijplugin.api.CommandNode;
import dev.rollczi.litecommands.intellijplugin.api.psijava.PsiJavaCommandNode;
import dev.rollczi.litecommands.intellijplugin.features.icon.LiteIcon;
import dev.rollczi.litecommands.intellijplugin.features.marker.LiteLineMarkerDescriptor;
import dev.rollczi.litecommands.intellijplugin.features.marker.LiteLineMarkerInfo;
import dev.rollczi.litecommands.intellijplugin.old.annotation.AnnotationFactory;
import dev.rollczi.litecommands.intellijplugin.old.annotation.AnnotationHolder;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import org.jetbrains.annotations.NotNull;

public class CommandMarker extends LiteLineMarkerDescriptor {

    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        if (!(element instanceof PsiClass psiClass)) {
            return null;
        }

        PsiAnnotation annotation = psiClass.getAnnotation(Command.class.getName());

        if (annotation == null) {
            return null;
        }

        CommandNode command = new PsiJavaCommandNode(psiClass);

        JBBox box = new JBBox(BoxLayout.Y_AXIS);
        box.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        box.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        box.add(new CommandComponent(command));
        box.add(new PermissionsComponent(command));
        box.add(new ExecutorsComponent(command));

        return LiteLineMarkerInfo.builder(psiClass.getNameIdentifier())
            .name("Command base viewer")
            .viewIcon(LiteIcon.BETA)
            .lineIcon(LiteIcon.COMMAND_MARK_LINE)
            .component(box)
            .build();
    }

}
