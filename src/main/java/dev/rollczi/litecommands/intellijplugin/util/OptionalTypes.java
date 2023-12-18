package dev.rollczi.litecommands.intellijplugin.util;

import com.intellij.psi.PsiType;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;
import panda.std.Option;

public final class OptionalTypes {

    private static final Set<String> OPTIONAL_WRAPPERS = Set.of(
        Optional.class.getName(),
        OptionalInt.class.getName(),
        OptionalDouble.class.getName(),
        OptionalLong.class.getName(),
        Option.class.getName()
    );

    private OptionalTypes() {}

    public static boolean isOptionalWrapper(PsiType type) {
        String canonicalText = type.getCanonicalText();
        return OPTIONAL_WRAPPERS.stream()
            .anyMatch(prefix -> canonicalText.startsWith(prefix));
    }

}



