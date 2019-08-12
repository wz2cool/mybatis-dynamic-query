package com.github.wz2cool.dynamic.builder;

import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.builder.direction.ISortDirection;
import com.github.wz2cool.dynamic.builder.opeartor.IFilterOperator;
import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.lambda.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Frank
 */
public class SelectClauseBuilder<T> implements IDynamicQueryBuilder<T> {

    private final List<String> selectedProperties = new ArrayList<>();
    private final DynamicQueryBuilder<T> dynamicQueryBuilder;

    public SelectClauseBuilder(DynamicQueryBuilder<T> dynamicQueryBuilder) {
        this.dynamicQueryBuilder = dynamicQueryBuilder;
        dynamicQueryBuilder.setSelectClauseBuilder(this);
    }

    public SelectClauseBuilder(DynamicQueryBuilder<T> dynamicQueryBuilder,
                               GetCommonPropertyFunction<T>[] getPropertyFunctions) {
        this.dynamicQueryBuilder = dynamicQueryBuilder;
        this.selectedProperties.addAll(getSelectedProperties(getPropertyFunctions));
        dynamicQueryBuilder.setSelectClauseBuilder(this);
    }

    String[] getSelectedProperties() {
        return selectedProperties.toArray(new String[0]);
    }

    @Override
    public DynamicQuery<T> build() {
        return dynamicQueryBuilder.build();
    }

    @SafeVarargs
    public final SelectClauseBuilder<T> select(GetCommonPropertyFunction<T>... getPropertyFunctions) {
        this.selectedProperties.addAll(getSelectedProperties(getPropertyFunctions));
        return this;
    }

    @SafeVarargs
    public final WhereClauseBuilder<T> where(
            GetBigDecimalPropertyFunction<T> getPropertyFunction, IFilterOperator<BigDecimal> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new WhereClauseBuilder<>(dynamicQueryBuilder, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    @SafeVarargs
    public final WhereClauseBuilder<T> where(
            GetBytePropertyFunction<T> getPropertyFunction, IFilterOperator<Byte> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new WhereClauseBuilder<>(dynamicQueryBuilder, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    @SafeVarargs
    public final WhereClauseBuilder<T> where(
            GetDatePropertyFunction<T> getPropertyFunction, IFilterOperator<Date> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new WhereClauseBuilder<>(dynamicQueryBuilder, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    @SafeVarargs
    public final WhereClauseBuilder<T> where(
            GetDoublePropertyFunction<T> getPropertyFunction, IFilterOperator<Double> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new WhereClauseBuilder<>(dynamicQueryBuilder, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    @SafeVarargs
    public final WhereClauseBuilder<T> where(
            GetFloatPropertyFunction<T> getPropertyFunction, IFilterOperator<Float> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new WhereClauseBuilder<>(dynamicQueryBuilder, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    @SafeVarargs
    public final WhereClauseBuilder<T> where(
            GetIntegerPropertyFunction<T> getPropertyFunction, IFilterOperator<Integer> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new WhereClauseBuilder<>(dynamicQueryBuilder, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    @SafeVarargs
    public final WhereClauseBuilder<T> where(
            GetLongPropertyFunction<T> getPropertyFunction, IFilterOperator<Long> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new WhereClauseBuilder<>(dynamicQueryBuilder, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    @SafeVarargs
    public final WhereClauseBuilder<T> where(
            GetShortPropertyFunction<T> getPropertyFunction, IFilterOperator<Short> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new WhereClauseBuilder<>(dynamicQueryBuilder, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    @SafeVarargs
    public final WhereClauseBuilder<T> where(
            GetStringPropertyFunction<T> getPropertyFunction, IFilterOperator<String> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new WhereClauseBuilder<>(dynamicQueryBuilder, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    public OrderByClauseBuilder<T> orderBy(GetCommonPropertyFunction<T> getPropertyFunction) {
        return new OrderByClauseBuilder<>(dynamicQueryBuilder, getPropertyFunction);
    }

    public OrderByClauseBuilder<T> orderBy(GetCommonPropertyFunction<T> getPropertyFunction, ISortDirection direction) {
        return new OrderByClauseBuilder<>(dynamicQueryBuilder, getPropertyFunction, direction);
    }

    private List<String> getSelectedProperties(GetCommonPropertyFunction<T>[] getPropertyFunctions) {
        List<String> result = new ArrayList<>();
        for (GetCommonPropertyFunction<T> getPropertyFunction : getPropertyFunctions) {
            String propertyName = CommonsHelper.getPropertyName(getPropertyFunction);
            result.add(propertyName);
        }
        return result;
    }
}
