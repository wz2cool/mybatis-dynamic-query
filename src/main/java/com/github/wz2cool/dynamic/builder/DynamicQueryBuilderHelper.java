package com.github.wz2cool.dynamic.builder;

import com.github.wz2cool.dynamic.FilterCondition;
import com.github.wz2cool.dynamic.builder.direction.Ascending;
import com.github.wz2cool.dynamic.builder.direction.Descending;
import com.github.wz2cool.dynamic.builder.opeartor.*;
import com.github.wz2cool.dynamic.lambda.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

/**
 * @author Frank
 */
public class DynamicQueryBuilderHelper {

    private DynamicQueryBuilderHelper() {
    }

    /// region and operator

    public static <R> LessThan<R> lessThan(R value) {
        return new LessThan<>(value);
    }

    public static <R> LessThanOrEqual<R> lessThanOrEqual(R value) {
        return new LessThanOrEqual<>(value);
    }

    public static <R> Equal<R> isEqual(R value) {
        return new Equal<>(value);
    }

    public static <R> NotEqual<R> notEqual(R value) {
        return new NotEqual<>(value);
    }

    public static <R> GreaterThanOrEqual<R> greaterThanOrEqual(R value) {
        return new GreaterThanOrEqual<>(value);
    }

    public static <R> GreaterThan<R> greaterThan(R value) {
        return new GreaterThan<>(value);
    }

    public static <R> StartWith<R> startWith(R value) {
        return new StartWith<>(value);
    }

    public static <R> EndWith<R> endWith(R value) {
        return new EndWith<>(value);
    }

    public static <R> Contains<R> contains(R value) {
        return new Contains<>(value);
    }

    public static <R> NotContains<R> notContains(R value) {
        return new NotContains<>(value);
    }

    @SafeVarargs
    public static <R> In<R> in(R... values) {
        return new In<>(values);
    }

    public static <R> In<R> in(Collection<R> values) {
        return new In<>(values);
    }

    @SafeVarargs
    public static <R> NotIn<R> notIn(R... values) {
        return new NotIn<>(values);
    }

    public static <R> NotIn<R> notIn(Collection<R> values) {
        return new NotIn<>(values);
    }

    public static <R> Between<R> between(R value1, R value2) {
        return new Between<>(value1, value2);
    }

    /// endregion

    /// region sort direction

    private static final Ascending ASC = new Ascending();
    private static final Descending DESC = new Descending();

    public static Ascending asc() {
        return ASC;
    }

    public static Descending desc() {
        return DESC;
    }

    /// endregion

    /// region and

    @SafeVarargs
    public static <T> ConditionClauseBuilder<T> and(
            GetBigDecimalPropertyFunction<T> getPropertyFunction, IFilterOperator<BigDecimal> operator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new ConditionClauseBuilder<>(
                FilterCondition.AND, getPropertyFunction, operator, conditionClauseBuilders);
    }

    @SafeVarargs
    public static <T> ConditionClauseBuilder<T> and(
            GetBytePropertyFunction<T> getPropertyFunction, IFilterOperator<Byte> operator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new ConditionClauseBuilder<>(
                FilterCondition.AND, getPropertyFunction, operator, conditionClauseBuilders);
    }

    @SafeVarargs
    public static <T> ConditionClauseBuilder<T> and(
            GetDatePropertyFunction<T> getPropertyFunction, IFilterOperator<Date> operator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new ConditionClauseBuilder<>(
                FilterCondition.AND, getPropertyFunction, operator, conditionClauseBuilders);
    }

    @SafeVarargs
    public static <T> ConditionClauseBuilder<T> and(
            GetDoublePropertyFunction<T> getPropertyFunction, IFilterOperator<Double> operator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new ConditionClauseBuilder<>(
                FilterCondition.AND, getPropertyFunction, operator, conditionClauseBuilders);
    }

    @SafeVarargs
    public static <T> ConditionClauseBuilder<T> and(
            GetFloatPropertyFunction<T> getPropertyFunction, IFilterOperator<Float> operator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new ConditionClauseBuilder<>(
                FilterCondition.AND, getPropertyFunction, operator, conditionClauseBuilders);
    }

