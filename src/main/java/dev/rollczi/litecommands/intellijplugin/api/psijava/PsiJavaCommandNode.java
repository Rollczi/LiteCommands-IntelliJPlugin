package dev.rollczi.litecommands.intellijplugin.api.psijava;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.intellijplugin.api.CommandNode;
import dev.rollczi.litecommands.intellijplugin.api.ExecutorNode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PsiJavaCommandNode extends PsiJavaAbstractNode implements CommandNode {

    private final PsiClass psiClass;

    public PsiJavaCommandNode(PsiClass psiClass) {
        super(psiClass, Command.class, "name", "aliases");
        this.psiClass = psiClass;
    }

    @Override
    public List<ExecutorNode> executors() {
        List<ExecutorNode> executors = new ArrayList<>();

        for (PsiMethod method : psiClass.getMethods()) {
            PsiAnnotation annotation = method.getAnnotation(Execute.class.getName());

            if (annotation == null) {
                continue;
            }

            executors.add(new PsiJavaExecutorNode(this, method));
        }

        return executors;
    }

    @Override
    public boolean hasExecutors() {
        return Arrays.stream(psiClass.getMethods())
            .anyMatch(method -> method.getAnnotation(Execute.class.getName()) != null);
    }

    @Override
    public PsiFile getFile() {
        return psiClass.getContainingFile();
    }

}
