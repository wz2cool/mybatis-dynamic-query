package com.github.wz2cool.dynamic.builder.opeartor;

import com.github.wz2cool.dynamic.FilterOperator;

/**
 * @author Frank
 */
public class NotEqual<T> implements ISingleValueFilterOperator<T> {

    private final T value;

    public NotEqual(T value) {
        this.value = value;
    }

    @Override
    public FilterOperator getOperator() {
        return FilterOperator.NOT_EQUAL;
    }

    @Override
    public T getValue() {
        return this.value;
    }
}
