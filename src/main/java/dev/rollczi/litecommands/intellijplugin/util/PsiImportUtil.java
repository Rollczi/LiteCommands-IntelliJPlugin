package dev.rollczi.litecommands.intellijplugin.util;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;

public final class PsiImportUtil {

    private PsiImportUtil() {
    }

    public static void importClass(PsiElement source, String classImport) {
        importClass(source.getContainingFile(), classImport);
    }

    public static void importClass(PsiFile file, String classImport) {
        Project project = file.getProject();

        if (!(file instanceof PsiJavaFile javaFile)) {
            throw new RuntimeException("Cannot add import to non-java file");
        }

        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
        PsiClass annotationClass = javaPsiFacade.findClass(classImport, GlobalSearchScope.allScope(project));

        if (annotationClass == null) {
            throw new RuntimeException("Cannot find annotation class " + classImport);
        }

        if (javaFile.findImportReferenceTo(annotationClass) == null) {
            javaFile.importClass(annotationClass);
        }
    }

}
