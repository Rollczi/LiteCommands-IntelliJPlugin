
package dev.rollczi.litecommands.intellijplugin.inspection.mapper.legacy;

import dev.rollczi.litecommands.annotations.permission.Permission;
import dev.rollczi.litecommands.intellijplugin.inspection.mapper.AnnotationMapper;
import dev.rollczi.litecommands.intellijplugin.inspection.mapper.AttributeType;
import dev.rollczi.litecommands.intellijplugin.inspection.mapper.LegacyAnnotationInspectionTool;

public class LegacyPermissionInspection extends LegacyAnnotationInspectionTool {

    private static final AnnotationMapper MAPPER = AnnotationMapper.builder()
            .legacyName("dev.rollczi.litecommands.command.permission.Permission")
            .modernName(Permission.class.getName())
            .attribute("value", "value", AttributeType.ARRAY)
            .build();

    public LegacyPermissionInspection() {
        super(MAPPER);
    }

}
