package dev.rollczi.litecommands.intellijplugin.util;

import com.intellij.psi.PsiElement;

public record PsiValue<T>(PsiElement source, T value) {


}
