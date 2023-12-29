package dev.rollczi.litecommands.intellijplugin.util;

import com.intellij.lang.jvm.types.JvmPrimitiveTypeKind;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiType;
import com.intellij.psi.search.GlobalSearchScope;
import java.util.Collection;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;
import panda.std.Option;

public class LiteCommandsTypes {

    private static final Set<String> OPTIONAL_WRAPPERS = Set.of(
        java.util.Optional.class.getName(),
        OptionalInt.class.getName(),
        OptionalDouble.class.getName(),
        OptionalLong.class.getName(),
        Option.class.getName()
    );

    public static boolean isOptionalWrapper(PsiType type) {
        String canonicalText = type.getCanonicalText();
        return OPTIONAL_WRAPPERS.stream()
            .anyMatch(prefix -> canonicalText.startsWith(prefix));
    }

    public static boolean isArrayWrapper(PsiType type, Project project) {
        PsiType collectionType = PsiType.getTypeByName(Collection.class.getName(), project, GlobalSearchScope.allScope(project));

        if (collectionType.isAssignableFrom(type)) {
            return true;
        }

        if (type.getArrayDimensions() > 0) {
            return true;
        }

        return false;
    }

    public static boolean isPrimitiveType(PsiType type) {
        JvmPrimitiveTypeKind primitiveKind = JvmPrimitiveTypeKind.getKindByName(type.getCanonicalText());

        return primitiveKind != null;
    }

}
