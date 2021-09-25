package com.github.wz2cool.dynamic.helper;

import com.github.wz2cool.dynamic.exception.InternalRuntimeException;
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
    private final static String STRING_SPLIT = "%s";
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

    public static <T, R extends Comparable> String getPropertyName(GetPropertyFunction<T, R> fn) {
        return getPropertyInfo(fn).getPropertyName();
    }

    @SuppressWarnings("squid:S00112")
    public static <T, R extends Comparable> PropertyInfo getPropertyInfo(GetPropertyFunction<T, R> fn) {
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
            throw new InternalRuntimeException(e);
        }
    }


    /**
     * 字符串简单替换,只处理%s类型的
     * pattern中的%s和arguments数量尽量保持一致,arguments必须大于pattern中的%s
     *
     * @param pattern   根据%s去替换arguments
     * @param arguments %s的替换值
     * @return String
     */
    public static String format(String pattern, String... arguments) {
        if (arguments.length == 0) {
            return pattern;
        }
        String[] split = pattern.split(STRING_SPLIT);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            stringBuilder.append(split[i]).append(arguments[i]);
        }
        return stringBuilder.toString();
    }
}