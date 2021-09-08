package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.lambda.GetCommonPropertyFunction;

import java.util.HashSet;
import java.util.Set;

public class UpdatePropertyConfig<T> {

    private final Set<String> selectPropertyNames = new HashSet<>();
    private final Set<String> ignorePropertyNames = new HashSet<>();

    Set<String> getSelectPropertyNames() {
        return selectPropertyNames;
    }

    Set<String> getIgnorePropertyNames() {
        return ignorePropertyNames;
    }

    @SafeVarargs
    public final UpdatePropertyConfig<T> select(GetCommonPropertyFunction<T>... getCommonPropertyFuncs) {
        for (GetCommonPropertyFunction<T> getCommonPropertyFunc : getCommonPropertyFuncs) {
            final String propertyName = CommonsHelper.getPropertyName(getCommonPropertyFunc);
            selectPropertyNames.add(propertyName);
        }
        return this;
    }

    @SafeVarargs
    public final UpdatePropertyConfig<T> ignore(GetCommonPropertyFunction<T>... getCommonPropertyFuncs) {
        for (GetCommonPropertyFunction<T> getCommonPropertyFunc : getCommonPropertyFuncs) {
            final String propertyName = CommonsHelper.getPropertyName(getCommonPropertyFunc);
            ignorePropertyNames.add(propertyName);
        }
        return this;
    }
}
