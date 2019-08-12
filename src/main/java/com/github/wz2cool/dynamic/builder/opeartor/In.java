package com.github.wz2cool.dynamic.builder.opeartor;

import com.github.wz2cool.dynamic.FilterOperator;

import java.util.Arrays;
import java.util.List;

/**
 * @author Frank
 */
public class In<T> implements IMultipleValueFilterOperator<T> {

    private List<T> value;

    public In(T[] values) {
        this.value = Arrays.asList(values);
    }

    @Override
    public FilterOperator getOperator() {
        return FilterOperator.IN;
    }

    @Override
    public List<T> getValue() {
        return this.value;
    }
}
