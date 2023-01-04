package dev.rollczi.litecommands.intellijplugin.validation.annoation.implementations;

import dev.rollczi.litecommands.intellijplugin.legacy.annotation.AttributeType;
import dev.rollczi.litecommands.intellijplugin.validation.annoation.AnnotationValidator;
import dev.rollczi.litecommands.intellijplugin.validation.annoation.ValidationAnnotationInspectionTool;

public class ExecuteValidationInspection extends ValidationAnnotationInspectionTool {

    private static final AnnotationValidator ROUTE_VALIDATOR = AnnotationValidator.builder()
            .name("dev.rollczi.litecommands.command.execute.Execute")
            .attribute("route", AttributeType.STRING, CommandNameValidUtils::validateString)
            .attribute("aliases", AttributeType.ARRAY, CommandNameValidUtils::validateArray)
            .attribute("required", AttributeType.INT, number -> number.matches("[0-9]+|-1"))
            .attribute("min", AttributeType.INT, number -> number.matches("[0-9]+|-1"))
            .attribute("max", AttributeType.INT, number -> number.matches("[0-9]+|-1"))
            .build();

    public ExecuteValidationInspection() {
        super(ROUTE_VALIDATOR);
    }

}
