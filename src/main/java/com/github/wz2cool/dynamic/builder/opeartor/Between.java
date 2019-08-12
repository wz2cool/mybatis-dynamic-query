package com.github.wz2cool.dynamic.builder.opeartor;

import com.github.wz2cool.dynamic.FilterOperator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Frank
 */
public class Between<T> implements ITwoValueFilterOperator<T> {

    private final List<T> value;

    public Between(T value1, T value2) {
        List<T> values = new ArrayList<>();
        values.add(value1);
        values.add(value2);
        this.value = values;
    }

    @Override
    public List<T> getValue() {
        return value;
    }

    @Override
    public FilterOperator getOperator() {
        return FilterOperator.BETWEEN;
    }
}
