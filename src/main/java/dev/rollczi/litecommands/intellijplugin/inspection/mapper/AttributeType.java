package dev.rollczi.litecommands.intellijplugin.inspection.mapper;

import java.util.function.Predicate;

public enum AttributeType {
    STRING,
    INT,
    LONG,
    FLOAT,
    DOUBLE,
    BOOLEAN,
    CHAR,
    BYTE,
    SHORT,
    CLASS,
    ENUM,
    ANNOTATION,
    ARRAY(value -> !value.equals("{}"));

    private final Predicate<String> predicate;

    AttributeType() {
        this.predicate = value -> true;
    }

    AttributeType(Predicate<String> predicate) {
        this.predicate = predicate;
    }

    public boolean matches(String value) {
        return this.predicate.test(value);
    }

}
