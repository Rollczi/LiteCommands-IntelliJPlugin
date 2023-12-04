
package dev.rollczi.litecommands.intellijplugin.inspection.mapper.legacy;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.intellijplugin.inspection.mapper.AnnotationMapper;
import dev.rollczi.litecommands.intellijplugin.inspection.mapper.LegacyAnnotationInspectionTool;

public class LegacyArgInspection extends LegacyAnnotationInspectionTool {

    private static final AnnotationMapper MAPPER = AnnotationMapper.builder()
            .legacyName("dev.rollczi.litecommands.argument.Arg")
            .modernName(Arg.class.getName())
            .build();

    public LegacyArgInspection() {
        super(MAPPER);
    }

}
