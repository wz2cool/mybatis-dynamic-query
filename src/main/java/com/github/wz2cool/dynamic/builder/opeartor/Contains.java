package com.github.wz2cool.dynamic.builder.opeartor;

import com.github.wz2cool.dynamic.FilterOperator;

/**
 * @author Frank
 */
public class Contains<T> implements ISingleValueFilterOperator<T> {

    private final T value;

    public Contains(T value) {
        this.value = value;
    }

    @Override
    public T getValue() {
        return this.value;
    }

    @Override
    public FilterOperator getOperator() {
        return FilterOperator.CONTAINS;
    }
}
