package dev.rollczi.litecommands.intellijplugin.inspection.mapper.validation;

import com.intellij.codeInspection.ProblemHighlightType;
import dev.rollczi.litecommands.annotations.shortcut.Shortcut;
import dev.rollczi.litecommands.intellijplugin.inspection.mapper.AnnotationMapperSchema;
import dev.rollczi.litecommands.intellijplugin.inspection.mapper.AttributeType;
import dev.rollczi.litecommands.intellijplugin.inspection.mapper.ValidationAnnotationInspectionTool;

public class ShortcutValidationInspection extends ValidationAnnotationInspectionTool {

    private static final AnnotationMapperSchema ROUTE_VALIDATOR = AnnotationMapperSchema.builder()
            .name(Shortcut.class.getName())
            .attribute("value", AttributeType.ARRAY, CommandNameValidUtils::validateRoute, new RouteQuickFix())
            .build();

    public ShortcutValidationInspection() {
        super(ROUTE_VALIDATOR, ProblemHighlightType.GENERIC_ERROR);
    }

}
