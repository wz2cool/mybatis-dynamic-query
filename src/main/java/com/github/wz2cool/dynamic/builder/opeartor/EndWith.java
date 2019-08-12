package com.github.wz2cool.dynamic.builder.opeartor;

import com.github.wz2cool.dynamic.FilterOperator;

/**
 * @author Frank
 */
public class EndWith<T> implements ISingleValueFilterOperator<T> {

    private final T value;

    public EndWith(T value) {
        this.value = value;
    }

    @Override
    public T getValue() {
        return this.value;
    }

    @Override
    public FilterOperator getOperator() {
        return FilterOperator.END_WITH;
    }
}
