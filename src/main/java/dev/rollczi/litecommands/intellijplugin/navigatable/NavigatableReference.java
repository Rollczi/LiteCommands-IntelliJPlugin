package dev.rollczi.litecommands.intellijplugin.navigatable;

import com.intellij.platform.backend.navigation.NavigationRequest;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import java.util.Optional;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;

public class NavigatableReference implements Navigatable {

    private final Supplier<Optional<Navigatable>> source;

    public NavigatableReference(Supplier<Navigatable> source) {
        this.source = () -> Optional.ofNullable(source.get());
    }

    @Override
    public @Nullable NavigationRequest navigationRequest() {
        return source.get()
            .map(Navigatable::navigationRequest)
            .orElse(null);

    }

    @Override
    public void navigate(boolean requestFocus) {
        source.get()
            .ifPresent(navigatable -> navigatable.navigate(requestFocus));
    }

    @Override
    public boolean canNavigate() {
        return source.get()
            .map(Navigatable::canNavigate)
            .orElse(false);
    }

    @Override
    public boolean canNavigateToSource() {
        return source.get()
            .map(Navigatable::canNavigateToSource)
            .orElse(false);
    }

    public static NavigatableReference of(Supplier<Navigatable> source) {
        return new NavigatableReference(source);
    }

    public static NavigatableReference empty() {
        return new NavigatableReference(() -> null);
    }

    public static NavigatableReference ofPsiElement(Supplier<PsiElement> source) {
        return new NavigatableReference(() -> {
            PsiElement psiElement = source.get();

            if (psiElement instanceof Navigatable) {
                return(Navigatable) psiElement;
            }

            return null;
        });
    }

}
