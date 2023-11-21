package dev.rollczi.litecommands.intellijplugin.inspection.mapper;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiAnnotation;
import org.jetbrains.annotations.NotNull;

class AnnotationVisitor extends JavaElementVisitor {

    private final String displayName;
    private final AnnotationMapper mapper;
    private final ProblemsHolder holder;

    AnnotationVisitor(String displayName, AnnotationMapper mapper, ProblemsHolder holder) {
        this.displayName = displayName;
        this.mapper = mapper;
        this.holder = holder;
    }

    @Override
    public void visitAnnotation(@NotNull PsiAnnotation annotation) {
        super.visitAnnotation(annotation);

        String qualifiedName = annotation.getQualifiedName();

        if (qualifiedName == null) {
            return;
        }

        if (!qualifiedName.contains(".")) {
            if (this.mapper.legacyName().equals(qualifiedName)) {
                this.holder.registerProblem(annotation, this.displayName, new AnnotationReplaceQuickFix(this.mapper, this.holder));
            }

            return;
        }

        if (this.mapper.legacyQualified().equals(qualifiedName)) {
            this.holder.registerProblem(annotation, this.displayName, new AnnotationReplaceQuickFix(this.mapper, this.holder));
        }
    }

}
