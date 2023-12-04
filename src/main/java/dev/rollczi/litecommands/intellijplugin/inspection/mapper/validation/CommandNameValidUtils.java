package dev.rollczi.litecommands.intellijplugin.inspection.mapper.validation;

import dev.rollczi.litecommands.util.LiteCommandsUtil;

final class CommandNameValidUtils {

    private CommandNameValidUtils() {
    }
    
    static boolean validateRoute(String value) {
        try {
            return LiteCommandsUtil.checkName(value);
        }
        catch (IllegalArgumentException exception) {
            return false;
        }
    }

}
