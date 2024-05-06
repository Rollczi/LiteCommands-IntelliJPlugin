package dev.rollczi.litecommands.intellijplugin.usage;

import com.intellij.find.findUsages.AbstractFindUsagesDialog;
import com.intellij.find.findUsages.FindUsagesOptions;
import com.intellij.lang.findUsages.DescriptiveNameUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.ui.SimpleColoredComponent;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.usageView.UsageViewUtil;
import org.jetbrains.annotations.NotNull;

class UsageFindUsagesDialog extends AbstractFindUsagesDialog {

    private final PsiElement myPsiElement;

    protected UsageFindUsagesDialog(@NotNull Project project, @NotNull FindUsagesOptions findUsagesOptions, PsiElement myPsiElement) {
        super(project, findUsagesOptions, false, false, false, true, true);
        this.myPsiElement = myPsiElement;
    }

    @Override
    public void configureLabelComponent(@NotNull SimpleColoredComponent coloredComponent) {
        coloredComponent.append(StringUtil.capitalize(UsageViewUtil.getType(this.myPsiElement)));
        coloredComponent.append(" ");
        coloredComponent.append(DescriptiveNameUtil.getDescriptiveName(this.myPsiElement), SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES);
    }

}
