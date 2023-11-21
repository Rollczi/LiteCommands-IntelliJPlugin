package dev.rollczi.litecommands.intellijplugin.inspection.mapper;

import com.intellij.psi.PsiElement;

class AttributeValue {

    private final String text;
    private final String rawText;
    private final PsiElement element;

    AttributeValue(String text, String rawText, PsiElement element) {
        this.text = text;
        this.rawText = rawText;
        this.element = element;
    }

    public String text() {
        return this.text;
    }

    public String rawText() {
        return this.rawText;
    }

    public PsiElement element() {
        return this.element;
    }

}
