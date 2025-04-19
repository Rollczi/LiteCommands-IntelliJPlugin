package dev.rollczi.litecommands.intellijplugin.api.psijava;

import com.intellij.lang.jvm.annotation.JvmAnnotationConstantValue;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.flag.Flag;
import dev.rollczi.litecommands.annotations.join.Join;
import dev.rollczi.litecommands.annotations.literal.Literal;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import dev.rollczi.litecommands.annotations.quoted.Quoted;
import dev.rollczi.litecommands.annotations.requirement.RequirementDefinition;
import dev.rollczi.litecommands.intellijplugin.api.Argument;
import dev.rollczi.litecommands.intellijplugin.api.CommandNode;
import dev.rollczi.litecommands.intellijplugin.api.ExecutorNode;
import dev.rollczi.litecommands.intellijplugin.navigatable.NavigatableReference;
import dev.rollczi.litecommands.intellijplugin.annotation.AnnotationFactory;
import dev.rollczi.litecommands.intellijplugin.util.LiteTypeChecks;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import static java.util.Optional.*;
import java.util.stream.Stream;

public class PsiJavaExecutorNode extends PsiJavaAbstractNode implements ExecutorNode {

    private final static List<ArgumentMapper> ARGUMENTS_MAPPERS = List.of(
        // Array or Collection
        (node, parameter, annotation) -> {
            if (LiteTypeChecks.isArrayWrapper(parameter.getType(), parameter.getProject())) {
                Optional<Argument> psiJavaArgument = AnnotationFactory.from(Arg.class, annotation)
                    .map(holder -> new PsiJavaArgument(node, parameter, holder.asAnnotation().value(), PsiJavaArgument.ARRAY));

                if (psiJavaArgument.isPresent()) {
                    return psiJavaArgument;
                }

                return AnnotationFactory.from(OptionalArg.class, annotation)
                    .map(holder -> new PsiJavaArgument(node, parameter, holder.asAnnotation().value(), PsiJavaArgument.ARRAY));
            }

            return empty();
        },

        // Optional wrapper
        (node, parameter, annotation) -> {
            if (LiteTypeChecks.isOptionalWrapper(parameter.getType())) {
                return AnnotationFactory.from(Arg.class, annotation)
                    .map(holder -> new PsiJavaArgument(node, parameter, holder.asAnnotation().value(), PsiJavaArgument.OPTIONAL));
            }

            return empty();
        },
        // Optional (nullable)
        (node, parameter, annotation) -> AnnotationFactory.from(OptionalArg.class, annotation)
            .map(holder -> new PsiJavaArgument(node, parameter, holder.asAnnotation().value(), PsiJavaArgument.OPTIONAL)),

        // Quoted
        (node, parameter, annotation) -> {
            if (parameter.getAnnotation(Quoted.class.getName()) == null) {
                return empty();
            }

            return AnnotationFactory.from(Arg.class, annotation)
                .map(holder -> new PsiJavaArgument(node, parameter, holder.asAnnotation().value(), PsiJavaArgument.JOIN));
        },
        // Joined
        (node, parameter, annotation) -> AnnotationFactory.from(Join.class, annotation)
            .map(holder -> new PsiJavaArgument(node, parameter, holder.asAnnotation().value(), PsiJavaArgument.JOIN)),

        // Arg
        (node, parameter, annotation) -> AnnotationFactory.from(Arg.class, annotation)
            .map(holder -> new PsiJavaArgument(node, parameter, holder.asAnnotation().value(), PsiJavaArgument.ARG)),

        // Flag
        (node, parameter, annotation) -> AnnotationFactory.from(Flag.class, annotation)
            .map(holder -> new PsiJavaArgument(node, parameter, holder.asAnnotation().value(), PsiJavaArgument.STATIC_VALUE)),

        // Literal
        (node, parameter, annotation) -> AnnotationFactory.from(Literal.class, annotation)
            .map(holder -> {
                String[] values = holder.asAnnotation().value();
                return new PsiJavaArgument(node, parameter, values.length == 0 ? "" : values[0], PsiJavaArgument.STATIC_VALUE);
            }),

        // Any
        (node, parameter, annotation) -> {
            boolean isArray = LiteTypeChecks.isArrayWrapper(parameter.getType(), parameter.getProject());
            Optional<RequirementDefinition> definition = extractArgumentDefinition(annotation);
            if (definition.isPresent()) {
                RequirementDefinition requirementDefinition = definition.get();
                Optional<String> string = extractName(requirementDefinition, annotation);

                return of(new PsiJavaArgument(node, parameter, string.orElse(""), isArray ? PsiJavaArgument.ARRAY : PsiJavaArgument.ARG));
            }

            return empty();
        }
    );

    private static Optional<String> extractName(RequirementDefinition definition, PsiAnnotation annotation) {
        return Stream.of(definition.nameProviders())
            .map(nameProvider -> annotation.findAttribute(nameProvider))
            .filter(Objects::nonNull)
            .map(jvmAnnotationAttribute -> jvmAnnotationAttribute.getAttributeValue())
            .filter(Objects::nonNull)
            .filter(jvmAnnotationAttributeValue -> jvmAnnotationAttributeValue instanceof JvmAnnotationConstantValue)
            .map(jvmAnnotationAttributeValue -> ((JvmAnnotationConstantValue) jvmAnnotationAttributeValue).getConstantValue())
            .filter(Objects::nonNull)
            .filter(value -> value instanceof String text && !text.isBlank())
            .map(value -> (String) value)
            .findFirst();
    }

    private static Optional<RequirementDefinition> extractArgumentDefinition(PsiAnnotation annotation) {
        PsiClass type = annotation.resolveAnnotationType();
        if (type == null) {
            return Optional.empty();
        }

        Optional<RequirementDefinition> definition = AnnotationFactory.fromAsAnnotation(RequirementDefinition.class, type);
        if (definition.isEmpty()) {
            return Optional.empty();
        }

        if (definition.get().type() == RequirementDefinition.Type.ARGUMENT) {
            return definition;
        }

        return Optional.empty();
    }

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
            for (PsiAnnotation annotation : parameter.getAnnotations()) {
                for (ArgumentMapper mapper : ARGUMENTS_MAPPERS) {
                    Optional<Argument> optional = mapper.map(this, parameter, annotation);
                    if (optional.isPresent()) {
                        arguments.add(optional.get());
                        break;
                    }
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
        Optional<Argument> map(PsiJavaExecutorNode node, PsiParameter parameter, PsiAnnotation annotation);
    }

}
