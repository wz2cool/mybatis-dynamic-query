package com.github.wz2cool.dynamic.builder.opeartor;

import com.github.wz2cool.dynamic.FilterOperator;

/**
 * @author Frank
 */
public class NotContains<T> implements ISingleValueFilterOperator<T> {

    private final T value;

    public NotContains(T value) {
        this.value = value;
    }

    @Override
    public T getValue() {
        return this.value;
    }

    @Override
    public FilterOperator getOperator() {
        return FilterOperator.NOT_CONTAINS;
    }
}
