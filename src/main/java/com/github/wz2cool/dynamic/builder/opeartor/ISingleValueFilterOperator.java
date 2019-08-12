package com.github.wz2cool.dynamic.builder.opeartor;

/**
 * @author Frank
 */
public interface ISingleValueFilterOperator<T> extends IFilterOperator<T> {
    /**
     * Get filter value.
     *
     * @return filter value.
     */
    @Override
    T getValue();
}
