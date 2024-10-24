package dev.rollczi.litecommands.intellijplugin.quickfix;

import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiTypeElement;
import dev.rollczi.litecommands.intellijplugin.util.PsiImportUtil;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

public class ChangeParameterTypeQuickFix extends ReplaceQuickFix {

    private final String[] imports;

    public ChangeParameterTypeQuickFix(Function<String, String> info, PsiParameter parameter, Class<?> type) {
        this(info, parameter, oldType -> type.getSimpleName(), type.getName());
    }

    public ChangeParameterTypeQuickFix(Function<String, String> info, PsiParameter parameter, Function<String, String> mapper, String... imports) {
        super(
            info.apply(mappedType(parameter, mapper)),
            createTypeRange(parameter),
            mappedType(parameter, mapper)
        );

        this.imports = imports;
    }

    private static String mappedType(PsiParameter parameter, Function<String, String> mapper) {
        return mapper.apply(parameter.getType().getPresentableText());
    }

    private static RangeMarker createTypeRange(PsiParameter parameter) {
        PsiFile file = parameter.getContainingFile();
        Document document = file.getViewProvider().getDocument();
        PsiTypeElement typeElement = parameter.getTypeElement();

        if (typeElement == null) {
            return document.createRangeMarker(0, 0);
        }

        return document.createRangeMarker(typeElement.getTextRange());
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
        super.applyFix(project, descriptor);

        for (String importType : imports) {
            PsiImportUtil.importClass(descriptor.getPsiElement(), importType);
        }
    }

}