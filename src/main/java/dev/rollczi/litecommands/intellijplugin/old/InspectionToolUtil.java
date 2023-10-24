package dev.rollczi.litecommands.intellijplugin.old;

import com.intellij.codeInspection.LocalInspectionTool;

public final class InspectionToolUtil {

    private static final String FILE_DESCRIPTION_EXCLUDE_END_NAME = "Inspection";
    private static final String FILE_DESCRIPTION_EXTENSION = ".html";

    private InspectionToolUtil() {
    }

    public static String getDescriptionFileName(Class<? extends LocalInspectionTool> inspectionToolClass) {
        String className = inspectionToolClass.getSimpleName();

        if (className.endsWith(FILE_DESCRIPTION_EXCLUDE_END_NAME)) {
            className = className.substring(0, className.length() - FILE_DESCRIPTION_EXCLUDE_END_NAME.length());
        }

        return className + FILE_DESCRIPTION_EXTENSION;
    }

}
