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


    private static final ConcurrentHashMap<GetPropertyFunction, PropertyInfo> classMap = new ConcurrentHashMap<>(256);
    private static final String GET = "get";
    private static final String IS = "is";
    private static final String WRITE_REPLACE = "writeReplace";


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
            PropertyInfo propertyInfo = classMap.get(fn);
            // First fetch it from cache
            if (propertyInfo != null) {
                return propertyInfo;
            }

            Method method = fn.getClass().getDeclaredMethod(WRITE_REPLACE);
            method.setAccessible(true);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(fn);
            String methodName = serializedLambda.getImplMethodName();
            String className = serializedLambda.getImplClass();
            String propertyName;
            if (methodName.startsWith(GET)) {
                propertyName = java.beans.Introspector.decapitalize(methodName.substring(3));
            } else if (methodName.startsWith(IS)) {
                propertyName = java.beans.Introspector.decapitalize(methodName.substring(2));
            } else {
                propertyName = methodName;
            }

            Class ownerClass = Class.forName(className.replace('/', '.'));
            propertyInfo = new PropertyInfo();
            propertyInfo.setPropertyName(propertyName);
            propertyInfo.setOwnerClass(ownerClass);
            classMap.put(fn, propertyInfo);
            return propertyInfo;
        } catch (ReflectiveOperationException e) {
            throw new InternalRuntimeException(e);
        }
    }
}