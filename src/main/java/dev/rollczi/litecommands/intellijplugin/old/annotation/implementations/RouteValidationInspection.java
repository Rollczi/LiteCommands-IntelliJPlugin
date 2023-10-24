package dev.rollczi.litecommands.intellijplugin.old.annotation.implementations;

import dev.rollczi.litecommands.intellijplugin.old.annotation.AttributeType;
import dev.rollczi.litecommands.intellijplugin.old.annotation.AnnotationValidator;
import dev.rollczi.litecommands.intellijplugin.old.annotation.ValidationAnnotationInspectionTool;

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
