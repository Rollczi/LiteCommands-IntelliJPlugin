package dev.rollczi.litecommands.intellijplugin.api.psijava;

import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import dev.rollczi.litecommands.intellijplugin.api.Permission;
import dev.rollczi.litecommands.intellijplugin.navigatable.NavigatableReference;

public class PsiJavaPermission implements Permission {

    private final String name;
    private final Navigatable navigatable;

    public PsiJavaPermission(String name, PsiElement source) {
        this.name = name;
        this.navigatable = NavigatableReference.ofPsiElement(() -> source);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Navigatable navigatable() {
        return navigatable;
    }

}
