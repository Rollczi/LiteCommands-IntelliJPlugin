package dev.rollczi.litecommands.intellijplugin.inspection.mapper.validation;

import dev.rollczi.litecommands.util.LiteCommandsUtil;

final class CommandNameValidUtils {

    private CommandNameValidUtils() {
    }
    
    static boolean validateMultiRoute(String value) {
        try {
            return LiteCommandsUtil.checkName(value);
        }
        catch (IllegalArgumentException exception) {
            return false;
        }
    }

    static boolean validateSingleRoute(String value) {
        if (value.isEmpty()) {
            return false;
        }

        if (value.contains(" ")) {
            return false;
        }

        return true;
    }

}
