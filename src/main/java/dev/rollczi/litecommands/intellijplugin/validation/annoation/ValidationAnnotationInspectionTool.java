package dev.rollczi.litecommands.intellijplugin.validation.annoation;

import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public abstract class ValidationAnnotationInspectionTool extends AbstractBaseJavaLocalInspectionTool {

    private static final String DISPLAY_NAME = "Annotation @%s has invalid value";

    private final AnnotationValidator annotationValidator;

    protected ValidationAnnotationInspectionTool(AnnotationValidator annotationValidator) {
        this.annotationValidator = annotationValidator;
    }

    @Override
    public @NotNull String getDisplayName() {
        return String.format(DISPLAY_NAME, this.annotationValidator.name());
    }

    @Override
    public @NotNull String getGroupDisplayName() {
        return "LiteCommands";
    }

    @Override
    public @NotNull String getShortName() {
        return "Validation" + this.annotationValidator.name() + "Annotation";
    }

    @Override
    public boolean isEnabledByDefault() {
        return true;
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {
            @Override
            public void visitAnnotation(PsiAnnotation annotation) {
                if (!ValidationAnnotationInspectionTool.this.annotationValidator.qualifiedName().equals(annotation.getQualifiedName())) {
                    return;
                }

                for (AttributeValidator validator : ValidationAnnotationInspectionTool.this.annotationValidator.getAttributesValidators()) {
                    PsiAnnotationMemberValue attribute = annotation.findDeclaredAttributeValue(validator.name());

                    if (attribute == null) {
                        continue;
                    }

                    String value = attribute.getText();

                    if (!validator.validate(value)) {
                        holder.registerProblem(attribute, String.format("%s attribute %s has invalid value '%s'", ValidationAnnotationInspectionTool.this.annotationValidator.annotation(), validator.name(), value));
                    }
                }
            }
        };
    }

}
