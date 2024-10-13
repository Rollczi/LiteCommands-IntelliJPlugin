package dev.rollczi.litecommands.intellijplugin.util;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInsight.intention.impl.JavaElementActionsFactory;
import com.intellij.lang.Language;
import com.intellij.lang.jvm.JvmModifiersOwner;
import com.intellij.lang.jvm.actions.AnnotationAttributeRequest;
import com.intellij.lang.jvm.actions.AnnotationAttributeValueRequest;
import com.intellij.lang.jvm.actions.AnnotationRequest;
import com.intellij.lang.jvm.actions.JvmElementActionsFactory;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.impl.ImaginaryEditor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiArrayInitializerMemberValue;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.PsiModifierListOwner;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.idea.quickfix.crossLanguage.KotlinElementActionsFactory;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UastContextKt;

@SuppressWarnings("UnstableApiUsage")
public class PsiAnnotationUtil {

    private PsiAnnotationUtil() {
    }

    public static List<PsiAnnotation> getAnnotations(PsiModifierListOwner owner, Class<? extends Annotation> annotationClass) {
        return getAnnotations(owner, annotationClass.getName());
    }

    public static List<PsiAnnotation> getAnnotations(PsiModifierListOwner owner, String annotationName) {
        return Stream.of(owner.getAnnotations())
            .filter(annotation -> Objects.equals(annotation.getQualifiedName(), annotationName))
            .collect(Collectors.toList());
    }

    public static List<PsiValue<String>> getStringArray(PsiAnnotation annotation, String attributeName) {
        PsiAnnotationMemberValue attributeValue = annotation.findDeclaredAttributeValue(attributeName);

        if (attributeValue instanceof PsiArrayInitializerMemberValue arrayInitializerMemberValue) {
            List<PsiValue<String>> values = new ArrayList<>();

            for (PsiAnnotationMemberValue initializer : arrayInitializerMemberValue.getInitializers()) {
                if (initializer instanceof PsiLiteralExpression literalExpression) {
                    String value = ReadAction.compute(() -> (String) literalExpression.getValue());
                    values.add(new PsiValue<>(literalExpression, value));
                }
            }

            return values;
        }

        if (attributeValue instanceof PsiLiteralExpression literalExpression) {
            Object value = literalExpression.getValue();

            if (value instanceof String) {
                return List.of(new PsiValue<>(literalExpression, (String) value));
            }

            if (value instanceof List<?> list) {
                return list.stream()
                    .filter(o -> o instanceof String)
                    .map(o -> new PsiValue<>(literalExpression, (String) o))
                    .collect(Collectors.toList());
            }

            return List.of();
        }

        return List.of();
    }


    public static void setString(PsiAnnotation annotation, String attributeName, String newValue) {
        Project project = annotation.getProject();
        Document document = PsiDocumentManager.getInstance(project).getDocument(annotation.getContainingFile());
        UElement element =  UastContextKt.toUElement(annotation.findDeclaredAttributeValue(attributeName));

        if (document == null) {
            throw new RuntimeException("Document not found");
        }

        JvmElementActionsFactory factory = getFactory(element.getLang());
        List<IntentionAction> actionList = factory.createChangeAnnotationAttributeActions(
            annotation,
            -1,
            new AnnotationAttributeRequest(
                attributeName,
                new AnnotationAttributeValueRequest.StringValue(newValue)
            )
            , "Test", "Test");

        for (IntentionAction intentionAction : actionList) {
            intentionAction.invoke(project, new ImaginaryEditor(project, document), annotation.getContainingFile());
        }
    }

    public static void setStringArray(PsiAnnotation annotation, String attributeName, Collection<String> list) {
        Project project = annotation.getProject();
        Document document = PsiDocumentManager.getInstance(project).getDocument(annotation.getContainingFile());

        if (document == null) {
            throw new RuntimeException("Document not found");
        }

        JvmElementActionsFactory factory = getFactory(annotation);
        List<IntentionAction> actionList = factory.createChangeAnnotationAttributeActions(
            annotation,
            -1,
            new AnnotationAttributeRequest(
                attributeName,
                new AnnotationAttributeValueRequest.ArrayValue(
                    list.stream()
                        .map(s -> new AnnotationAttributeValueRequest.StringValue(s))
                        .collect(Collectors.toList())
                )
            )
            , "Test", "Test");

        for (IntentionAction intentionAction : actionList) {
            intentionAction.invoke(project, new ImaginaryEditor(project, document), annotation.getContainingFile());
        }
    }

    public static JvmElementActionsFactory getFactory(PsiElement psiElement) {
        UElement element =  UastContextKt.toUElement(psiElement);

        return getFactory(element.getLang());
    }

    private static JvmElementActionsFactory getFactory(Language lang) {
        if (lang.getID().toLowerCase(Locale.ROOT).equals("kotlin")) {
            return new KotlinElementActionsFactory();
        }

        return new JavaElementActionsFactory();
    }

    public static void addAnnotation(String qName, PsiModifierListOwner owner, AnnotationAttributeRequest... attributes) {
        JvmElementActionsFactory factory = getFactory(owner);

        if (!(owner instanceof JvmModifiersOwner jvmModifiersOwner)) {
            throw new RuntimeException("Owner is not JvmModifiersOwner");
        }

        Project project = owner.getProject();
        Document document = PsiDocumentManager.getInstance(project).getDocument(owner.getContainingFile());

        if (document == null) {
            throw new RuntimeException("Document not found");
        }

        List<IntentionAction> actions = factory.createAddAnnotationActions(jvmModifiersOwner, new CreateRequest(qName, attributes));

        for (IntentionAction action : actions) {
            action.invoke(owner.getProject(), new ImaginaryEditor(project, document), owner.getContainingFile());
        }
    }

    private static class CreateRequest implements AnnotationRequest {

        private final String qualifiedName;
        private final List<AnnotationAttributeRequest> attributes = new ArrayList<>();

        public CreateRequest(String qualifiedName, AnnotationAttributeRequest... attributes) {
            this.qualifiedName = qualifiedName;
            this.attributes.addAll(List.of(attributes));
        }

        @Override
        public @NotNull String getQualifiedName() {
            return qualifiedName;
        }

        @Override
        public @NotNull List<AnnotationAttributeRequest> getAttributes() {
            return attributes;
        }

        @Override
        public boolean isValid() {
            return true;
        }
    }


}
