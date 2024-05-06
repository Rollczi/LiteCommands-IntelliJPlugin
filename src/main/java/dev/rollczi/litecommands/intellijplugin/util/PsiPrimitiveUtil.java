package dev.rollczi.litecommands.intellijplugin.util;

import com.intellij.lang.jvm.types.JvmPrimitiveTypeKind;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiType;
import com.intellij.psi.search.GlobalSearchScope;

public final class PsiPrimitiveUtil {

    private PsiPrimitiveUtil() {
    }

    public static PsiClassType boxPrimitiveType(PsiType type, Project project) {
        if (type instanceof PsiClassType) {
            return (PsiClassType) type;
        }

        JvmPrimitiveTypeKind primitiveKind = JvmPrimitiveTypeKind.getKindByName(type.getCanonicalText());

        if (primitiveKind == null) {
            throw new IllegalArgumentException("Type is not primitive and cannot be boxed");
        }

        return PsiType.getTypeByName(primitiveKind.getBoxedFqn(), project, GlobalSearchScope.allScope(project));
    }

}
