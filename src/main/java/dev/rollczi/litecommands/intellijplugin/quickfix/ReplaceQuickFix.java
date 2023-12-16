package dev.rollczi.litecommands.intellijplugin.quickfix;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import java.util.concurrent.atomic.AtomicBoolean;
import org.jetbrains.annotations.NotNull;

public class ReplaceQuickFix implements LocalQuickFix {

    private final AtomicBoolean applied = new AtomicBoolean(false);

    private final String info;
    private final RangeMarker rangeMarker;
    private final CharSequence charSequence;

    public ReplaceQuickFix(String info, RangeMarker rangeMarker, CharSequence charSequence) {
        this.info = info;
        this.rangeMarker = rangeMarker;
        this.charSequence = charSequence;
    }

    @Override
    public String getFamilyName() {
        return this.info;
    }

    @Override
    public ReplaceQuickFix getFileModifierForPreview(@NotNull PsiFile target) {
        return new ReplaceQuickFix(info, rangeMarker, charSequence);
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
        if (!applied.compareAndSet(false, true)) return;

        PsiFile file = descriptor.getPsiElement().getContainingFile();
        if (file == null) {
            return;
        }

        Document doc = file.getViewProvider().getDocument();
        if (doc == null) {
            return;
        }

        if (rangeMarker.isValid()) {
            doc.replaceString(rangeMarker.getStartOffset(), rangeMarker.getEndOffset(), charSequence);
        }

        PsiDocumentManager.getInstance(project).commitDocument(doc);
    }

}