package dev.rollczi.litecommands.intellijplugin.inspection.parameter;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.jvm.types.JvmPrimitiveTypeKind;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import com.intellij.psi.PsiType;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import dev.rollczi.litecommands.intellijplugin.inspection.LiteInspection;
import dev.rollczi.litecommands.intellijplugin.quickfix.ReplaceQuickFix;
import org.jetbrains.annotations.NotNull;

public class ParameterNullableOptionalPrimitiveInspection extends LiteInspection {

    private static final String DISPLAY_NAME = "Optional nullable parameter cannot be primitive";
    private static final String PROBLEM_DESCRIPTION = "Optional nullable parameter cannot be primitive";

    public ParameterNullableOptionalPrimitiveInspection() {
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
                if (parameter.getAnnotation(OptionalArg.class.getName()) == null) {
                    continue;
                }

                PsiType type = parameter.getType();
                JvmPrimitiveTypeKind primitiveKind = JvmPrimitiveTypeKind.getKindByName(type.getCanonicalText());

                if (primitiveKind != null) {
                    holder.registerProblem(parameter, PROBLEM_DESCRIPTION, ProblemHighlightType.GENERIC_ERROR, fixes(parameter, primitiveKind));
                }
            }
        }

        private LocalQuickFix[] fixes(PsiParameter par, JvmPrimitiveTypeKind primitiveKind) {
            Document document = par.getContainingFile().getViewProvider().getDocument();
            RangeMarker rangeMarker = document.createRangeMarker(par.getTypeElement().getTextRange());
            String boxedType = primitiveKind.getBoxedFqn().substring(primitiveKind.getBoxedFqn().lastIndexOf(".") + 1);

            return new LocalQuickFix[] {
                new ReplaceQuickFix(
                    "Replace primitive '" + primitiveKind.getName() + "' with boxed type '" + boxedType + "'",
                    rangeMarker,
                    boxedType
                )
            };
        }

    }

}
