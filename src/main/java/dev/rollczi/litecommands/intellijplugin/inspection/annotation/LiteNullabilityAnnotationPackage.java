package dev.rollczi.litecommands.intellijplugin.inspection.annotation;

import com.intellij.codeInsight.ContextNullabilityInfo;
import com.intellij.codeInsight.Nullability;
import com.intellij.codeInsight.NullabilityAnnotationInfo;
import com.intellij.codeInsight.annoPackages.AnnotationPackageSupport;
import com.intellij.lang.jvm.annotation.JvmAnnotationAttribute;
import com.intellij.lang.jvm.annotation.JvmAnnotationAttributeValue;
import com.intellij.lang.jvm.annotation.JvmAnnotationConstantValue;
import com.intellij.psi.PsiAnnotation;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.flag.Flag;
import dev.rollczi.litecommands.annotations.join.Join;
import dev.rollczi.litecommands.annotations.literal.Literal;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import dev.rollczi.litecommands.annotations.varargs.Varargs;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LiteNullabilityAnnotationPackage implements AnnotationPackageSupport {

    @Override
    public @NotNull List<String> getNullabilityAnnotations(@NotNull Nullability nullability) {
        return switch (nullability) {
            case NOT_NULL -> List.of(
                Join.class.getName(),
                Varargs.class.getName(),
                Literal.class.getName(),
                Flag.class.getName()
            );
            case NULLABLE -> List.of(
                OptionalArg.class.getName()
            );
            case UNKNOWN -> List.of();
        };
    }

    @Override
    public @NotNull ContextNullabilityInfo getNullabilityByContainerAnnotation(@NotNull PsiAnnotation anno, PsiAnnotation.TargetType[] types, boolean superPackage) {
        if (!Arg.class.getName().equals(anno.getQualifiedName())) {
            return ContextNullabilityInfo.EMPTY;
        }

        @Nullable JvmAnnotationAttribute attribute = anno.findAttribute("nullable");
        if (attribute == null) {
            return ContextNullabilityInfo.EMPTY;
        }

        @Nullable JvmAnnotationAttributeValue value = attribute.getAttributeValue();
        if (!(value instanceof JvmAnnotationConstantValue constantValue)) {
            return ContextNullabilityInfo.EMPTY;
        }

        if (!(constantValue.getConstantValue() instanceof Boolean nullable)) {
            return ContextNullabilityInfo.EMPTY;
        }

        return ContextNullabilityInfo.constant(new NullabilityAnnotationInfo(anno, nullable ? Nullability.NULLABLE : Nullability.NOT_NULL, false));
    }

}
