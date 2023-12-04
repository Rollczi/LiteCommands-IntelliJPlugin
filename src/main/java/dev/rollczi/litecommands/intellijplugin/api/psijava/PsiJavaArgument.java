package dev.rollczi.litecommands.intellijplugin.api.psijava;

import com.intellij.psi.PsiParameter;
import dev.rollczi.litecommands.intellijplugin.api.Argument;
import java.util.function.Function;

class PsiJavaArgument implements Argument {

    static final Function<String, String> ARG = rawValue -> "<" + rawValue + ">";
    static final Function<String, String> FLAG = rawValue -> rawValue;
    static final Function<String, String> OPTIONAL = rawValue -> "[" + rawValue + "]";
    static final Function<String, String> JOIN = rawValue -> "<" + rawValue + "...>";


    private final PsiJavaExecutorNode parent;
    private final PsiParameter psiParameter;
    private final String name;
    private final Function<String, String> formatter;

    public PsiJavaArgument(PsiJavaExecutorNode parent, PsiParameter psiParameter, String name, Function<String, String> formatter) {
        this.parent = parent;
        this.psiParameter = psiParameter;
        this.formatter = formatter;
        this.name = name.isBlank() ? psiParameter.getName() : name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String formattedName() {
        return formatter.apply(name);
    }

    @Override
    public String type() {
        return psiParameter.getType().getCanonicalText();
    }

}
