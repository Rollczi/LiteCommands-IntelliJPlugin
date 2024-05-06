package dev.rollczi.litecommands.intellijplugin.usage;

import com.intellij.find.actions.ShowUsagesManager;
import com.intellij.find.actions.ShowUsagesParameters;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IntRef;
import com.intellij.psi.PsiElement;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.usages.impl.UsageViewPopupManager;
import java.awt.Point;

@Service
public final class UsageService {

    public void show(String title, String optionsString, Editor editor, PsiElement element, Point point, UsageProvider usageProvider) {
        Project project = element.getProject();
        ShowUsagesManager manager = ShowUsagesManager.getInstance(project);
        UsageViewPopupManager service = project.getService(UsageViewPopupManager.class);

        manager.showElementUsagesWithResult(
            new ShowUsagesParameters(
                project,
                editor,
                new RelativePoint(point),
                new IntRef(100),
                100
            ),
            new UsageActionHandler(title, optionsString, element, usageProvider),
            service.createUsageViewPopup(element.getLanguage())
        );

    }

    public static UsageService getInstance() {
        return ApplicationManager.getApplication().getService(UsageService.class);
    }

}
