package dev.rollczi.litecommands.intellijplugin.old.annotation.implementations;

final class CommandNameValidUtils {

    private CommandNameValidUtils() {
    }
    
    static boolean validateString(String value) {
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
