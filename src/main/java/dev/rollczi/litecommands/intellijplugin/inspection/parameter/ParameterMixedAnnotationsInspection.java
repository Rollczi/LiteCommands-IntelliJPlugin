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
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.bind.Bind;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.flag.Flag;
import dev.rollczi.litecommands.annotations.join.Join;
import dev.rollczi.litecommands.annotations.literal.Literal;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import dev.rollczi.litecommands.annotations.varargs.Varargs;
import dev.rollczi.litecommands.intellijplugin.inspection.LiteInspection;
import dev.rollczi.litecommands.intellijplugin.quickfix.ReplaceQuickFix;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public class ParameterMixedAnnotationsInspection extends LiteInspection {

    private static final Set<String> ANNOTATIONS = Set.of(
        Bind.class.getName(),
        Context.class.getName(),
        Arg.class.getName(),
        OptionalArg.class.getName(),
        Flag.class.getName(),
        Join.class.getName(),
        Varargs.class.getName()
    );

    private static final String DISPLAY_NAME = "Mixed annotations";
    private static final String PROBLEM_DESCRIPTION = "You cannot mix: %s";

    public ParameterMixedAnnotationsInspection() {
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

                if (annotations.length < 2) {
                    continue;
                }

                List<PsiAnnotation> psiAnnotations = new ArrayList<>();

                for (PsiAnnotation psiAnnotation : annotations) {
                    if (ANNOTATIONS.contains(psiAnnotation.getQualifiedName())) {
                        psiAnnotations.add(psiAnnotation);
                    }
                }

                if (psiAnnotations.size() < 2) {
                    continue;
                }


                String problemMessage = String.format(PROBLEM_DESCRIPTION, psiAnnotations.stream()
                    .map(psiAnnotation -> getAnnotationName(psiAnnotation))
                    .collect(Collectors.joining(", ")));

                holder.registerProblem(parameter, problemMessage, ProblemHighlightType.GENERIC_ERROR, fixes(parameter, psiAnnotations));
            }
        }

        private LocalQuickFix[] fixes(PsiParameter parameter, List<PsiAnnotation> annotations) {
            Document document = parameter.getContainingFile().getViewProvider().getDocument();

            return annotations.stream()
                .map(psiAnnotation -> {
                    RangeMarker rangeMarker = document.createRangeMarker(psiAnnotation.getTextRange());
                    String annotationName = getAnnotationName(psiAnnotation);

                    return new ReplaceQuickFix(
                        "Remove '" + annotationName + "' annotation",
                        rangeMarker,
                        ""
                    );
                })
                .toArray(LocalQuickFix[]::new);
        }

        private String getAnnotationName(PsiAnnotation annotation) {
            return annotation.getQualifiedName().substring(annotation.getQualifiedName().lastIndexOf(".") + 1);
        }

    }

}
