package com.github.wz2cool.dynamic.builder;

import com.github.wz2cool.dynamic.FilterCondition;
import com.github.wz2cool.dynamic.builder.opeartor.IFilterOperator;
import com.github.wz2cool.dynamic.lambda.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Frank
 */
public abstract class BaseConditionClauseBuilder<E extends BaseConditionClauseBuilder<E, T>, T> {

    final List<ConditionClauseBuilder<T>> conditionClauseBuilders = new ArrayList<>();

    // and

    @SafeVarargs
    public final E and(
            GetBigDecimalPropertyFunction<T> getPropertyFunction, IFilterOperator<BigDecimal> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return andOrInternal(FilterCondition.AND, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    @SafeVarargs
    public final E and(
            GetBytePropertyFunction<T> getPropertyFunction, IFilterOperator<Byte> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return andOrInternal(FilterCondition.AND, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    @SafeVarargs
    public final E and(
            GetDatePropertyFunction<T> getPropertyFunction, IFilterOperator<Date> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return andOrInternal(FilterCondition.AND, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    @SafeVarargs
    public final E and(
            GetDoublePropertyFunction<T> getPropertyFunction, IFilterOperator<Double> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return andOrInternal(FilterCondition.AND, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    @SafeVarargs
    public final E and(
            GetFloatPropertyFunction<T> getPropertyFunction, IFilterOperator<Float> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return andOrInternal(FilterCondition.AND, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    @SafeVarargs
    public final E and(
            GetIntegerPropertyFunction<T> getPropertyFunction, IFilterOperator<Integer> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return andOrInternal(FilterCondition.AND, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    @SafeVarargs
    public final E and(
            GetLongPropertyFunction<T> getPropertyFunction, IFilterOperator<Long> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return andOrInternal(FilterCondition.AND, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    @SafeVarargs
    public final E and(
            GetShortPropertyFunction<T> getPropertyFunction, IFilterOperator<Short> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return andOrInternal(FilterCondition.AND, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    @SafeVarargs
    public final E and(
            GetStringPropertyFunction<T> getPropertyFunction, IFilterOperator<String> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return andOrInternal(FilterCondition.AND, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    // or

    @SafeVarargs
    public final E or(
            GetBigDecimalPropertyFunction<T> getPropertyFunction, IFilterOperator<BigDecimal> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return andOrInternal(FilterCondition.OR, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    @SafeVarargs
    public final E or(
            GetBytePropertyFunction<T> getPropertyFunction, IFilterOperator<Byte> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return andOrInternal(FilterCondition.OR, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    @SafeVarargs
    public final E or(
            GetDatePropertyFunction<T> getPropertyFunction, IFilterOperator<Date> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return andOrInternal(FilterCondition.OR, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    @SafeVarargs
    public final E or(
            GetDoublePropertyFunction<T> getPropertyFunction, IFilterOperator<Double> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return andOrInternal(FilterCondition.OR, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    @SafeVarargs
    public final E or(
            GetFloatPropertyFunction<T> getPropertyFunction, IFilterOperator<Float> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return andOrInternal(FilterCondition.OR, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    @SafeVarargs
    public final E or(
            GetIntegerPropertyFunction<T> getPropertyFunction, IFilterOperator<Integer> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return andOrInternal(FilterCondition.OR, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    @SafeVarargs
    public final E or(
            GetLongPropertyFunction<T> getPropertyFunction, IFilterOperator<Long> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return andOrInternal(FilterCondition.OR, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    @SafeVarargs
    public final E or(
            GetShortPropertyFunction<T> getPropertyFunction, IFilterOperator<Short> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return andOrInternal(FilterCondition.OR, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    @SafeVarargs
    public final E or(
            GetStringPropertyFunction<T> getPropertyFunction, IFilterOperator<String> filterOperator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return andOrInternal(FilterCondition.OR, getPropertyFunction, filterOperator, conditionClauseBuilders);
    }

    @SuppressWarnings("unchecked")
    <R extends Comparable> E andOrInternal(
            FilterCondition condition,
            GetPropertyFunction<T, R> getPropertyFunction,
            IFilterOperator<R> operator,
            ConditionClauseBuilder<T>[] subConditionClauseBuilders) {
        ConditionClauseBuilder<T> conditionClauseBuilder = new ConditionClauseBuilder<>(
                condition, getPropertyFunction, operator, subConditionClauseBuilders);
        conditionClauseBuilders.add(conditionClauseBuilder);
        return (E) this;
    }
}
