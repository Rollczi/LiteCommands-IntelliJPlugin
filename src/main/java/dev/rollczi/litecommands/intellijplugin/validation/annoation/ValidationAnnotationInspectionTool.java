package dev.rollczi.litecommands.intellijplugin.validation.annoation;

import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.JavaTokenType;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiJavaToken;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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

                    List<AttributeValue> values = ValidationAnnotationInspectionTool.this.getValues(attribute);

                    for (AttributeValue value : values) {
                        if (!validator.validate(value.text())) {
                            holder.registerProblem(
                                    value.element(),
                                    String.format("Attribute '%s' has invalid value <code>%s</code>", validator.name(), value.text()),
                                    new ReplaceWithValidValueQuickFix(annotation, validator, value)
                            );
                        }
                    }
                }
            }
        };
    }

    private static final Set<IElementType> CHECKED_TOKENS = Set.of(
            JavaTokenType.STRING_LITERAL,
            JavaTokenType.CHARACTER_LITERAL,
            JavaTokenType.INTEGER_LITERAL,
            JavaTokenType.LONG_LITERAL,
            JavaTokenType.FLOAT_LITERAL,
            JavaTokenType.DOUBLE_LITERAL,
            JavaTokenType.TRUE_KEYWORD,
            JavaTokenType.FALSE_KEYWORD,
            JavaTokenType.NULL_KEYWORD,
            JavaTokenType.MINUS,
            JavaTokenType.PLUS
    );

    private List<AttributeValue> getValues(PsiElement element) {
        List<AttributeValue> values = new ArrayList<>();

        for (PsiElement child : element.getChildren()) {
            if (child instanceof PsiJavaToken) {
                PsiJavaToken token = (PsiJavaToken) child;
                IElementType tokenType = token.getTokenType();
                String text = token.getText();

                if (!CHECKED_TOKENS.contains(tokenType)) {
                    continue;
                }

                if (tokenType == JavaTokenType.STRING_LITERAL || tokenType == JavaTokenType.CHARACTER_LITERAL) {
                    values.add(new AttributeValue(text.substring(1, text.length() - 1), text, token));
                    continue;
                }

                if (tokenType == JavaTokenType.MINUS || tokenType == JavaTokenType.PLUS) {
                    return Collections.singletonList(new AttributeValue(element.getText(), element.getText(), element));
                }
            }

            if (child instanceof PsiLiteralExpression) {
                values.addAll(this.getValues(child));
            }
        }

        if (values.isEmpty()) {
            values.add(new AttributeValue(element.getText(), element.getText(), element));
        }

        return values;
    }

}
