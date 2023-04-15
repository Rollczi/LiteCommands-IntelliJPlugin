package dev.rollczi.litecommands.intellijplugin;

import com.intellij.codeInspection.InspectionToolProvider;
import com.intellij.codeInspection.LocalInspectionTool;
import dev.rollczi.litecommands.intellijplugin.legacy.annotation.implemenations.LegacySectionInspection;
import dev.rollczi.litecommands.intellijplugin.validation.annotation.implementations.ExecuteValidationInspection;
import dev.rollczi.litecommands.intellijplugin.validation.annotation.implementations.RouteValidationInspection;
import org.jetbrains.annotations.NotNull;

public class CodeInspectionProvider implements InspectionToolProvider {

    @Override
    public Class<? extends LocalInspectionTool> @NotNull [] getInspectionClasses() {
        return new Class[]{
                LegacySectionInspection.class,
                RouteValidationInspection.class,
                ExecuteValidationInspection.class,
        };
    }

}
