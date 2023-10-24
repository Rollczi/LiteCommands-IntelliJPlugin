package dev.rollczi.litecommands.intellijplugin.old.annotation.implemenations;

import dev.rollczi.litecommands.intellijplugin.old.annotation.AnnotationMapper;
import dev.rollczi.litecommands.intellijplugin.old.annotation.AttributeType;
import dev.rollczi.litecommands.intellijplugin.old.annotation.LegacyAnnotationInspectionTool;

public class LegacySectionInspection extends LegacyAnnotationInspectionTool {

    private static final AnnotationMapper MAPPER = AnnotationMapper.builder()
            .legacyName("dev.rollczi.litecommands.command.section.Section")
            .modernName("dev.rollczi.litecommands.command.route.Route")
            .attribute("route", "name", AttributeType.STRING)
            .attribute("aliases", "aliases", AttributeType.ARRAY)
            .build();

    public LegacySectionInspection() {
        super(MAPPER);
    }

}
