package dev.rollczi.litecommands.intellijplugin.template;

import com.intellij.ide.IdeView;
import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.ide.actions.CreateTemplateInPackageAction;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.InputValidatorEx;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import dev.rollczi.litecommands.intellijplugin.icon.LiteIcons;
import java.util.Arrays;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.kotlin.idea.base.projectStructure.RootKindFilter;
import org.jetbrains.kotlin.idea.base.projectStructure.RootKindMatcher;

public class LiteCommandsTemplatesAction extends CreateFileFromTemplateAction {

    public static final String TEMPLATE_JAVA = "LiteCommands Command Java";
    public static final String TEMPLATE_JAVA_ROOT = "LiteCommands Root Command Java";
    public static final String TEMPLATE_KOTLIN = "LiteCommands Command Kotlin";
    public static final String TEMPLATE_KOTLIN_ROOT = "LiteCommands Root Command Kotlin";

    @Override
    protected void buildDialog(@NotNull Project project, @NotNull PsiDirectory directory, CreateFileFromTemplateDialog.@NotNull Builder builder) {
        builder
            .setTitle("Create LiteCommands Class")
            .addKind("Command", LiteIcons.Template.Java, TEMPLATE_JAVA, new Validator())
            .addKind("Root command", LiteIcons.Template.JavaRoot, TEMPLATE_JAVA_ROOT, new Validator())
            .addKind("Command (Kotlin)", LiteIcons.Template.Kotlin, TEMPLATE_KOTLIN, new Validator())
            .addKind("Root command (Kotlin)", LiteIcons.Template.KotlinRoot, TEMPLATE_KOTLIN_ROOT, new Validator());
    }

    @Override
    public boolean isDumbAware() {
        return false;
    }

    @Override
    protected boolean isAvailable(DataContext dataContext) {
        if (!super.isAvailable(dataContext)) {
            return false;
        }

        Editor editor = CommonDataKeys.EDITOR.getData(dataContext);
        if (editor != null && editor.getSelectionModel().hasSelection()) {
            return false;
        }

        Project project = CommonDataKeys.PROJECT.getData(dataContext);
        IdeView view = LangDataKeys.IDE_VIEW.getData(dataContext);

        if (project == null || view == null || view.getDirectories().length == 0) {
            return false;
        }

        if (!project.isInitialized()) {
            return false;
        }

        ProjectFileIndex projectFileIndex = ProjectRootManager.getInstance(project).getFileIndex();

        return Arrays.stream(view.getDirectories()).anyMatch(directory -> {
            VirtualFile virtualFile = directory.getVirtualFile();
            RootKindFilter projectSources = RootKindFilter.projectSources;
            boolean isMatches = RootKindMatcher.matches(project, virtualFile, projectSources);
            boolean inContentRoot = CreateTemplateInPackageAction.isInContentRoot(virtualFile, projectFileIndex);

            return isMatches || inContentRoot;
        });
    }

    @Override
    protected @NlsContexts.Command String getActionName(PsiDirectory directory, @NonNls @NotNull String newName, @NonNls String templateName) {
        return "Create Command Class: " + newName;
    }

    private static class Validator implements InputValidatorEx {

        @Override
        public @NlsContexts.DetailedDescription @Nullable String getErrorText(@NonNls String inputString) {
            if (inputString.trim().isEmpty()) {
                return "Name cannot be empty";
            }

            if (inputString.trim().length() != inputString.length()) {
                return "Spaces at the beginning and end of the name are not allowed";
            }

            return null;
        }

    }

}
