package com.github.wz2cool.dynamic.builder.opeartor;

import com.github.wz2cool.dynamic.FilterOperator;

import java.util.*;

/**
 * @author Frank
 */
public class In<T> implements IMultipleValueFilterOperator<T> {

    private List<T> value;

    public In(T[] values) {
        if (Objects.isNull(values)) {
            this.value = new ArrayList<>();
        } else {
            this.value = Arrays.asList(values);
        }
    }

    public In(Collection<T> values) {
        if (Objects.isNull(values)) {
            this.value = new ArrayList<>();
        } else {
            this.value = new ArrayList<>(values);
        }
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
