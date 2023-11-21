package dev.rollczi.litecommands.intellijplugin.inspection.mapper.legacy;

import dev.rollczi.litecommands.intellijplugin.inspection.mapper.AnnotationMapper;
import dev.rollczi.litecommands.intellijplugin.inspection.mapper.AttributeType;
import dev.rollczi.litecommands.intellijplugin.inspection.mapper.LegacyAnnotationInspectionTool;

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
