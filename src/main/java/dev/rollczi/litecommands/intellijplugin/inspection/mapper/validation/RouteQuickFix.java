package dev.rollczi.litecommands.intellijplugin.inspection.mapper.validation;

import java.util.function.UnaryOperator;

class RouteQuickFix implements UnaryOperator<String> {

    @Override
    public String apply(String rawValue) {
        if (rawValue.startsWith("\"") && rawValue.endsWith("\"")) {
            rawValue = rawValue.substring(1, rawValue.length() - 1);
        }

        String value = rawValue
                .replaceAll(" +", " ")
                .trim();

        if (value.isEmpty()) {
            value = "name";
        }

        return "\"" + value + "\"";
    }

}
