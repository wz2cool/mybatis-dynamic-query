package com.github.wz2cool.dynamic.builder.opeartor;

import com.github.wz2cool.dynamic.FilterOperator;

/**
 * @author Frank
 */
public class StartWith<T> implements ISingleValueFilterOperator<T> {

    private final T value;

    public StartWith(T value) {
        this.value = value;
    }

    @Override
    public FilterOperator getOperator() {
        return FilterOperator.START_WITH;
    }

    @Override
    public T getValue() {
        return this.value;
    }
}
