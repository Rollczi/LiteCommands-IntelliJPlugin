package dev.rollczi.litecommands.intellijplugin.usage;

import com.intellij.find.actions.ShowUsagesActionHandler;
import com.intellij.find.actions.ShowUsagesParameters;
import com.intellij.find.findUsages.FindUsagesOptions;
import com.intellij.internal.statistic.eventLog.events.EventPair;
import com.intellij.internal.statistic.eventLog.events.ObjectEventData;
import com.intellij.lang.Language;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.impl.EditorHistoryManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.usageView.UsageInfo;
import com.intellij.usages.UsageSearchPresentation;
import com.intellij.usages.UsageSearcher;
import com.intellij.usages.impl.UsageViewStatisticsCollector;
import com.intellij.util.containers.ContainerUtil;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class UsageActionHandler implements ShowUsagesActionHandler {

    private final String title;
    private final String optionsString;
    private final PsiElement element;
    private final UsageProvider usageProvider;

    public UsageActionHandler(String title, String optionsString, PsiElement element, UsageProvider usageProvider) {
        this.title = title;
        this.optionsString = optionsString;
        this.element = element;
        this.usageProvider = usageProvider;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public @NotNull UsageSearchPresentation getPresentation() {
        return new UsageSearchPresentation() {
            @Override
            public @Nls @NotNull String getSearchTargetString() {
                return title;
            }

            @Override
            public @Nls @NotNull String getOptionsString() {
                return optionsString;
            }
        };
    }

    @Override
    public @NotNull UsageSearcher createUsageSearcher() {
        return processor -> ApplicationManager.getApplication().runReadAction(() -> {
            for (Usage usage : usageProvider.getUsages()) {
                processor.process(usage);
            }
        });
    }

    @Override
    public void findUsages() {

    }

    @Override
    public @Nullable ShowUsagesActionHandler showDialog() {
        Project project = element.getProject();
        UsageFindUsagesDialog liteFindUsagesDialog = new UsageFindUsagesDialog(project, new FindUsagesOptions(project), element);

        liteFindUsagesDialog.showAndGet();
        return null;
    }

    @Override
    public @Nullable ShowUsagesActionHandler withScope(@NotNull SearchScope searchScope) {
        return null;
    }

    @Override
    public @Nullable ShowUsagesParameters moreUsages(@NotNull ShowUsagesParameters showUsagesParameters) {
        return null;
    }

    @Override
    public @NotNull SearchScope getSelectedScope() {
        return GlobalSearchScope.allScope(element.getProject());
    }

    @Override
    public @NotNull SearchScope getMaximalScope() {
        return GlobalSearchScope.allScope(element.getProject());
    }

    @Override
    public @Nullable Language getTargetLanguage() {
        return element.getLanguage();
    }

    @Override
    public @NotNull Class<?> getTargetClass() {
        return element.getClass();
    }

    @Override
    public @NotNull List<EventPair<?>> getEventData() {
        return new ArrayList<>();
    }

    @Override
    public @NotNull List<EventPair<?>> buildFinishEventData(@Nullable UsageInfo selectedUsage) {
        return new ArrayList<>();
    }

    @Override
    public void beforeClose(@NonNls String s) {
    }

    @Override
    public boolean navigateToSingleUsageImmediately() {
        return false;
    }

}
