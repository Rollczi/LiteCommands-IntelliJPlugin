package dev.rollczi.litecommands.intellijplugin.old.annotation.implementations;

import dev.rollczi.litecommands.intellijplugin.old.annotation.AttributeType;
import dev.rollczi.litecommands.intellijplugin.old.annotation.AnnotationValidator;
import dev.rollczi.litecommands.intellijplugin.old.annotation.ValidationAnnotationInspectionTool;

public class ExecuteValidationInspection extends ValidationAnnotationInspectionTool {

    private static final String NUMBER_MORE_OR_EQ_MINUS_ONE_REGEX = "( *[0-9]+ *| *- *1 *)";

    private static final AnnotationValidator ROUTE_VALIDATOR = AnnotationValidator.builder()
            .name("dev.rollczi.litecommands.command.execute.Execute")
            .attribute("route", AttributeType.STRING, CommandNameValidUtils::validateString, new StringQuickFix())
            .attribute("aliases", AttributeType.ARRAY, CommandNameValidUtils::validateString, new StringQuickFix())
            .attribute("required", AttributeType.INT, number -> number.matches(NUMBER_MORE_OR_EQ_MINUS_ONE_REGEX), new MoreThanMinusOneQuickFix())
            .attribute("min", AttributeType.INT, number -> number.matches(NUMBER_MORE_OR_EQ_MINUS_ONE_REGEX), new MoreThanMinusOneQuickFix())
            .attribute("max", AttributeType.INT, number -> number.matches(NUMBER_MORE_OR_EQ_MINUS_ONE_REGEX), new MoreThanMinusOneQuickFix())
            .build();

    public ExecuteValidationInspection() {
        super(ROUTE_VALIDATOR);
    }

}
