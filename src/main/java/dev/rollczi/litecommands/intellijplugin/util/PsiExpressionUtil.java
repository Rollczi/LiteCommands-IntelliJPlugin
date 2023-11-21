package dev.rollczi.litecommands.intellijplugin.util;

import com.intellij.lang.Language;
import com.intellij.lang.java.parser.JavaParser;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiFile;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.jetbrains.kotlin.psi.KtBinaryExpression;
import org.jetbrains.kotlin.psi.KtExpression;
import org.jetbrains.kotlin.psi.KtPsiFactory;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UFile;
import org.jetbrains.uast.ULiteralExpression;
import org.jetbrains.uast.UastContextKt;
import org.jetbrains.uast.UastUtils;
import org.jetbrains.uast.generate.UastCodeGenerationPlugin;
import org.jetbrains.uast.generate.UastElementFactory;
import org.jetbrains.uast.util.UastExpressionUtils;

public final class PsiExpressionUtil {

    private static final Map<String, ArrayFormat> FORMATS = Map.of(
        "java", new ArrayFormat("{", "}", true, ", ", "\"", "\""),
        "kotlin", new ArrayFormat("[", "]", false, ", ", "\"", "\"")
    );

    private PsiExpressionUtil() {
    }

    public static UElement arrayExpression(UFile file, String... values) {
        return arrayExpression(file, Arrays.asList(values));
    }

    public static UElement arrayExpression(UFile file, Collection<String> values) {
        Language language = file.getLang();
        String languageID = language.getID().toLowerCase(Locale.ROOT);
        ArrayFormat format = FORMATS.getOrDefault(languageID, FORMATS.get("java"));

        String collected = values.stream()
            .map(value -> format.valuePrefix + value + format.valueSuffix)
            .collect(Collectors.joining(format.separator));

        if (values.size() == 1 && format.oneValueWithoutBrackets) {
            return generate(file, language, collected);
        }

        return generate(file, language, format.leftBracket + collected + format.rightBracket);
    }

    private static UElement generate(UElement element, Language language, String expression) {
        UastElementFactory factory = UastCodeGenerationPlugin.byLanguage(language).getElementFactory(element.getSourcePsi().getProject());

        return factory.createStringLiteralExpression(expression, element.getSourcePsi());
    }

    record ArrayFormat(String leftBracket, String rightBracket, boolean oneValueWithoutBrackets, String separator, String valuePrefix, String valueSuffix) {
    }

}
