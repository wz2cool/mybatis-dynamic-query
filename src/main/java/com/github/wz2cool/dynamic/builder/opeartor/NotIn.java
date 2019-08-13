package com.github.wz2cool.dynamic.builder.opeartor;

import com.github.wz2cool.dynamic.FilterOperator;

import java.util.Arrays;
import java.util.List;

/**
 * @author Frank
 */
public class NotIn<T> implements IMultipleValueFilterOperator<T> {

    private List<T> value;

    public NotIn(T[] value) {
        this.value = Arrays.asList(value);
    }

    @Override
    public FilterOperator getOperator() {
        return FilterOperator.NOT_IN;
    }

    @Override
    public List<T> getValue() {
        return this.value;
    }
}
