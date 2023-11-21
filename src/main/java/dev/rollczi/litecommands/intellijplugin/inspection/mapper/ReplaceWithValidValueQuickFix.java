package dev.rollczi.litecommands.intellijplugin.inspection.mapper;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

class ReplaceWithValidValueQuickFix implements LocalQuickFix {

    private final PsiAnnotation patternAnnotation;
    private final AnnotationMapperAttributeSchema validator;
    private final AttributeValue value;

    public ReplaceWithValidValueQuickFix(PsiAnnotation patternAnnotation, AnnotationMapperAttributeSchema validator, AttributeValue value) {
        this.patternAnnotation = patternAnnotation;
        this.validator = validator;
        this.value = value;
    }

    @Override
    public @IntentionFamilyName @NotNull String getFamilyName() {
        String quickFix = this.validator.quickFix(this.value.rawText());

        if (quickFix.startsWith("\"") && quickFix.endsWith("\"")) {
            quickFix = quickFix.substring(1, quickFix.length() - 1);
        }

        return "Replace with '" + quickFix + "'";
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
        String quickFix = this.validator.quickFix(this.value.rawText());
        PsiElement old = this.value.element();
        String annotationText = this.patternAnnotation.getText();

        int start = old.getTextRange().getStartOffset() - this.patternAnnotation.getTextRange().getStartOffset();
        int end = old.getTextRange().getEndOffset() - this.patternAnnotation.getTextRange().getStartOffset();

        String newAnnotationText = annotationText.substring(0, start) + quickFix + annotationText.substring(end);

        this.patternAnnotation.replace(JavaPsiFacade.getElementFactory(project).createAnnotationFromText(
                newAnnotationText,
                this.patternAnnotation
        ));
    }

}
