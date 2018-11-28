package com.github.wz2cool.dynamic.helper;

import com.github.wz2cool.dynamic.lambda.GetPropertyFunction;
import com.github.wz2cool.dynamic.model.PropertyInfo;
import jodd.methref.Methref;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

/**
 * Created by Frank on 7/7/2017.
 */
public class CommonsHelper {
    private CommonsHelper() {
        throw new UnsupportedOperationException();
    }

    public static boolean isNumeric(final Class targetClass) {
        if (targetClass == null) {
            return false;
        }

        return Number.class.isAssignableFrom(targetClass);
    }

    public static boolean isArrayOrCollection(final Object value) {
        return isArray(value) || isCollection(value);
    }

    public static boolean isArray(final Object value) {
        return value != null && value.getClass().isArray();
    }

    public static boolean isCollection(final Object value) {
        return value instanceof Iterable;
    }

    public static Object[] getCollectionValues(final Object inputValue) {
        if (inputValue == null) {
            throw new NullPointerException("inputValue");
        }

        if (!isArrayOrCollection(inputValue)) {
            throw new IllegalArgumentException("inputValue should be array or collection");
        }

        Collection<Object> values = new ArrayList<>();
        if (inputValue instanceof Iterable) {
            Iterable iterable = (Iterable) inputValue;
            for (Object value : iterable) {
                values.add(value);
            }
        } else {
            int length = Array.getLength(inputValue);
            for (int i = 0; i < length; i++) {
                Object value = Array.get(inputValue, i);
                values.add(value);
            }
        }

        return values.toArray();
    }

    public static String toStringSafe(final Object obj) {
        if (obj == null) {
            return "";
        }

        return obj.toString();
    }

    public static <T> PropertyInfo getPropertyInfo(GetPropertyFunction<T> fn) {
        try {
            Method method = fn.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(fn);
            String methodName = serializedLambda.getImplMethodName();
            String className = serializedLambda.getImplClass();
            String propertyType = serializedLambda.getImplMethodSignature();
            String propertyName;
            if (methodName.startsWith("get")) {
                propertyName = java.beans.Introspector.decapitalize(methodName.substring(3));
            } else if (methodName.startsWith("is")) {
                propertyName = java.beans.Introspector.decapitalize(methodName.substring(2));
            } else {
                propertyName = methodName;
            }
            PropertyInfo propertyInfo = new PropertyInfo();
            propertyInfo.setPropertyName(propertyName);
            propertyInfo.setClassName(className);
            propertyInfo.setPropertyType(propertyType);
            return propertyInfo;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> String getPropertyInfo(final Class<T> target, final Function<T, Object> getMethodFunc) {
        String methodName = obtainGetMethodName(target, getMethodFunc);
        if (methodName.startsWith("get")) {
            return java.beans.Introspector.decapitalize(methodName.substring(3));
        } else if (methodName.startsWith("is")) {
            return java.beans.Introspector.decapitalize(methodName.substring(2));
        }

        return methodName;
    }

    public static <T> String obtainGetMethodName(final Class<T> target, final Function<T, Object> getMethodFunc) {
        Methref<T> methodRef = Methref.on(target);
        getMethodFunc.apply(methodRef.to());
        return methodRef.ref();
    }
}