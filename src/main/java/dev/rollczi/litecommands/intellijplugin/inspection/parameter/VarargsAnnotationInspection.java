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
import dev.rollczi.litecommands.annotations.varargs.Varargs;
import dev.rollczi.litecommands.intellijplugin.inspection.LiteInspection;
import dev.rollczi.litecommands.intellijplugin.quickfix.ChangeParameterTypeQuickFix;
import dev.rollczi.litecommands.intellijplugin.quickfix.WrapParameterTypeQuickFix;
import dev.rollczi.litecommands.intellijplugin.util.LiteTypeChecks;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class VarargsAnnotationInspection extends LiteInspection {

    private static final String DISPLAY_NAME = "@Varargs support only collection types";
    private static final String PROBLEM_DESCRIPTION = "%s type is not supported by @Varargs! Use a collection instead.";

    public VarargsAnnotationInspection() {
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
                    if (Varargs.class.getName().equals(psiAnnotation.getQualifiedName())) {
                        this.checkVarargsAnnotation(parameter);
                    }
                }
            }
        }

        private void checkVarargsAnnotation(PsiParameter parameter) {
            if (LiteTypeChecks.isArrayWrapper(parameter.getType(), parameter.getProject())) {
                return;
            }

            String problemMessage = String.format(PROBLEM_DESCRIPTION, parameter.getType().getCanonicalText());

            holder.registerProblem(parameter, problemMessage, ProblemHighlightType.GENERIC_ERROR, fixes(parameter));
        }

        private LocalQuickFix[] fixes(PsiParameter parameter) {
            return new LocalQuickFix[] {
                new WrapParameterTypeQuickFix(parameter, List.class),
                new WrapParameterTypeQuickFix(parameter, Set.class),
                new ChangeParameterTypeQuickFix(type -> "Replace to " + type, parameter, type -> type + "[]"),
                new ChangeParameterTypeQuickFix(type -> "Replace to " + type, parameter, type -> type + "...")
            };
        }

    }

}
