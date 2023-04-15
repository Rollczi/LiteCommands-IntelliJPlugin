package dev.rollczi.litecommands.intellijplugin.validation.annotation;

import dev.rollczi.litecommands.intellijplugin.legacy.annotation.AttributeType;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public final class AnnotationValidator {

    private final String name;
    private final Set<AttributeValidator> attributeValidators;

    private AnnotationValidator(String name, Set<AttributeValidator> attributeValidators) {
        this.name = name;
        this.attributeValidators = attributeValidators;
    }

    public String qualifiedName() {
        return this.name;
    }

    public String name() {
        return this.name.substring(this.name.lastIndexOf('.') + 1);
    }

    public String annotation() {
        return "@" + this.name();
    }

    public Set<AttributeValidator> getAttributesValidators() {
        return this.attributeValidators;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String name;
        private final Set<AttributeValidator> attributeValidators = new HashSet<>();

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder attribute(String modern, AttributeType type, Predicate<String> validator, UnaryOperator<String> quickFix) {
            this.attributeValidators.add(new AttributeValidator(modern, type, validator, quickFix));
            return this;
        }

        public AnnotationValidator build() {
            return new AnnotationValidator(this.name, this.attributeValidators);
        }

    }

}
