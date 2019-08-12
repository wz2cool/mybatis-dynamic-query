package com.github.wz2cool.dynamic.builder;

import com.github.wz2cool.dynamic.BaseFilterDescriptor;
import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.FilterCondition;
import com.github.wz2cool.dynamic.builder.direction.ISortDirection;
import com.github.wz2cool.dynamic.builder.opeartor.IFilterOperator;
import com.github.wz2cool.dynamic.lambda.GetCommonPropertyFunction;
import com.github.wz2cool.dynamic.lambda.GetPropertyFunction;

/**
 * @author Frank
 */
public class WhereClauseBuilder<T>
        extends BaseConditionClauseBuilder<WhereClauseBuilder<T>, T>
        implements IDynamicQueryBuilder<T> {

    private final DynamicQueryBuilder<T> dynamicQueryBuilder;

    public <R extends Comparable> WhereClauseBuilder(
            DynamicQueryBuilder<T> dynamicQueryBuilder,
            GetPropertyFunction<T, R> getPropertyFunction,
            IFilterOperator<R> operator,
            ConditionClauseBuilder<T>[] conditionClauseBuilders) {
        this.dynamicQueryBuilder = dynamicQueryBuilder;
        dynamicQueryBuilder.setWhereClauseBuilder(this);
        andOrInternal(FilterCondition.AND, getPropertyFunction, operator, conditionClauseBuilders);
    }

    public OrderByClauseBuilder<T> orderBy(GetCommonPropertyFunction<T> getPropertyFunction) {
        return new OrderByClauseBuilder<>(dynamicQueryBuilder, getPropertyFunction);
    }

    public OrderByClauseBuilder<T> orderBy(GetCommonPropertyFunction<T> getPropertyFunction, ISortDirection direction) {
        return new OrderByClauseBuilder<>(dynamicQueryBuilder, getPropertyFunction, direction);
    }

    @Override
    public DynamicQuery<T> build() {
        return dynamicQueryBuilder.build();
    }

    BaseFilterDescriptor[] getFilters() {
        return this.conditionClauseBuilders.stream().map(ConditionClauseBuilder::toFilter)
                .toArray(BaseFilterDescriptor[]::new);
    }
}
