package com.github.wz2cool.dynamic.builder;

import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.builder.direction.ISortDirection;
import com.github.wz2cool.dynamic.builder.opeartor.IFilterOperator;
import com.github.wz2cool.dynamic.lambda.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Frank
 */
public class DynamicQueryBuilder<T> implements IDynamicQueryBuilder<T> {

    private Class<T> entityClass;
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

    private DynamicQueryBuilder(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public static <T> DynamicQueryBuilder<T> create(Class<T> entityClass) {
        return new DynamicQueryBuilder<>(entityClass);
    }

    @SafeVarargs
    public final SelectClauseBuilder<T> select(GetCommonPropertyFunction<T>... getPropertyFunctions) {
        return new SelectClauseBuilder<>(this, getPropertyFunctions);
    }

    @SafeVarargs
    public final WhereClauseBuilder<T> where(
            GetBigDecimalPropertyFunction<T> getPropertyFunction, IFilterOperator<BigDecimal> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new WhereClauseBuilder<>(this, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    @SafeVarargs
    public final WhereClauseBuilder<T> where(
            GetBytePropertyFunction<T> getPropertyFunction, IFilterOperator<Byte> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new WhereClauseBuilder<>(this, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    @SafeVarargs
    public final WhereClauseBuilder<T> where(
            GetDatePropertyFunction<T> getPropertyFunction, IFilterOperator<Date> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new WhereClauseBuilder<>(this, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    @SafeVarargs
    public final WhereClauseBuilder<T> where(
            GetDoublePropertyFunction<T> getPropertyFunction, IFilterOperator<Double> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new WhereClauseBuilder<>(this, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    @SafeVarargs
    public final WhereClauseBuilder<T> where(
            GetFloatPropertyFunction<T> getPropertyFunction, IFilterOperator<Float> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new WhereClauseBuilder<>(this, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    @SafeVarargs
    public final WhereClauseBuilder<T> where(
            GetIntegerPropertyFunction<T> getPropertyFunction, IFilterOperator<Integer> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new WhereClauseBuilder<>(this, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    @SafeVarargs
    public final WhereClauseBuilder<T> where(
            GetLongPropertyFunction<T> getPropertyFunction, IFilterOperator<Long> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new WhereClauseBuilder<>(this, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    @SafeVarargs
    public final WhereClauseBuilder<T> where(
            GetShortPropertyFunction<T> getPropertyFunction, IFilterOperator<Short> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new WhereClauseBuilder<>(this, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    @SafeVarargs
    public final WhereClauseBuilder<T> where(
            GetStringPropertyFunction<T> getPropertyFunction, IFilterOperator<String> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new WhereClauseBuilder<>(this, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    public OrderByClauseBuilder<T> orderBy(GetCommonPropertyFunction<T> getPropertyFunction) {
        return new OrderByClauseBuilder<>(this, getPropertyFunction);
    }

    public OrderByClauseBuilder<T> orderBy(GetCommonPropertyFunction<T> getPropertyFunction, ISortDirection direction) {
        return new OrderByClauseBuilder<>(this, getPropertyFunction, direction);
    }


    @Override
    public DynamicQuery<T> build() {
        DynamicQuery<T> dynamicQuery = DynamicQuery.createQuery(this.entityClass);
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
