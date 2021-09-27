package com.github.wz2cool.dynamic.model;

import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.lambda.GetCommonPropertyFunction;

import java.util.HashSet;
import java.util.Set;

public class SelectPropertyConfig<T> {

    private final Set<String> selectPropertyNames = new HashSet<>();
    private final Set<String> ignorePropertyNames = new HashSet<>();

    public Set<String> getSelectPropertyNames() {
        return selectPropertyNames;
    }

    public Set<String> getIgnorePropertyNames() {
        return ignorePropertyNames;
    }

    @SafeVarargs
    public final SelectPropertyConfig<T> select(boolean enable, GetCommonPropertyFunction<T>... getCommonPropertyFuncs) {
        for (GetCommonPropertyFunction<T> getCommonPropertyFunc : getCommonPropertyFuncs) {
            final String propertyName = CommonsHelper.getPropertyName(getCommonPropertyFunc);
            selectPropertyNames.add(propertyName);
        }
        return this;
    }

    @SafeVarargs
    public final SelectPropertyConfig<T> select(GetCommonPropertyFunction<T>... getCommonPropertyFuncs) {
        return select(true, getCommonPropertyFuncs);
    }

    @SafeVarargs
    public final SelectPropertyConfig<T> ignore(boolean enable, GetCommonPropertyFunction<T>... getCommonPropertyFuncs) {
        for (GetCommonPropertyFunction<T> getCommonPropertyFunc : getCommonPropertyFuncs) {
            final String propertyName = CommonsHelper.getPropertyName(getCommonPropertyFunc);
            ignorePropertyNames.add(propertyName);
        }
        return this;
    }

    @SafeVarargs
    public final SelectPropertyConfig<T> ignore(GetCommonPropertyFunction<T>... getCommonPropertyFuncs) {
        return ignore(true, getCommonPropertyFuncs);
    }
}
