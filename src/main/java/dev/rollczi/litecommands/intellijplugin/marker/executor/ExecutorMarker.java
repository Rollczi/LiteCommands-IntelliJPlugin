package dev.rollczi.litecommands.intellijplugin.marker.executor;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.ui.components.JBBox;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.intellijplugin.api.ExecutorNode;
import dev.rollczi.litecommands.intellijplugin.api.psijava.PsiJavaCommandNode;
import dev.rollczi.litecommands.intellijplugin.api.psijava.PsiJavaExecutorNode;
import dev.rollczi.litecommands.intellijplugin.icon.LiteIcons;
import dev.rollczi.litecommands.intellijplugin.marker.LiteLineMarkerDescriptor;
import dev.rollczi.litecommands.intellijplugin.marker.LiteLineMarkerInfo;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UElementKt;
import org.jetbrains.uast.UMethod;
import org.jetbrains.uast.UastUtils;
import panda.std.Pair;

public class ExecutorMarker extends LiteLineMarkerDescriptor {

    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        Pair<ExecutorNode, PsiElement> pair = findCommandNode(element);

        if (pair == null) {
            return null;
        }

        ExecutorNode executorNode = pair.getFirst();

        JBBox box = new JBBox(BoxLayout.Y_AXIS);
        box.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        box.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        box.add(new ExecutorComponent(executorNode));

        return LiteLineMarkerInfo.builder(pair.getSecond())
            .name("Command executor viewer")
            .viewIcon(LiteIcons.BETA)
            .lineIcon(LiteIcons.EXECUTORS)
            .component(box)
            .build();
    }

    private static Pair<ExecutorNode, PsiElement> findCommandNode(@NotNull PsiElement element) {
        UElement uElement = UastUtils.getUParentForIdentifier(element);

        if (!(uElement instanceof UMethod uMethod)) {
            return null;
        }

        PsiElement identifier = UElementKt.getSourcePsiElement(uMethod.getUastAnchor());

        if (identifier == null) {
            return null;
        }

        PsiMethod psiMethod = uMethod.getJavaPsi();
        PsiAnnotation commandAnnotation = psiMethod.getAnnotation(Execute.class.getName());

        if (commandAnnotation == null) {
            return null;
        }

        PsiClass aClass = psiMethod.getContainingClass();

        if (aClass == null) {
            return null;
        }

        PsiAnnotation command = aClass.getAnnotation(Command.class.getName());

        if (command == null) {
            return null;
        }

        PsiJavaCommandNode commandNode = new PsiJavaCommandNode(aClass);
        PsiJavaExecutorNode executorNode = new PsiJavaExecutorNode(commandNode, psiMethod);

        return Pair.of(executorNode, identifier);

    }

}
