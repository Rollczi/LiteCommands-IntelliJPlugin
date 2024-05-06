package dev.rollczi.litecommands.intellijplugin.util;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiType;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ClassInheritorsSearch;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.parser.TypedParser;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolverBase;
import dev.rollczi.litecommands.argument.resolver.MultipleArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.TypedArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.collector.AbstractCollectorArgumentResolver;
import dev.rollczi.litecommands.argument.suggester.Suggester;
import dev.rollczi.litecommands.argument.suggester.TypedSuggester;
import dev.rollczi.litecommands.join.JoinArgumentResolver;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class LiteTypeSearcher {

    private static final int PARSER_GENERIC_TYPE_INDEX = 1;
    private static final int SUGGESTER_GENERIC_TYPE_INDEX = 1;

    /**
     * Pre-defined filters (abstract classes, interfaces, etc.)
     */
    private static final Set<String> PRE_DEFINED_FILTERS = Set.of(
        Suggester.class.getName(),
        TypedSuggester.class.getName(),
        Parser.class.getName(),
        TypedParser.class.getName(),
        JoinArgumentResolver.class.getName(),
        TypedArgumentResolver.class.getName(),
        ArgumentResolver.class.getName(),
        ArgumentResolverBase.class.getName(),
        MultipleArgumentResolver.class.getName(),
        AbstractCollectorArgumentResolver.class.getName()
    );

    private static final Set<String> SUGGESTERS_FILTERS = calculateFiltersForBase(Suggester.class, ArgumentResolver.class);
    private static final Set<String> PARSER_FILTERS = calculateFiltersForBase(Parser.class, ArgumentResolver.class);

    public static @NotNull Collection<PsiClass> findParsersByType(Project project, PsiType type) {
        return getAllClassesByGeneric(project, Parser.class, PARSER_GENERIC_TYPE_INDEX, PARSER_FILTERS, type);
    }

    public static @NotNull Collection<PsiClass> findSuggestersByType(Project project, PsiType type) {
        return getAllClassesByGeneric(project, Suggester.class, SUGGESTER_GENERIC_TYPE_INDEX, SUGGESTERS_FILTERS, type);
    }

    private static Collection<PsiClass> getAllClassesByGeneric(
        Project project,
        Class<?> base,
        int genericIndexOfBase,
        Set<String> filters,
        PsiType compareToGeneric
    ) {
        GlobalSearchScope resolveScope = GlobalSearchScope.allScope(project);
        PsiClassType psiCompareToGeneric = PsiPrimitiveUtil.boxPrimitiveType(compareToGeneric, project);

        JavaPsiFacade psiFacade = JavaPsiFacade.getInstance(project);
        String baseName = base.getName();
        PsiClass psiTypeToCheck = psiFacade.findClass(baseName, resolveScope);

        if (psiTypeToCheck == null) {
            return Collections.emptyList();
        }

        return ClassInheritorsSearch.search(psiTypeToCheck)
            .filtering(psiClass -> !psiClass.isInterface() && !PRE_DEFINED_FILTERS.contains(psiClass.getQualifiedName()))
            .filtering(psiClass -> isGenericEqualsTo(psiClass.getSuperTypes(), baseName, psiCompareToGeneric, genericIndexOfBase, filters))
            .findAll();
    }

    private static boolean isGenericEqualsTo(
        PsiType[] superTypes,
        String baseName,
        PsiClassType compareToGeneric,
        int genericIndex,
        Set<String> filters
    ) {
        for (PsiType superType : superTypes) {
            if (!(superType instanceof PsiClassType.Stub stub)) {
                continue;
            }

            String subCanonicalText = stub.rawType().getCanonicalText();

            if (!subCanonicalText.equals(baseName)) {
                if (filters.contains(subCanonicalText)) {
                    continue;
                }

                if (isGenericEqualsTo(superType.getSuperTypes(), baseName, compareToGeneric, genericIndex, filters)) {
                    return true;
                }

                continue;
            }

            PsiType[] parameters = stub.getParameters();

            if (parameters.length <= genericIndex) {
                return false;
            }

            PsiType typeToCheck = parameters[genericIndex];

            return typeToCheck.equals(compareToGeneric);
        }

        return false;
    }

    private static Set<String> calculateFiltersForBase(Class<?> base, Class<?>... samples) {
        Set<String> filters = new HashSet<>();

        for (Class<?> sample : samples) {
            filters.addAll(calculateFilters(base, sample).filters());
        }

        return filters;
    }

    private static Result calculateFilters(Class<?> base, Class<?> sample) {
        Set<String> filters = new HashSet<>();
        boolean isBaseIn = false;

        for (Class<?> sampleInterface : sample.getInterfaces()) {
            if (sampleInterface.equals(base)) {
                isBaseIn = true;
                continue;
            }

            Result baseFilters = calculateFilters(base, sampleInterface);

            if (baseFilters.isBaseIn()) {
                isBaseIn = true;
            } else {
                filters.add(sampleInterface.getName());
            }

            filters.addAll(baseFilters.filters());
        }

        return new Result(isBaseIn, filters);
    }

    private record Result(boolean isBaseIn, Set<String> filters) {
    }

}
