package dev.rollczi.litecommands.intellijplugin.api.psijava;

import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.intellijplugin.api.Argument;
import dev.rollczi.litecommands.intellijplugin.api.CommandNode;
import dev.rollczi.litecommands.intellijplugin.api.ExecutorNode;
import dev.rollczi.litecommands.intellijplugin.old.annotation.AnnotationFactory;
import dev.rollczi.litecommands.intellijplugin.old.annotation.AnnotationHolder;
import java.util.ArrayList;
import java.util.List;
import panda.std.Pair;

public class PsiJavaExecutorNode extends PsiJavaAbstractNode implements ExecutorNode {

    private final PsiJavaCommandNode parent;
    private final PsiMethod psiMethod;

    public PsiJavaExecutorNode(PsiJavaCommandNode parent, PsiMethod psiMethod) {
        super(psiMethod, Execute.class, "name", "aliases");
        this.parent = parent;
        this.psiMethod = psiMethod;
    }

    @Override
    public CommandNode parent() {
        return parent;
    }

    @Override
    public List<Argument> arguments() {
        List<Argument> arguments = new ArrayList<>();

        for (PsiParameter parameter : psiMethod.getParameterList().getParameters()) {
            List<AnnotationHolder<Arg>> list = AnnotationFactory.from(Arg.class, parameter);

            if (!list.isEmpty()) {
                AnnotationHolder<Arg> holder = list.get(0);
                Arg annotation = holder.asAnnotation();

                arguments.add(new PsiJavaArgument(this, parameter, annotation.value()));
            }
        }

        return arguments;
    }

    @Override
    public Navigatable navigatable() {
        return psiMethod;
    }

}