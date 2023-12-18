package dev.rollczi.litecommands.intellijplugin.inspection.annotation;

import com.intellij.codeInsight.Nullability;
import com.intellij.codeInsight.annoPackages.AnnotationPackageSupport;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.flag.Flag;
import dev.rollczi.litecommands.annotations.join.Join;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class LiteNullabilityAnnotationPackage implements AnnotationPackageSupport {

    @Override
    public @NotNull List<String> getNullabilityAnnotations(@NotNull Nullability nullability) {
        return switch (nullability) {
            case NOT_NULL -> List.of(
                Arg.class.getName(),
                Join.class.getName(),
                Flag.class.getName()
            );
            case NULLABLE -> List.of(
                OptionalArg.class.getName()
            );
            case UNKNOWN -> List.of();
        };
    }

}
