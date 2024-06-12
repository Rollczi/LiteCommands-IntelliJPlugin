package dev.rollczi.litecommands.intellijplugin.inlay;

import com.intellij.codeInsight.hints.InlayHintsCollector;
import com.intellij.codeInsight.hints.InlayHintsSink;
import static com.intellij.codeInsight.hints.InlayPresentationFactory.*;
import com.intellij.codeInsight.hints.presentation.InlayPresentation;
import com.intellij.codeInsight.hints.presentation.InsetPresentation;
import com.intellij.codeInsight.hints.presentation.PresentationFactory;
import com.intellij.codeInsight.hints.presentation.ScaleAwarePresentationFactory;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationParameterList;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import dev.rollczi.litecommands.intellijplugin.util.LiteAnnotationChecks;
import dev.rollczi.litecommands.intellijplugin.util.LiteTypeChecks;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
class LiteInlayHintsCollector implements InlayHintsCollector {

    public static final boolean RELATES_TO_PRECEDING_TEXT = true;
    public static final boolean PLACE_AT_THE_END_OF_LINE = false;

    public static final int LEFT_MARGIN = 2;
    public static final int RIGHT_MARGIN = 2;

    public static final Padding PADDING = new Padding(0, 0, 1, 1);
    public static final RoundedCorners ROUNDED_CORNERS = new RoundedCorners(8, 8);
    public static final float BACKGROUND_ALPHA = 0.55f;

    @Override
    public boolean collect(@NotNull PsiElement psiElement, @NotNull Editor editor, @NotNull InlayHintsSink inlayHintsSink) {
        if (!(psiElement instanceof PsiClass psiClass)) {
            return true;
        }

        resolveClass(editor, inlayHintsSink, psiClass);
        return false;
    }

    private void resolveClass(@NotNull Editor editor, @NotNull InlayHintsSink inlayHintsSink, PsiClass psiClass) {
        if (!LiteAnnotationChecks.isCommand(psiClass)) {
            for (PsiClass innerClass : psiClass.getInnerClasses()) {
                resolveClass(editor, inlayHintsSink, innerClass);
            }

            return;
        }

        for (PsiMethod method : psiClass.getMethods()) {
            if (!LiteAnnotationChecks.isCommandExecutor(method)) {
                continue;
            }

            resolveMethod(editor, inlayHintsSink, method);
        }
    }

    private void resolveMethod(@NotNull Editor editor, @NotNull InlayHintsSink inlayHintsSink, PsiMethod method) {
        for (PsiParameter parameter : method.getParameterList().getParameters()) {
            for (PsiAnnotation annotation : parameter.getAnnotations()) {
                if (!LiteTypeChecks.isArgumentAnnotation(annotation)) {
                    continue;
                }

                int textOffset = getOffset(annotation);
                InlayPresentation inlayPresentation = createInsetPresentation(editor, parameter.getType(), parameter);

                inlayHintsSink.addInlineElement(textOffset, RELATES_TO_PRECEDING_TEXT, inlayPresentation, PLACE_AT_THE_END_OF_LINE);
            }
        }
    }

    private int getOffset(@NotNull PsiAnnotation annotation) {
        PsiAnnotationParameterList parameterList = annotation.getParameterList();
        @NotNull PsiElement[] children = parameterList.getChildren();

        if (children.length != 0) {
            PsiElement child = children[0];
            return child.getTextOffset() + child.getTextLength();
        }

        return parameterList.getTextOffset();
    }

    @NotNull
    private InlayPresentation createInsetPresentation(@NotNull Editor editor, PsiType psiType, PsiParameter parameter) {
        TextAttributes attributes = editor.getColorsScheme().getAttributes(DefaultLanguageHighlighterColors.INLINE_PARAMETER_HINT);
        InlayHintsIconPresentation presentation = new InlayHintsIconPresentation(AllIcons.General.ArrowDown, psiType, parameter, editor);
        ScaleAwarePresentationFactory factory = new ScaleAwarePresentationFactory(editor, new PresentationFactory(editor));

        InlayPresentation container = factory.container(
            presentation,
            PADDING,
            ROUNDED_CORNERS,
            attributes.getBackgroundColor(),
            BACKGROUND_ALPHA
        );

        return new InsetPresentation(
            factory.lineCentered(container),
            LEFT_MARGIN, RIGHT_MARGIN, 0, 0
        );
    }

}
