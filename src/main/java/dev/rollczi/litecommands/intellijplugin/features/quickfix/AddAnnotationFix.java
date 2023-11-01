package dev.rollczi.litecommands.intellijplugin.features.quickfix;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import dev.rollczi.litecommands.intellijplugin.util.PsiImportUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;


public class AddAnnotationFix implements LocalQuickFix {

    private static final String DISPLAY_NAME = "Add @%s annotation";

    private final PsiElement element;
    private final String annotationName;
    private final String annotationQualifiedName;

    public AddAnnotationFix(PsiElement element, String annotationName, String annotationQualifiedName) {
        this.element = element;
        this.annotationName = annotationName;
        this.annotationQualifiedName = annotationQualifiedName;
    }

    public AddAnnotationFix(Annotation annotation, PsiElement element) {
        this.element = element;
        Class<? extends Annotation> type = annotation.annotationType();

        this.annotationName = type.getSimpleName();
        this.annotationQualifiedName = type.getCanonicalName();
    }

    @Override
    public @IntentionFamilyName @NotNull String getFamilyName() {
        return String.format(DISPLAY_NAME, this.annotationName);
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);
        PsiAnnotation newAnnotation = elementFactory.createAnnotationFromText(
                "@" + this.annotationName,
                this.element
        );

        this.element.addBefore(newAnnotation, this.element.getFirstChild());

        PsiImportUtil.importClass(this.element, this.annotationQualifiedName);
    }
}
