package dev.rollczi.litecommands.intellijplugin.inspection.annotation;

import com.intellij.codeInsight.Nullability;
import com.intellij.codeInsight.NullabilityAnnotationInfo;
import com.intellij.codeInsight.annoPackages.AnnotationPackageSupport;
import com.intellij.lang.jvm.annotation.JvmAnnotationAttribute;
import com.intellij.lang.jvm.annotation.JvmAnnotationAttributeValue;
import com.intellij.lang.jvm.annotation.JvmAnnotationConstantValue;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElement;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.flag.Flag;
import dev.rollczi.litecommands.annotations.join.Join;
import dev.rollczi.litecommands.annotations.literal.Literal;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import dev.rollczi.litecommands.annotations.varargs.Varargs;
import dev.rollczi.litecommands.intellijplugin.annotation.AnnotationFactory;
import dev.rollczi.litecommands.intellijplugin.annotation.AnnotationHolder;
import java.util.List;
import java.util.Optional;
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
    public @Nullable NullabilityAnnotationInfo getNullabilityByContainerAnnotation(@NotNull PsiAnnotation anno, @NotNull PsiElement context, PsiAnnotation.TargetType @NotNull [] types, boolean superPackage) {
        if (!Arg.class.getName().equals(anno.getQualifiedName())) {
            return null;
        }

        @Nullable JvmAnnotationAttribute attribute = anno.findAttribute("nullable");
        if (attribute == null) {
            return null;
        }

        @Nullable JvmAnnotationAttributeValue value = attribute.getAttributeValue();
        if (!(value instanceof JvmAnnotationConstantValue constantValue)) {
            return null;
        }

        if (!(constantValue.getConstantValue() instanceof Boolean nullable)) {
            return null;
        }

        return new NullabilityAnnotationInfo(anno, nullable ? Nullability.NULLABLE : Nullability.NOT_NULL, false);
    }

}
