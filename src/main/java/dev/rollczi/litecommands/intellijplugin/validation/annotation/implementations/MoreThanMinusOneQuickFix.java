package dev.rollczi.litecommands.intellijplugin.validation.annotation.implementations;

import java.util.function.UnaryOperator;

class MoreThanMinusOneQuickFix implements UnaryOperator<String> {

    @Override
    public String apply(String rawValue) {
        rawValue = rawValue.trim();

        if (rawValue.startsWith("-")) {
            return "-1";
        }

        return rawValue;
    }

}
