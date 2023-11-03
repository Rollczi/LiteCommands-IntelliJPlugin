package dev.rollczi.litecommands.intellijplugin.api.psijava;

import com.intellij.psi.PsiElement;
import dev.rollczi.litecommands.intellijplugin.api.PermissionEntry;
import dev.rollczi.litecommands.intellijplugin.navigatable.NavigatableReference;

class PsiJavaPermissionEntry implements PermissionEntry {

    private final String name;
    private final NavigatableReference navigatable;

    public PsiJavaPermissionEntry(String name, PsiElement source) {
        this.name = name;
        this.navigatable = NavigatableReference.ofPsiElement(() -> source);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public NavigatableReference navigatable() {
        return navigatable;
    }

}
