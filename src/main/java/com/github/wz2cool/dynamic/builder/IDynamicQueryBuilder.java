package com.github.wz2cool.dynamic.builder;

import com.github.wz2cool.dynamic.DynamicQuery;

/**
 * @author Frank
 */
public interface IDynamicQueryBuilder<T> {
    /**
     * build to dynamic Query.
     *
     * @return dynamic query.
     */
    DynamicQuery<T> build();
}
