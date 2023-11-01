package dev.rollczi.litecommands.intellijplugin.features.inspection.executor;

import com.intellij.codeInsight.intention.AddAnnotationFix;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.*;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.bind.Bind;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.intellijplugin.features.inspection.LiteInspection;
import org.jetbrains.annotations.NotNull;

public class ExecutorNotAnnotatedParameterInspection extends LiteInspection {

    private static final String DISPLAY_NAME = "Executor parameter is not annotated with @Arg or @Context";
    private static final String PROBLEM_DESCRIPTION = "Missing @Arg or @Context annotation";

    public ExecutorNotAnnotatedParameterInspection() {
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
                if (parameter.getAnnotations().length != 0) {
                    continue;
                }

                holder.registerProblem(parameter, PROBLEM_DESCRIPTION, ProblemHighlightType.GENERIC_ERROR, fixes(parameter));
            }
        }

        private LocalQuickFix[] fixes(PsiParameter par) {
            return new LocalQuickFix[] {
                new AddAnnotationFix(Arg.class.getName(), par),
                new AddAnnotationFix(Context.class.getName(), par),
                new AddAnnotationFix(Bind.class.getName(), par),
            };
        }

    }

}
