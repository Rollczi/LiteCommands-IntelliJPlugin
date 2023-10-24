package dev.rollczi.litecommands.intellijplugin.old.command;

import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.PsiElement;
import javax.swing.*;

public class MyPsiElementLinkProvider {

    public static void showLink(PsiElement targetPsiElement) {
        NavigationGutterIconBuilder<PsiElement> builder =
            NavigationGutterIconBuilder.create(getIcon(targetPsiElement))
                .setAlignment(GutterIconRenderer.Alignment.CENTER)
                .setTarget(targetPsiElement)
                .setTooltipText("Navigate to reference");

//        builder.install(new AnnotationHolderImpl(), targetPsiElement);
    }

    private static Icon getIcon(PsiElement psiElement) {
        // Return the desired icon for the gutter icon
        // You can use IconLoader.getIcon() to load an icon from resources
        return null;
    }

}
