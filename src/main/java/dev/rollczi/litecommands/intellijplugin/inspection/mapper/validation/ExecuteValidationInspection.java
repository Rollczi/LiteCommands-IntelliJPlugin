package dev.rollczi.litecommands.intellijplugin.inspection.mapper.validation;

import com.intellij.codeInspection.ProblemHighlightType;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.intellijplugin.inspection.mapper.AttributeType;
import dev.rollczi.litecommands.intellijplugin.inspection.mapper.AnnotationMapperSchema;
import dev.rollczi.litecommands.intellijplugin.inspection.mapper.ValidationAnnotationInspectionTool;

public class ExecuteValidationInspection extends ValidationAnnotationInspectionTool {

    private static final AnnotationMapperSchema ROUTE_VALIDATOR = AnnotationMapperSchema.builder()
            .name(Execute.class.getName())
            .attribute("name", AttributeType.STRING, CommandNameValidUtils::validateMultiRoute, RouteQuickFix.multiple())
            .attribute("aliases", AttributeType.ARRAY, CommandNameValidUtils::validateMultiRoute, RouteQuickFix.multiple())
            .build();

    public ExecuteValidationInspection() {
        super(ROUTE_VALIDATOR, ProblemHighlightType.GENERIC_ERROR);
    }

}
