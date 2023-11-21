package dev.rollczi.litecommands.intellijplugin.inspection.mapper.legacy;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.intellijplugin.inspection.mapper.AnnotationMapper;
import dev.rollczi.litecommands.intellijplugin.inspection.mapper.AttributeType;
import dev.rollczi.litecommands.intellijplugin.inspection.mapper.LegacyAnnotationInspectionTool;

public class LegacyRouteInspection extends LegacyAnnotationInspectionTool {

    private static final AnnotationMapper MAPPER = AnnotationMapper.builder()
            .legacyName("dev.rollczi.litecommands.command.route.Route")
            .modernName(Command.class.getName())
            .attribute("name", "name", AttributeType.STRING)
            .attribute("aliases", "aliases", AttributeType.ARRAY)
            .build();

    public LegacyRouteInspection() {
        super(MAPPER);
    }

}
