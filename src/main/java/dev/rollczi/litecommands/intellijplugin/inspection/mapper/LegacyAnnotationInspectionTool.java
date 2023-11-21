package dev.rollczi.litecommands.intellijplugin.inspection.mapper;

import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public abstract class LegacyAnnotationInspectionTool extends AbstractBaseJavaLocalInspectionTool {

    private static final String DISPLAY_NAME = "Usage of @%s is not recommended. Use @%s instead";

    private final AnnotationMapper mapper;

    protected LegacyAnnotationInspectionTool(AnnotationMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public @NotNull String getDisplayName() {
        return String.format(DISPLAY_NAME, this.mapper.legacyName(), this.mapper.modernName());
    }

    @Override
    public @NotNull String getGroupDisplayName() {
        return "LiteCommands";
    }

    @Override
    public boolean isEnabledByDefault() {
        return true;
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new AnnotationVisitor(this.getDisplayName(), this.mapper, holder);
    }

}
