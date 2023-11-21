package dev.rollczi.litecommands.intellijplugin.inspection.mapper;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public final class AnnotationMapperSchema {

    private final String name;
    private final Set<AnnotationMapperAttributeSchema> annotationMapperAttributeSchemas;

    private AnnotationMapperSchema(String name, Set<AnnotationMapperAttributeSchema> annotationMapperAttributeSchemas) {
        this.name = name;
        this.annotationMapperAttributeSchemas = annotationMapperAttributeSchemas;
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

    public Set<AnnotationMapperAttributeSchema> getAttributesValidators() {
        return this.annotationMapperAttributeSchemas;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String name;
        private final Set<AnnotationMapperAttributeSchema> annotationMapperAttributeSchemas = new HashSet<>();

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder attribute(String modern, AttributeType type, Predicate<String> validator, UnaryOperator<String> quickFix) {
            this.annotationMapperAttributeSchemas.add(new AnnotationMapperAttributeSchema(modern, type, validator, quickFix));
            return this;
        }

        public AnnotationMapperSchema build() {
            return new AnnotationMapperSchema(this.name, this.annotationMapperAttributeSchemas);
        }

    }

}
