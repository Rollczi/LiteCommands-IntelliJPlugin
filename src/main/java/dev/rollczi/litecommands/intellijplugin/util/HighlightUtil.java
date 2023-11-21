package dev.rollczi.litecommands.intellijplugin.util;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.util.concurrency.AppExecutorUtil;
import dev.rollczi.litecommands.intellijplugin.ui.LiteColors;
import java.awt.Font;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class HighlightUtil {


    public static final int HIGHLIGHT_REMOVAL_DELAY = 1000;

    private HighlightUtil() {
    }

    public static void highlight(PsiElement psiElement) {
        FileEditorManager manager = FileEditorManager.getInstance(psiElement.getProject());
        FileEditor[] fileEditors = manager.openFile(psiElement.getContainingFile().getVirtualFile(), true);

        for (FileEditor fileEditor : fileEditors) {
            if (fileEditor instanceof TextEditor textEditor) {
                Editor editor = textEditor.getEditor();
                MarkupModel markupModel = editor.getMarkupModel();
                TextRange range = psiElement.getTextRange();

                RangeHighlighter rangeHighlighter = markupModel.addRangeHighlighter(
                    range.getStartOffset(),
                    range.getEndOffset(),
                    Integer.MAX_VALUE,
                    new TextAttributes(null, LiteColors.HIGH_LIGHT, null, null, Font.PLAIN),
                    HighlighterTargetArea.EXACT_RANGE
                );

                ScheduledExecutorService executorService = AppExecutorUtil.getAppScheduledExecutorService();
                executorService.schedule(() -> markupModel.removeHighlighter(rangeHighlighter), HIGHLIGHT_REMOVAL_DELAY, TimeUnit.MILLISECONDS);
            }
        }
    }

}
