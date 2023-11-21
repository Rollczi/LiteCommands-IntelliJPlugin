package dev.rollczi.litecommands.intellijplugin.inspection.mapper;

class Attribute {

    private final String legacyName;
    private final String modernName;
    private final AttributeType type;

    Attribute(String legacyName, String modernName, AttributeType type) {
        this.legacyName = legacyName;
        this.modernName = modernName;
        this.type = type;
    }

    AttributeType type() {
        return this.type;
    }

    String legacyName() {
        return this.legacyName;
    }

    String modernName() {
        return this.modernName;
    }

}
