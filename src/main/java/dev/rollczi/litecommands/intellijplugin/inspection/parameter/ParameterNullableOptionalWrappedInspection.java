package dev.rollczi.litecommands.intellijplugin.inspection.parameter;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import com.intellij.psi.PsiType;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import dev.rollczi.litecommands.intellijplugin.inspection.LiteInspection;
import dev.rollczi.litecommands.intellijplugin.quickfix.ReplaceQuickFix;
import dev.rollczi.litecommands.intellijplugin.util.LiteCommandsTypes;
import dev.rollczi.litecommands.intellijplugin.util.PsiImportUtil;
import org.jetbrains.annotations.NotNull;

public class ParameterNullableOptionalWrappedInspection extends LiteInspection {

    private static final String DISPLAY_NAME = "Optional parameter is primitive";
    private static final String PROBLEM_DESCRIPTION = "Optional parameter cannot be primitive";

    public ParameterNullableOptionalWrappedInspection() {
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
                PsiAnnotation psiAnnotation = parameter.getAnnotation(OptionalArg.class.getName());
                if (psiAnnotation == null) {
                    continue;
                }

                PsiType type = parameter.getType();
                if (!LiteCommandsTypes.isOptionalWrapper(type)) {
                    continue;
                }

                holder.registerProblem(parameter, PROBLEM_DESCRIPTION, ProblemHighlightType.GENERIC_ERROR, fixes(psiAnnotation));
            }
        }

        private LocalQuickFix[] fixes(PsiAnnotation annotation) {
            PsiFile file = annotation.getContainingFile();
            Document document = file.getViewProvider().getDocument();
            RangeMarker rangeMarker = document.createRangeMarker(annotation.getTextRange());

            return new LocalQuickFix[] {
                new ReplaceQuickFix(
                    "Replace with @Arg",
                    rangeMarker,
                    "@Arg",
                    () -> PsiImportUtil.importClass(file, Arg.class.getName())
                )
            };
        }

    }

}
