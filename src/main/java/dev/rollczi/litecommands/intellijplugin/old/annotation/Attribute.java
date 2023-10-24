package dev.rollczi.litecommands.intellijplugin.old.annotation;

public class Attribute {

    private final String legacyName;
    private final String modernName;
    private final AttributeType type;

    public Attribute(String legacyName, String modernName, AttributeType type) {
        this.legacyName = legacyName;
        this.modernName = modernName;
        this.type = type;
    }

    public AttributeType type() {
        return this.type;
    }

    public String legacyName() {
        return this.legacyName;
    }

    public String modernName() {
        return this.modernName;
    }

}
