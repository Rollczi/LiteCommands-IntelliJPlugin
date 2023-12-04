
package dev.rollczi.litecommands.intellijplugin.inspection.mapper.legacy;

import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.intellijplugin.inspection.mapper.AnnotationMapper;
import dev.rollczi.litecommands.intellijplugin.inspection.mapper.AttributeType;
import dev.rollczi.litecommands.intellijplugin.inspection.mapper.LegacyAnnotationInspectionTool;

public class LegacyExecuteInspection extends LegacyAnnotationInspectionTool {

    private static final AnnotationMapper MAPPER = AnnotationMapper.builder()
            .legacyName("dev.rollczi.litecommands.command.execute.Execute")
            .modernName(Execute.class.getName())
            .attribute("route", "name", AttributeType.STRING)
            .attribute("aliases", "aliases", AttributeType.ARRAY)
            .build();

    public LegacyExecuteInspection() {
        super(MAPPER);
    }

}
