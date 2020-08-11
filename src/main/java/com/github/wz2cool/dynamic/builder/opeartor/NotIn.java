package com.github.wz2cool.dynamic.builder.opeartor;

import com.github.wz2cool.dynamic.FilterOperator;

import java.util.*;

/**
 * @author Frank
 */
public class NotIn<T> implements IMultipleValueFilterOperator<T> {

    private List<T> value;

    public NotIn(T[] values) {
        if (Objects.isNull(values)) {
            this.value = new ArrayList<>();
        } else {
            this.value = Arrays.asList(values);
        }
    }

    public NotIn(Collection<T> values) {
        if (Objects.isNull(values)) {
            this.value = new ArrayList<>();
        } else {
            this.value = new ArrayList<>(values);
        }
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
