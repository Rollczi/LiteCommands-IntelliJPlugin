package dev.rollczi.litecommands.intellijplugin;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElement;

public class MyAnnotator implements Annotator {

    @Override
    public void annotate(PsiElement element, AnnotationHolder holder) {
        element instanceof PsiAnnotation
    }

}