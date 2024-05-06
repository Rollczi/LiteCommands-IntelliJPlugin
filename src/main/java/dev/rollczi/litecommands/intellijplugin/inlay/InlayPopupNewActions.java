package dev.rollczi.litecommands.intellijplugin.inlay;

import com.intellij.codeInsight.highlighting.ReadWriteAccessDetector;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiType;
import dev.rollczi.litecommands.intellijplugin.settings.LiteCommandsConfigurable;
import dev.rollczi.litecommands.intellijplugin.settings.LiteCommandsSettings;
import dev.rollczi.litecommands.intellijplugin.usage.Usage;
import dev.rollczi.litecommands.intellijplugin.usage.UsageService;
import dev.rollczi.litecommands.intellijplugin.util.LiteTypeSearcher;
import java.awt.Point;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class InlayPopupNewActions extends BaseListPopupStep<InlayPopupNewActions.Action> {

    public InlayPopupNewActions(Point point, PsiType type, Editor editor, PsiElement element) {
        super("Actions for " + type.getPresentableText() + " Argument",
            new Action(AllIcons.Actions.FindBackward, "Find parser declarations", () -> {
                UsageService usageService = UsageService.getInstance();
                usageService.show(
                    "<b>Parsers</b> of <b>" + type.getPresentableText() + "</b> type",
                    "Parsers declarations",
                    editor,
                    element,
                    point,
                    () -> LiteTypeSearcher.findParsersByType(element.getProject(), type).stream()
                        .map(parser -> new Usage(parser, ReadWriteAccessDetector.Access.Read))
                        .toList()
                );
            }),
            new Action(AllIcons.Actions.FindForward, "Find suggester declarations", () -> {
                UsageService usageService = UsageService.getInstance();

                usageService.show(
                    "<b>Suggesters</b> of <b>" + type.getPresentableText() + "</b> type",
                    "Suggesters declarations",
                    editor,
                    element,
                    point,
                    () -> LiteTypeSearcher.findSuggestersByType(element.getProject(), type).stream()
                        .map(parser -> new Usage(parser, ReadWriteAccessDetector.Access.Read))
                        .toList()
                );
            }),
            new Action(AllIcons.Actions.Uninstall, "Hide argument hints", () -> {
                LiteCommandsConfigurable configurable = LiteCommandsConfigurable.createConfigurable();
                LiteCommandsSettings edited = configurable.getEdited();

                edited.showArgumentHints = false;
                configurable.apply();
            })
        );
    }

    @Override
    public Icon getIconFor(Action action) {
        return action.icon;
    }

    @Override
    public @NotNull String getTextFor(Action value) {
        return value.name();
    }

    @Override
    public @Nullable PopupStep<?> onChosen(Action selectedValue, boolean finalChoice) {
        selectedValue.action().run();
        return PopupStep.FINAL_CHOICE;
    }

    record Action(Icon icon, String name, Runnable action) {}

}
