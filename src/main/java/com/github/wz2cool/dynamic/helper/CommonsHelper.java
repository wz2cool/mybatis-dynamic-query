package com.github.wz2cool.dynamic.helper;

import com.github.wz2cool.dynamic.lambda.GetPropertyFunction;
import com.github.wz2cool.dynamic.model.PropertyInfo;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author Frank
 */
public class CommonsHelper {
    private static ConcurrentHashMap<String, Class> classMap = new ConcurrentHashMap<>();

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

    @SuppressWarnings("squid:S00112")
    public static <T, R> PropertyInfo getPropertyInfo(GetPropertyFunction<T, R> fn) {
        try {
            Method method = fn.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(fn);
            String methodName = serializedLambda.getImplMethodName();
            String className = serializedLambda.getImplClass();
            String propertyName;
            String getString = "get";
            String isString = "is";
            if (methodName.startsWith(getString)) {
                propertyName = java.beans.Introspector.decapitalize(methodName.substring(3));
            } else if (methodName.startsWith(isString)) {
                propertyName = java.beans.Introspector.decapitalize(methodName.substring(2));
            } else {
                propertyName = methodName;
            }

            Class ownerClass;
            if (classMap.containsKey(className)) {
                ownerClass = classMap.get(className);
            } else {
                ownerClass = Class.forName(className.replace('/', '.'));
                classMap.put(className, ownerClass);
            }

            PropertyInfo propertyInfo = new PropertyInfo();
            propertyInfo.setPropertyName(propertyName);
            propertyInfo.setOwnerClass(ownerClass);
            return propertyInfo;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
