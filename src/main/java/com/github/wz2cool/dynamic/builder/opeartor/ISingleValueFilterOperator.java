package com.github.wz2cool.dynamic.builder.opeartor;

/**
 * @author Frank
 */
public interface ISingleValueFilterOperator<T> extends IFilterOperator<T> {
    /**
     * Get and value.
     *
     * @return and value.
     */
    @Override
    T getValue();
}
