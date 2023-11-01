package dev.rollczi.litecommands.intellijplugin.features.inspection;

import com.intellij.codeInspection.InspectionToolProvider;
import dev.rollczi.litecommands.intellijplugin.features.inspection.executor.ExecutorNotAnnotatedParameterInspection;
import org.jetbrains.annotations.NotNull;

public class LiteInspectionProvider implements InspectionToolProvider {

    @Override
    public Class<? extends LiteInspection> @NotNull [] getInspectionClasses() {
        return new Class[]{
                ExecutorNotAnnotatedParameterInspection.class,
        };
    }

}
