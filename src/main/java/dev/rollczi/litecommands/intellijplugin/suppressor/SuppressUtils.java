package dev.rollczi.litecommands.intellijplugin.suppressor;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.command.RootCommand;
import java.util.Set;

public class SuppressUtils {

    public final static String UNUSED_DECLARATION = "UnusedDeclaration";
    public final static String OPTIONAL_FIELD_OR_PARAMETER_TYPE = "OptionalUsedAsFieldOrParameterType";
    public final static String NULLABLE_PROBLEMS = "NullableProblems";

    private final static Set<String> INJECT_ANNOTATIONS = Set.of(
        "Inject",
        "Autowired"
    );

    public static boolean isInjectClass(PsiClass psiClass) {
        if (!isCommandClass(psiClass)) {
            return false;
        }

        PsiMethod[] constructors = psiClass.getConstructors();

        for (PsiMethod constructor : constructors) {
            if (isInjectConstructor(constructor)) {
                return true;
            }
        }

        return false;
    }

    private static boolean isCommandClass(PsiClass psiClass) {
        return psiClass.getAnnotation(Command.class.getName()) != null || psiClass.getAnnotation(RootCommand.class.getName()) != null;
    }

    public static boolean isInjectConstructor(PsiMethod psiMethod) {
        if (!psiMethod.isConstructor()) {
            return false;
        }

        PsiClass containingClass = psiMethod.getContainingClass();

        if (containingClass == null) {
            return false;
        }

        if (!isCommandClass(containingClass)) {
            return false;
        }

        PsiAnnotation[] annotations = psiMethod.getAnnotations();

        for (PsiAnnotation annotation : annotations) {
            String qualifiedName = annotation.getQualifiedName();

            if (qualifiedName == null) {
                continue;
            }

            int index = qualifiedName.lastIndexOf('.');

            if (index == -1) {
                continue;
            }

            String name = qualifiedName.substring(index + 1);

            if (INJECT_ANNOTATIONS.contains(name)) {
                return true;
            }
        }

        return false;
    }

}
