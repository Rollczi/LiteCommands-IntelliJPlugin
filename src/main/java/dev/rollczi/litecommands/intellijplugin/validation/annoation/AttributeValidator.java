package dev.rollczi.litecommands.intellijplugin.validation.annoation;

import dev.rollczi.litecommands.intellijplugin.legacy.annotation.AttributeType;

import java.util.function.Predicate;

public class AttributeValidator {

    private final String name;
    private final AttributeType type;
    private final Predicate<String> validator;

    public AttributeValidator(String name, AttributeType type, Predicate<String> validator) {
        this.name = name;
        this.type = type;
        this.validator = validator;
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

}
