package dev.rollczi.litecommands.intellijplugin.old.annotation.implementations;

import java.util.function.UnaryOperator;

class StringQuickFix implements UnaryOperator<String> {

    @Override
    public String apply(String rawValue) {
        if (rawValue.startsWith("\"") && rawValue.endsWith("\"")) {
            rawValue = rawValue.substring(1, rawValue.length() - 1);
        }

        String value = rawValue
                .replaceAll(" +", " ")
                .trim();

        if (value.isEmpty()) {
            value = "xyz";
        }

        return "\"" + value + "\"";
    }

}
