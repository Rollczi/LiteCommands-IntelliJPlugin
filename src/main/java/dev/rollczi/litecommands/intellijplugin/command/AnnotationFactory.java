package dev.rollczi.litecommands.intellijplugin.command;

import com.intellij.lang.jvm.JvmAnnotation;
import com.intellij.lang.jvm.annotation.JvmAnnotationArrayValue;
import com.intellij.lang.jvm.annotation.JvmAnnotationAttribute;
import com.intellij.lang.jvm.annotation.JvmAnnotationAttributeValue;
import com.intellij.lang.jvm.annotation.JvmAnnotationClassValue;
import com.intellij.lang.jvm.annotation.JvmAnnotationConstantValue;
import com.intellij.lang.jvm.annotation.JvmAnnotationEnumFieldValue;
import com.intellij.lang.jvm.annotation.JvmNestedAnnotationValue;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiModifierListOwner;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AnnotationFactory {

    public static <A extends Annotation> List<A> fromAnnotatedPsi(Class<A> annotationClass, PsiModifierListOwner element) {
        return Arrays.stream(element.getAnnotations())
            .filter(psiAnnotation -> psiAnnotation.hasQualifiedName(annotationClass.getName()))
            .flatMap(psiAnnotation -> fromPsiAnnotation(annotationClass, psiAnnotation).stream())
            .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public static <A extends Annotation> Optional<A> fromPsiAnnotation(Class<A> annotationClass, PsiAnnotation psiAnnotation) {
        try {
            A annotation = (A) Proxy.newProxyInstance(
                AnnotationFactory.class.getClassLoader(),
                new Class[]{annotationClass},
                new PsiAnnotationInvocationHandler<>(annotationClass, psiAnnotation)
            );

            if (checkAllAttributes(annotation)) {
                return Optional.of(annotation);
            }

            return Optional.empty();
        }
        catch (PsiAnnotationCreateException createException) {
            return Optional.empty();
        }
    }

    private static boolean checkAllAttributes(Annotation annotation) {
        for (Method method : annotation.annotationType().getDeclaredMethods()) {
            Parameter[] parameters = method.getParameters();

            if (parameters.length != 0) {
                continue;
            }

            try {
                method.invoke(annotation);
            }
            catch (InvocationTargetException invocationTargetException) {
                if (isNotCorrectlyFilled(invocationTargetException)) {
                    return false;
                }

                throw new RuntimeException(invocationTargetException);
            }
            catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        }

        return true;
    }

    private static boolean isNotCorrectlyFilled(InvocationTargetException exception) {
        Throwable targetException = exception.getTargetException();

        if (targetException instanceof UndeclaredThrowableException) {
            UndeclaredThrowableException undeclaredThrowable = (UndeclaredThrowableException) targetException;
            Throwable throwable = undeclaredThrowable.getUndeclaredThrowable();

            if (throwable instanceof IllegalArgumentException) {
                return true;
            }

            return throwable instanceof NoSuchMethodException;
        }

        return false;
    }

    private static final class PsiAnnotationInvocationHandler<A extends Annotation> implements InvocationHandler {

        private final Class<A> annotationClass;
        private final PsiAnnotation annotation;

        public PsiAnnotationInvocationHandler(Class<A> annotationClass, PsiAnnotation annotation) {
            this.annotationClass = annotationClass;
            this.annotation = annotation;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String name = method.getName();
            if (name.equals("annotationType")) {
                return annotationClass;
            }

            JvmAnnotationAttribute attribute = annotation.findAttribute(name);

            if (attribute == null || attribute.getAttributeValue() == null) {
                Method annotationAttribute = annotationClass.getDeclaredMethod(name);

                Object defaultValue = annotationAttribute.getDefaultValue();

                if (defaultValue != null) {
                    return defaultValue;
                }

                throw new NoSuchMethodException("Attribute " + name + " not found in annotation " + annotationClass.getName());
            }

            return getRealValue(attribute.getAttributeValue(), method.getReturnType());
        }

        private Object getRealValue(JvmAnnotationAttributeValue attributeValue, Class<?> basedType) {
            if (attributeValue instanceof JvmAnnotationConstantValue) {
                if (basedType.isArray()) {
                    Object array = Array.newInstance(basedType.componentType(), 1);
                    Object constantValue = ((JvmAnnotationConstantValue) attributeValue).getConstantValue();

                    if (!basedType.componentType().isInstance(constantValue)) {
                        throw new IllegalArgumentException("Can not inject " + attributeValue + " as " + basedType.componentType());
                    }

                    Array.set(array, 0, constantValue);
                    return array;
                }

                return ((JvmAnnotationConstantValue) attributeValue).getConstantValue();
            }

            if (attributeValue instanceof JvmAnnotationArrayValue) {
                if (!basedType.isArray()) {
                    throw new IllegalArgumentException("Can not compile " + basedType + " from " + attributeValue);
                }

                Object[] objects = ((JvmAnnotationArrayValue) attributeValue).getValues().stream()
                    .map(value -> getRealValue(value, basedType.componentType()))
                    .toArray();

                Object typedArray = Array.newInstance(basedType.componentType(), objects.length);

                for (int i = 0; i < objects.length; i++) {
                    Array.set(typedArray, i, objects[i]);
                }

                return typedArray;
            }

            if (attributeValue instanceof JvmAnnotationEnumFieldValue) {
                JvmAnnotationEnumFieldValue enumFieldValue = (JvmAnnotationEnumFieldValue) attributeValue;

                try {
                    Class enumClass = Class.forName(enumFieldValue.getContainingClass().getQualifiedName());

                    return Enum.valueOf(enumClass, enumFieldValue.getFieldName());
                }
                catch (ClassNotFoundException exception) {
                    throw new RuntimeException(exception);
                }
            }

            if (attributeValue instanceof JvmAnnotationClassValue) {
                JvmAnnotationClassValue classValue = (JvmAnnotationClassValue) attributeValue;

                try {
                    return Class.forName(classValue.getQualifiedName());
                }
                catch (ClassNotFoundException exception) {
                    throw new RuntimeException(exception);
                }
            }

            if (attributeValue instanceof JvmNestedAnnotationValue) {
                JvmNestedAnnotationValue nestedAnnotationValue = (JvmNestedAnnotationValue) attributeValue;
                JvmAnnotation nestedAnnotation = nestedAnnotationValue.getValue();

                if (nestedAnnotation instanceof PsiAnnotation) {
                    try {
                        Class annotationClass = Class.forName(nestedAnnotation.getQualifiedName());

                        return fromPsiAnnotation(annotationClass, (PsiAnnotation) nestedAnnotation)
                            .orElseThrow(() -> new PsiAnnotationCreateException("Cannot create annotation for " + nestedAnnotation.getClass().getName()));
                    } catch (Throwable exception) {
                        throw new RuntimeException(exception);
                    }
                }
            }

            throw new RuntimeException("Unsupported attribute value type: " + attributeValue.getClass().getName());
        }

    }

    private static class PsiAnnotationCreateException extends RuntimeException {

        public PsiAnnotationCreateException(String message) {
            super(message);
        }

    }

}
