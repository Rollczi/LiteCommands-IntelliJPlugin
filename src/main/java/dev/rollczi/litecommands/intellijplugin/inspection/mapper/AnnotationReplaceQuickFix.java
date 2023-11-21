package dev.rollczi.litecommands.intellijplugin.inspection.mapper;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiImportList;
import com.intellij.psi.PsiImportStatement;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

class AnnotationReplaceQuickFix implements LocalQuickFix {

    private static final String DISPLAY_NAME = "Replace with @%s";

    private final AnnotationMapper mapper;
    private final ProblemsHolder holder;

    public AnnotationReplaceQuickFix(AnnotationMapper mapper, ProblemsHolder holder) {
        this.mapper = mapper;
        this.holder = holder;
    }

    @Override
    public @NotNull String getFamilyName() {
        return String.format(DISPLAY_NAME, this.mapper.modernName());
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
        PsiElement psiElement = descriptor.getPsiElement();

        if (!(psiElement instanceof PsiAnnotation)) {
            return;
        }

        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);
        PsiAnnotation oldAnnotation = (PsiAnnotation) psiElement;
        PsiAnnotation newAnnotation = elementFactory.createAnnotationFromText(
                this.mapper.modernAnnotation(),
                oldAnnotation
        );

        for (Attribute attribute : this.mapper.getAttributes()) {
            PsiAnnotationMemberValue attributeValue = oldAnnotation.findAttributeValue(attribute.legacyName());

            if (attributeValue != null && attribute.type().matches(attributeValue.getText())) {
                newAnnotation.setDeclaredAttributeValue(attribute.modernName(), attributeValue);
            }
        }

        oldAnnotation.replace(newAnnotation);

        // add import
        PsiFile containingFile = this.holder.getFile();
        if (!(containingFile instanceof PsiJavaFile)) {
            return;
        }

        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
        PsiClass routeClass = javaPsiFacade.findClass(this.mapper.modernQualified(), GlobalSearchScope.allScope(project));
        PsiJavaFile javaFile = (PsiJavaFile) containingFile;

        if (routeClass == null) {
            return;
        }

        if (javaFile.findImportReferenceTo(routeClass) == null) {
            javaFile.importClass(routeClass);
        }

        // Optimize imports
        PsiImportList importList = javaFile.getImportList();

        if (importList == null) {
            return;
        }

        PsiImportStatement statement = importList.findSingleClassImportStatement(this.mapper.legacyQualified());

        if (statement == null) {
            return;
        }

        statement.delete();
    }

}
