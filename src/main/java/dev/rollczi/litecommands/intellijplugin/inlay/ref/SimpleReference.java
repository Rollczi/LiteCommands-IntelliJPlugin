package dev.rollczi.litecommands.intellijplugin.inlay.ref;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.PsiType;
import com.intellij.psi.ResolveResult;
import dev.rollczi.litecommands.intellijplugin.icon.LiteIcons;
import dev.rollczi.litecommands.intellijplugin.util.LiteTypeSearcher;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class SimpleReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {

    private final PsiElement element;
    private final PsiType type;

    SimpleReference(PsiType type, @NotNull PsiElement element, TextRange textRange) {
        super(element, textRange);
        this.type = type;
        this.element = element;
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        List<ResolveResult> results = new ArrayList<>();
        for (PsiClass parser : LiteTypeSearcher.findParsersByType(element.getProject(), type)) {
            results.add(new PsiElementResolveResult(parser));
        }

        return results.toArray(new ResolveResult[0]);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        ResolveResult[] resolveResults = multiResolve(false);
        return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
    }

    @Override
    public Object @NotNull [] getVariants() {
        List<LookupElement> variants = new ArrayList<>();

        for (PsiClass parser : LiteTypeSearcher.findParsersByType(element.getProject(), type)) {
            variants.add(LookupElementBuilder
                .create(parser)
                .withIcon(LiteIcons.ADD)
                .withTypeText(parser.getContainingFile().getName())
            );
        }

        return variants.toArray();
    }

}