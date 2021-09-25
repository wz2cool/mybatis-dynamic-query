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
 * @author wangjin
 */
public class CommonsHelper {
    private static final String SYMBOL = "{}";
    private static final int SYMBOL_LENGTH = SYMBOL.length();

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
     * 字符串替换,表示符号为:{} {@link CommonsHelper#SYMBOL}
     * <code>
     * format("{},{}", "A", "B");           AB
     * format("{},{}", "A", "B", "C");      A,B
     * format("{},{},{}", "A");             A
     * format("{},{},{}", "A", "B", "C");   A,B,C
     * </code>
     *
     * @param pattern   根据{}去替换arguments
     * @param arguments {}的替换值
     * @return String
     */
    public static String format(String pattern, String... arguments) {
        final StringBuilder stringBuilder = new StringBuilder(pattern.length() + 16);
        //当前{}的坐标
        int indexOf = -1;
        //从什么位置开始找{}坐标
        int fromIndex = 0;
        for (String argument : arguments) {
            indexOf = pattern.indexOf(SYMBOL, fromIndex);
            if (indexOf == -1) {
                if (fromIndex == 0) {
                    //直接返回,没有匹配到
                    return pattern;
                } else {
                    //这里说明{}加多了. 那就容错处理.添加后面所有的字符串返回
                    return stringBuilder.append(pattern, fromIndex, pattern.length()).toString();
                }
            }
            stringBuilder.append(pattern, fromIndex, indexOf);
            stringBuilder.append(argument);
            //当前坐标加上SYMBOL_LENGTH,继续循环判断
            fromIndex = indexOf + SYMBOL_LENGTH;
        }
        return stringBuilder.toString();
    }
}