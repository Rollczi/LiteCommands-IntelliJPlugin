package dev.rollczi.litecommands.intellijplugin.validation.annotation;

import dev.rollczi.litecommands.intellijplugin.legacy.annotation.AttributeType;

import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class AttributeValidator {

    private final String name;
    private final AttributeType type;
    private final Predicate<String> validator;
    private final UnaryOperator<String> quickFix;

    public AttributeValidator(String name, AttributeType type, Predicate<String> validator, UnaryOperator<String> quickFix) {
        this.name = name;
        this.type = type;
        this.validator = validator;
        this.quickFix = quickFix;
    }

    public String name() {
        return this.name;
    }

    public AttributeType type() {
        return this.type;
    }

    public boolean validate(String value) {
        return this.validator.test(value);
    }

    public String quickFix(String value) {
        return this.quickFix.apply(value);
    }

}
