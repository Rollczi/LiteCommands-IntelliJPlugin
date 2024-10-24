package dev.rollczi.litecommands.intellijplugin.inspection;

import com.intellij.codeInspection.InspectionToolProvider;
import dev.rollczi.litecommands.intellijplugin.inspection.executor.ExecutorNotAnnotatedParameterInspection;
import dev.rollczi.litecommands.intellijplugin.inspection.mapper.legacy.LegacyArgInspection;
import dev.rollczi.litecommands.intellijplugin.inspection.mapper.legacy.LegacyExecuteInspection;
import dev.rollczi.litecommands.intellijplugin.inspection.mapper.legacy.LegacyJoinInspection;
import dev.rollczi.litecommands.intellijplugin.inspection.mapper.legacy.LegacyPermissionInspection;
import dev.rollczi.litecommands.intellijplugin.inspection.mapper.legacy.LegacyRouteInspection;
import dev.rollczi.litecommands.intellijplugin.inspection.mapper.legacy.LegacySectionInspection;
import dev.rollczi.litecommands.intellijplugin.inspection.mapper.validation.ExecuteValidationInspection;
import dev.rollczi.litecommands.intellijplugin.inspection.mapper.validation.CommandValidationInspection;
import dev.rollczi.litecommands.intellijplugin.inspection.mapper.validation.ShortcutValidationInspection;
import dev.rollczi.litecommands.intellijplugin.inspection.parameter.LiteralAnnotationInspection;
import dev.rollczi.litecommands.intellijplugin.inspection.parameter.ParameterMixedAnnotationsInspection;
import dev.rollczi.litecommands.intellijplugin.inspection.parameter.ParameterNullableOptionalPrimitiveInspection;
import dev.rollczi.litecommands.intellijplugin.inspection.parameter.ParameterNullableOptionalWrappedInspection;
import dev.rollczi.litecommands.intellijplugin.inspection.parameter.VarargsAnnotationInspection;
import org.jetbrains.annotations.NotNull;

public class LiteInspectionProvider implements InspectionToolProvider {

    @Override
    public Class<? extends LiteInspection> @NotNull [] getInspectionClasses() {
        return new Class[]{
            ExecutorNotAnnotatedParameterInspection.class,
            ParameterNullableOptionalPrimitiveInspection.class,
            VarargsAnnotationInspection.class,
            LiteralAnnotationInspection.class,
            ParameterNullableOptionalWrappedInspection.class,
            ParameterMixedAnnotationsInspection.class,
            LegacyRouteInspection.class,
            LegacySectionInspection.class,
            LegacyArgInspection.class,
            LegacyExecuteInspection.class,
            LegacyJoinInspection.class,
            LegacyPermissionInspection.class,
            ExecuteValidationInspection.class,
            CommandValidationInspection.class,
            ShortcutValidationInspection.class
        };
    }

}
