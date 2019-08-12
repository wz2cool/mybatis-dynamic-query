package com.github.wz2cool.dynamic.builder;

import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.lambda.GetCommonPropertyFunction;

/**
 * @author Frank
 */
public class DynamicQueryBuilder<T> implements IDynamicQueryBuilder<T> {

    private SelectClauseBuilder<T> selectClauseBuilder;
    private WhereClauseBuilder<T> whereClauseBuilder;
    private OrderByClauseBuilder<T> orderByClauseBuilder;

    void setSelectClauseBuilder(SelectClauseBuilder<T> selectClauseBuilder) {
        this.selectClauseBuilder = selectClauseBuilder;
    }

    void setWhereClauseBuilder(WhereClauseBuilder<T> whereClauseBuilder) {
        this.whereClauseBuilder = whereClauseBuilder;
    }

    void setOrderByClauseBuilder(OrderByClauseBuilder<T> orderByClauseBuilder) {
        this.orderByClauseBuilder = orderByClauseBuilder;
    }

    public final SelectClauseBuilder<T> selectAll() {
        return new SelectClauseBuilder<>(this);
    }

    @SafeVarargs
    public final SelectClauseBuilder<T> select(GetCommonPropertyFunction<T>... getPropertyFunctions) {
        return new SelectClauseBuilder<>(this, getPropertyFunctions);
    }

    @Override
    public DynamicQuery<T> build() {
        DynamicQuery<T> dynamicQuery = new DynamicQuery<>();
        if (this.selectClauseBuilder != null) {
            dynamicQuery.setSelectedProperties(this.selectClauseBuilder.getSelectedProperties());
        }

        if (this.whereClauseBuilder != null) {
            dynamicQuery.setFilters(this.whereClauseBuilder.getFilters());
        }

        if (this.orderByClauseBuilder != null) {
            dynamicQuery.setSorts(this.orderByClauseBuilder.getSorts());
        }
        return dynamicQuery;
    }
}
