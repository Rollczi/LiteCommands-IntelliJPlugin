package dev.rollczi.litecommands.intellijplugin.validation.annoation.implementations;

import dev.rollczi.litecommands.intellijplugin.legacy.annotation.AttributeType;
import dev.rollczi.litecommands.intellijplugin.validation.annoation.AnnotationValidator;
import dev.rollczi.litecommands.intellijplugin.validation.annoation.ValidationAnnotationInspectionTool;

public class RouteValidationInspection extends ValidationAnnotationInspectionTool {

    // TODO context
    private static final AnnotationValidator ROUTE_VALIDATOR = AnnotationValidator.builder()
            .name("dev.rollczi.litecommands.command.route.Route")
            .attribute("name", AttributeType.STRING, CommandNameValidUtils::validateString, new StringQuickFix())
            .attribute("aliases", AttributeType.ARRAY, CommandNameValidUtils::validateString, new StringQuickFix())
            .build();

    public RouteValidationInspection() {
        super(ROUTE_VALIDATOR);
    }

}
