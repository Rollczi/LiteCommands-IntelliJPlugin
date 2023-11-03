package dev.rollczi.litecommands.intellijplugin.api.psijava;

import com.intellij.psi.PsiParameter;
import dev.rollczi.litecommands.intellijplugin.api.Argument;

class PsiJavaArgument implements Argument {

    private final PsiJavaExecutorNode parent;
    private final PsiParameter psiParameter;
    private final String name;

    public PsiJavaArgument(PsiJavaExecutorNode parent, PsiParameter psiParameter, String name) {
        this.parent = parent;
        this.psiParameter = psiParameter;
        this.name = name.isBlank() ? psiParameter.getName() : name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String type() {
        return psiParameter.getType().getCanonicalText();
    }

}
