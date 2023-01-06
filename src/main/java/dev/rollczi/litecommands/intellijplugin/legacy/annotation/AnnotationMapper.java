package dev.rollczi.litecommands.intellijplugin.legacy.annotation;

import java.util.ArrayList;
import java.util.List;

public final class AnnotationMapper {

    private final String legacyName;
    private final String modernName;
    private final List<Attribute> attributes;

    private AnnotationMapper(String legacyName, String modernName, List<Attribute> attributes) {
        this.legacyName = legacyName;
        this.modernName = modernName;
        this.attributes = attributes;
    }

    public String legacyQualified() {
        return this.legacyName;
    }

    public String modernQualified() {
        return this.modernName;
    }

    public String legacyName() {
        return this.toShortName(this.legacyName);
    }

    public String modernName() {
        return this.toShortName(this.modernName);
    }

    public String legacyAnnotation() {
        return "@" + this.legacyName();
    }

    public String modernAnnotation() {
        return "@" + this.modernName();
    }

    private String toShortName(String name) {
        return name.substring(name.lastIndexOf('.') + 1);
    }

    public List<Attribute> getAttributes() {
        return this.attributes;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String legacyName;
        private String modernName;
        private final List<Attribute> attributes = new ArrayList<>();

        public Builder legacyName(String legacyName) {
            this.legacyName = legacyName;
            return this;
        }

        public Builder modernName(String modernName) {
            this.modernName = modernName;
            return this;
        }

        public Builder attribute(String legacy, String modern, AttributeType type) {
            this.attributes.add(new Attribute(legacy, modern, type));
            return this;
        }

        public AnnotationMapper build() {
            return new AnnotationMapper(this.legacyName, this.modernName, this.attributes);
        }

    }

}