    @SafeVarargs
    public static <T> ConditionClauseBuilder<T> and(
            GetIntegerPropertyFunction<T> getPropertyFunction, IFilterOperator<Integer> operator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new ConditionClauseBuilder<>(
                FilterCondition.AND, getPropertyFunction, operator, conditionClauseBuilders);
    }

    @SafeVarargs
    public static <T> ConditionClauseBuilder<T> and(
            GetLongPropertyFunction<T> getPropertyFunction, IFilterOperator<Long> operator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new ConditionClauseBuilder<>(
                FilterCondition.AND, getPropertyFunction, operator, conditionClauseBuilders);
    }

    @SafeVarargs
    public static <T> ConditionClauseBuilder<T> and(
            GetShortPropertyFunction<T> getPropertyFunction, IFilterOperator<Short> operator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new ConditionClauseBuilder<>(
                FilterCondition.AND, getPropertyFunction, operator, conditionClauseBuilders);
    }

    @SafeVarargs
    public static <T> ConditionClauseBuilder<T> and(
            GetStringPropertyFunction<T> getPropertyFunction, IFilterOperator<String> operator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new ConditionClauseBuilder<>(
                FilterCondition.AND, getPropertyFunction, operator, conditionClauseBuilders);
    }

    /// endregion

    /// region or

    @SafeVarargs
    public static <T> ConditionClauseBuilder<T> or(
            GetBigDecimalPropertyFunction<T> getPropertyFunction, IFilterOperator<BigDecimal> operator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new ConditionClauseBuilder<>(
                FilterCondition.OR, getPropertyFunction, operator, conditionClauseBuilders);
    }

    @SafeVarargs
    public static <T> ConditionClauseBuilder<T> or(
            GetBytePropertyFunction<T> getPropertyFunction, IFilterOperator<Byte> operator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new ConditionClauseBuilder<>(
                FilterCondition.OR, getPropertyFunction, operator, conditionClauseBuilders);
    }

    @SafeVarargs
    public static <T> ConditionClauseBuilder<T> or(
            GetDatePropertyFunction<T> getPropertyFunction, IFilterOperator<Date> operator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new ConditionClauseBuilder<>(
                FilterCondition.OR, getPropertyFunction, operator, conditionClauseBuilders);
    }

    @SafeVarargs
    public static <T> ConditionClauseBuilder<T> or(
            GetDoublePropertyFunction<T> getPropertyFunction, IFilterOperator<Double> operator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new ConditionClauseBuilder<>(
                FilterCondition.OR, getPropertyFunction, operator, conditionClauseBuilders);
    }

    @SafeVarargs
    public static <T> ConditionClauseBuilder<T> or(
            GetFloatPropertyFunction<T> getPropertyFunction, IFilterOperator<Float> operator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new ConditionClauseBuilder<>(
                FilterCondition.OR, getPropertyFunction, operator, conditionClauseBuilders);
    }

    @SafeVarargs
    public static <T> ConditionClauseBuilder<T> or(
            GetIntegerPropertyFunction<T> getPropertyFunction, IFilterOperator<Integer> operator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new ConditionClauseBuilder<>(
                FilterCondition.OR, getPropertyFunction, operator, conditionClauseBuilders);
    }

    @SafeVarargs
    public static <T> ConditionClauseBuilder<T> or(
            GetLongPropertyFunction<T> getPropertyFunction, IFilterOperator<Long> operator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new ConditionClauseBuilder<>(
                FilterCondition.OR, getPropertyFunction, operator, conditionClauseBuilders);
    }

    @SafeVarargs
    public static <T> ConditionClauseBuilder<T> or(
            GetShortPropertyFunction<T> getPropertyFunction, IFilterOperator<Short> operator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new ConditionClauseBuilder<>(
                FilterCondition.OR, getPropertyFunction, operator, conditionClauseBuilders);
    }

    @SafeVarargs
    public static <T> ConditionClauseBuilder<T> or(
            GetStringPropertyFunction<T> getPropertyFunction, IFilterOperator<String> operator,
            ConditionClauseBuilder<T>... conditionClauseBuilders) {
        return new ConditionClauseBuilder<>(
                FilterCondition.OR, getPropertyFunction, operator, conditionClauseBuilders);
    }

    /// endregion
}
