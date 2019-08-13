package com.github.wz2cool.dynamic.builder.opeartor;

import com.github.wz2cool.dynamic.FilterOperator;

/**
 * @author Frank
 */
public class GreaterThanOrEqual<T> implements ISingleValueFilterOperator<T> {

    private final T value;

    public GreaterThanOrEqual(T value) {
        this.value = value;
    }

    @Override
    public FilterOperator getOperator() {
        return FilterOperator.GREATER_THAN_OR_EQUAL;
    }

    @Override
    public T getValue() {
        return this.value;
    }
}
