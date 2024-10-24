package dev.rollczi.litecommands.intellijplugin.quickfix;

import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiTypeElement;
import dev.rollczi.litecommands.intellijplugin.util.PsiImportUtil;
import org.jetbrains.annotations.NotNull;

public class WrapParameterTypeQuickFix extends ReplaceQuickFix {

    private final String importType;

    public WrapParameterTypeQuickFix(PsiParameter parameter, Class<?> type) {
        this(parameter, type.getSimpleName(), type.getName());
    }

    public WrapParameterTypeQuickFix(PsiParameter parameter, String type, String importType) {
        super(
            "Replace to " + getWrappedType(parameter, type),
            createTypeRange(parameter),
            getWrappedType(parameter, type)
        );

        this.importType = importType;
    }

    private static @NotNull String getWrappedType(PsiParameter parameter, String type) {
        return type + "<" + parameter.getType().getPresentableText() + ">";
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
        PsiImportUtil.importClass(descriptor.getPsiElement(), importType);
    }

}