package dev.rollczi.litecommands.intellijplugin.usage;

import com.intellij.codeInsight.highlighting.ReadWriteAccessDetector;
import com.intellij.psi.PsiElement;
import com.intellij.usageView.UsageInfo;
import com.intellij.usages.ReadWriteAccessUsageInfo2UsageAdapter;

public class Usage extends ReadWriteAccessUsageInfo2UsageAdapter {

    public Usage(UsageInfo usageInfo, ReadWriteAccessDetector.Access rwAccess) {
        super(usageInfo, rwAccess);
    }

    public Usage(PsiElement usageInfo, ReadWriteAccessDetector.Access rwAccess) {
        super(new UsageInfo(usageInfo), rwAccess);
    }

}
