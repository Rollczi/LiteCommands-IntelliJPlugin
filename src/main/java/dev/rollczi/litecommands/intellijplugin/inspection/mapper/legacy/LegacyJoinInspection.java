
package dev.rollczi.litecommands.intellijplugin.inspection.mapper.legacy;

import dev.rollczi.litecommands.annotations.join.Join;
import dev.rollczi.litecommands.intellijplugin.inspection.mapper.AnnotationMapper;
import dev.rollczi.litecommands.intellijplugin.inspection.mapper.LegacyAnnotationInspectionTool;

public class LegacyJoinInspection extends LegacyAnnotationInspectionTool {

    private static final AnnotationMapper MAPPER = AnnotationMapper.builder()
            .legacyName("dev.rollczi.litecommands.argument.joiner.Joiner")
            .modernName(Join.class.getName())
            .build();

    public LegacyJoinInspection() {
        super(MAPPER);
    }

}
