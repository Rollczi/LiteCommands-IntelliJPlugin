package dev.rollczi.litecommands.intellijplugin.validation.annoation.implementations;

final class CommandNameValidUtils {

    private CommandNameValidUtils() {
    }


    static boolean validateArray(String value) {
        if (value.startsWith("{") && value.endsWith("}")) {
            value = value.substring(1, value.length() - 1);
        }

        value = value.trim();

        if (value.isEmpty()) {
            return true;
        }

        String[] values = value.split(",");

        for (String val : values) {
            if (!validateString(val)) {
                return false;
            }
        }

        return true;
    }

    static boolean validateString(String value) {
        value = value.trim();

        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }

        if (value.isEmpty()) {
            return false;
        }

        if (value.startsWith(" ") || value.endsWith(" ")) {
            return false;
        }

        boolean space = false;
        for (char letter : value.toCharArray()) {
            if (letter == ' ') {
                if (space) {
                    return false;
                }

                space = true;
                continue;
            }

            space = false;
        }

        return true;
    }

}
