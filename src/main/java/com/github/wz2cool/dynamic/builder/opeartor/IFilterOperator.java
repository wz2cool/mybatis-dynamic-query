package com.github.wz2cool.dynamic.builder.opeartor;

import com.github.wz2cool.dynamic.FilterOperator;

/**
 * @author Frank
 */
@SuppressWarnings("squid:S2326")
public interface IFilterOperator<T> {
    /**
     * Get filter operator.
     *
     * @return filterOperator enum.
     */
    FilterOperator getOperator();

    /**
     * Get filter value.
     *
     * @return filter value.
     */
    Object getValue();
}
