package dev.rollczi.litecommands.intellijplugin.inspection.parameter;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.literal.Literal;
import dev.rollczi.litecommands.intellijplugin.inspection.LiteInspection;
import dev.rollczi.litecommands.intellijplugin.quickfix.ChangeParameterTypeQuickFix;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class LiteralAnnotationInspection extends LiteInspection {

    private static final Set<String> SUPPORTED_TYPES = Set.of(
        String.class.getName(),
        Void.class.getName()
    );

    private static final String DISPLAY_NAME = "@Literal support only String type";
    private static final String PROBLEM_DESCRIPTION = "%s type is not supported by @Literal! Use a String instead.";

    public LiteralAnnotationInspection() {
        super(DISPLAY_NAME);
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new MethodVisitor(holder);
    }

    private static class MethodVisitor extends JavaElementVisitor {

        private final ProblemsHolder holder;

        public MethodVisitor(ProblemsHolder holder) {
            this.holder = holder;
        }

        @Override
        public void visitMethod(@NotNull PsiMethod method) {
            PsiAnnotation annotation = method.getAnnotation(Execute.class.getName());

            if (annotation == null) {
                return;
            }

            PsiParameterList parameterList = method.getParameterList();

            for (PsiParameter parameter : parameterList.getParameters()) {
                PsiAnnotation[] annotations = parameter.getAnnotations();

                for (PsiAnnotation psiAnnotation : annotations) {
                    if (Literal.class.getName().equals(psiAnnotation.getQualifiedName())) {
                        this.checkLiteralAnnotation(parameter);
                    }
                }
            }
        }

        private void checkLiteralAnnotation(PsiParameter parameter) {
            if (SUPPORTED_TYPES.contains(parameter.getType().getCanonicalText())) {
                return;
            }

            String problemMessage = String.format(PROBLEM_DESCRIPTION, parameter.getType().getCanonicalText());

            holder.registerProblem(parameter, problemMessage, ProblemHighlightType.GENERIC_ERROR, fixes(parameter));
        }

        private LocalQuickFix[] fixes(PsiParameter parameter) {
            return new LocalQuickFix[] { new ChangeParameterTypeQuickFix(type -> "Replace to String", parameter, old -> "String") };
        }
    }

}
