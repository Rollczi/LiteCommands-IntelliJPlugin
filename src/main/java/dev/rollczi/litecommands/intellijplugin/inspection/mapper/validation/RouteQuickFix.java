package dev.rollczi.litecommands.intellijplugin.inspection.mapper.validation;

import java.util.function.UnaryOperator;

class RouteQuickFix implements UnaryOperator<String> {

    private final boolean multiple;

    private RouteQuickFix(boolean multiple) {
        this.multiple = multiple;
    }

    @Override
    public String apply(String rawValue) {
        if (rawValue.startsWith("\"") && rawValue.endsWith("\"")) {
            rawValue = rawValue.substring(1, rawValue.length() - 1);
        }

        String value = rawValue
                .replaceAll(" +", multiple ? " " : "")
                .trim();

        if (value.isEmpty()) {
            value = "name";
        }

        return "\"" + value + "\"";
    }

    static RouteQuickFix multiple() {
        return new RouteQuickFix(true);
    }

    static RouteQuickFix single() {
        return new RouteQuickFix(false);
    }

}
