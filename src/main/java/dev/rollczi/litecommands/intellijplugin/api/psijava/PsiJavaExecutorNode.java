package dev.rollczi.litecommands.intellijplugin.api.psijava;

import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.flag.Flag;
import dev.rollczi.litecommands.annotations.join.Join;
import dev.rollczi.litecommands.annotations.quoted.Quoted;
import dev.rollczi.litecommands.intellijplugin.api.Argument;
import dev.rollczi.litecommands.intellijplugin.api.CommandNode;
import dev.rollczi.litecommands.intellijplugin.api.ExecutorNode;
import dev.rollczi.litecommands.intellijplugin.navigatable.NavigatableReference;
import dev.rollczi.litecommands.intellijplugin.annotation.AnnotationFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;
import panda.std.Option;

public class PsiJavaExecutorNode extends PsiJavaAbstractNode implements ExecutorNode {

    private static final Set<String> OPTIONAL_WRAPPERS = Set.of(
        Optional.class.getName(),
        OptionalInt.class.getName(),
        OptionalDouble.class.getName(),
        OptionalLong.class.getName(),
        Option.class.getName()
    );

    private final static List<ArgumentMapper> ARGUMENTS_MAPPERS = List.of(
        (node, parameter) -> {
            for (String optionalWrapper : OPTIONAL_WRAPPERS) {
                if (!parameter.getType().getCanonicalText().startsWith(optionalWrapper)) {
                    continue;
                }

                return AnnotationFactory.from(Arg.class, parameter).stream()
                    .findFirst()
                    .map(holder -> new PsiJavaArgument(node, parameter, holder.asAnnotation().value(), PsiJavaArgument.OPTIONAL));
            }

            return Optional.empty();
        },

        (node, parameter) -> {
            if (parameter.getAnnotation(Quoted.class.getName()) == null) {
                return Optional.empty();
            }

            return AnnotationFactory.from(Arg.class, parameter).stream()
                .findFirst()
                .map(holder -> new PsiJavaArgument(node, parameter, holder.asAnnotation().value(), PsiJavaArgument.JOIN));
        },

        (node, parameter) -> AnnotationFactory.from(Arg.class, parameter).stream()
            .findFirst()
            .map(holder -> new PsiJavaArgument(node, parameter, holder.asAnnotation().value(), PsiJavaArgument.ARG)),

        (node, parameter) -> AnnotationFactory.from(Flag.class, parameter).stream()
            .findFirst()
            .map(holder -> new PsiJavaArgument(node, parameter, holder.asAnnotation().value(), PsiJavaArgument.FLAG)),

        (node, parameter) -> AnnotationFactory.from(Join.class, parameter).stream()
            .findFirst()
            .map(holder -> new PsiJavaArgument(node, parameter, holder.asAnnotation().value(), PsiJavaArgument.JOIN))
    );

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
            for (ArgumentMapper mapper : ARGUMENTS_MAPPERS) {
                Optional<Argument> optional = mapper.map(this, parameter);

                if (optional.isPresent()) {
                    arguments.add(optional.get());
                    break;
                }
            }
        }

        return arguments;
    }

    @Override
    public NavigatableReference navigatable() {
        return NavigatableReference.ofPsiElement(() -> psiMethod);
    }

    interface ArgumentMapper {
        Optional<Argument> map(PsiJavaExecutorNode node, PsiParameter parameter);
    }

}
