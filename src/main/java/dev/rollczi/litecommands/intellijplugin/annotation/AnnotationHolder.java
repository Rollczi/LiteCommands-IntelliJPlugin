package dev.rollczi.litecommands.intellijplugin.annotation;

import com.intellij.psi.PsiAnnotation;

import java.lang.annotation.Annotation;

public class AnnotationHolder<A extends Annotation> {

    private final PsiAnnotation annotation;
    private final A instance;

    public AnnotationHolder(PsiAnnotation annotation, A instance) {
        this.annotation = annotation;
        this.instance = instance;
    }

    public PsiAnnotation asPsi() {
        return annotation;
    }

    public A asAnnotation() {
        return instance;
    }

}
