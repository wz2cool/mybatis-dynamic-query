package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.builder.opeartor.IFilterOperator;
import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.lambda.*;
import org.apache.commons.lang3.ArrayUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.function.UnaryOperator;

/**
 * @author Frank
 **/
@SuppressWarnings("unchecked")
public abstract class BaseFilterGroup<T, S extends BaseFilterGroup<T, S>> {

    private BaseFilterDescriptor<T>[] filters = new BaseFilterDescriptor[0];

    public BaseFilterDescriptor<T>[] getFilters() {
        return filters;
    }

    public void setFilters(BaseFilterDescriptor<T>[] filters) {
        this.filters = filters;
    }

    @SafeVarargs
    public final void addFilters(BaseFilterDescriptor<T>... newFilters) {
        setFilters(ArrayUtils.addAll(filters, newFilters));
    }

    @SafeVarargs
    public final void removeFilters(BaseFilterDescriptor<T>... removeFilters) {
        for (BaseFilterDescriptor<T> removeFilter : removeFilters) {
            setFilters(ArrayUtils.removeAllOccurences(filters, removeFilter));
        }
    }

    public S and(
            GetBigDecimalPropertyFunction<T> getPropertyFunc,
            IFilterOperator<BigDecimal> filterOperator) {
        return and(true, getPropertyFunc, filterOperator);
    }

    public S or(
            GetBigDecimalPropertyFunction<T> getPropertyFunc,
            IFilterOperator<BigDecimal> filterOperator) {
        return or(true, getPropertyFunc, filterOperator);
    }

    public S and(
            boolean enable,
            GetBigDecimalPropertyFunction<T> getPropertyFunc,
            IFilterOperator<BigDecimal> filterOperator) {
        return filterInternal(FilterCondition.AND, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue(), enable);
    }

    public S or(
            boolean enable,
            GetBigDecimalPropertyFunction<T> getPropertyFunc,
            IFilterOperator<BigDecimal> filterOperator) {
        return filterInternal(FilterCondition.OR, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue(), enable);
    }

    public S and(
            GetBytePropertyFunction<T> getPropertyFunc,
            IFilterOperator<Byte> filterOperator) {
        return and(true, getPropertyFunc, filterOperator);
    }

    public S or(
            GetBytePropertyFunction<T> getPropertyFunc,
            IFilterOperator<Byte> filterOperator) {
        return or(true, getPropertyFunc, filterOperator);
    }

    public S and(
            boolean enable,
            GetBytePropertyFunction<T> getPropertyFunc,
            IFilterOperator<Byte> filterOperator) {
        return filterInternal(FilterCondition.AND, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue(), enable);
    }

    public S or(
            boolean enable,
            GetBytePropertyFunction<T> getPropertyFunc,
            IFilterOperator<Byte> filterOperator) {
        return filterInternal(FilterCondition.OR, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue(), enable);
    }

    public S and(
            GetDatePropertyFunction<T> getPropertyFunc,
            IFilterOperator<Date> filterOperator) {
        return and(true, getPropertyFunc, filterOperator);
    }

    public S or(
            GetDatePropertyFunction<T> getPropertyFunc,
            IFilterOperator<Date> filterOperator) {
        return or(true, getPropertyFunc, filterOperator);
    }

    public S and(
            boolean enable,
            GetDatePropertyFunction<T> getPropertyFunc,
            IFilterOperator<Date> filterOperator) {
        return filterInternal(FilterCondition.AND, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue(), enable);
    }

    public S or(
            boolean enable,
            GetDatePropertyFunction<T> getPropertyFunc,
            IFilterOperator<Date> filterOperator) {
        return filterInternal(FilterCondition.OR, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue(), enable);
    }

    public S and(
            GetDoublePropertyFunction<T> getPropertyFunc,
            IFilterOperator<Double> filterOperator) {
        return and(true, getPropertyFunc, filterOperator);
    }

    public S or(
            GetDoublePropertyFunction<T> getPropertyFunc,
            IFilterOperator<Double> filterOperator) {
        return or(true, getPropertyFunc, filterOperator);
    }

    public S and(
            boolean enable,
            GetDoublePropertyFunction<T> getPropertyFunc,
            IFilterOperator<Double> filterOperator) {
        return filterInternal(FilterCondition.AND, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue(), enable);
    }

    public S or(
            boolean enable,
            GetDoublePropertyFunction<T> getPropertyFunc,
            IFilterOperator<Double> filterOperator) {
        return filterInternal(FilterCondition.OR, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue(), enable);
    }

    public S and(
            GetFloatPropertyFunction<T> getPropertyFunc,
            IFilterOperator<Float> filterOperator) {
        return and(true, getPropertyFunc, filterOperator);
    }

    public S or(
            GetFloatPropertyFunction<T> getPropertyFunc,
            IFilterOperator<Float> filterOperator) {
        return or(true, getPropertyFunc, filterOperator);
    }

    public S and(
            boolean enable,
            GetFloatPropertyFunction<T> getPropertyFunc,
            IFilterOperator<Float> filterOperator) {
        return filterInternal(FilterCondition.AND, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue(), enable);
    }

    public S or(
            boolean enable,
            GetFloatPropertyFunction<T> getPropertyFunc,
            IFilterOperator<Float> filterOperator) {
        return filterInternal(FilterCondition.OR, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue(), enable);
    }

    public S and(
            GetIntegerPropertyFunction<T> getPropertyFunc,
            IFilterOperator<Integer> filterOperator) {
        return and(true, getPropertyFunc, filterOperator);
    }

    public S or(
            GetIntegerPropertyFunction<T> getPropertyFunc,
            IFilterOperator<Integer> filterOperator) {
        return or(true, getPropertyFunc, filterOperator);
    }

    public S and(
            boolean enable,
            GetIntegerPropertyFunction<T> getPropertyFunc,
            IFilterOperator<Integer> filterOperator) {
        return filterInternal(FilterCondition.AND, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue(), enable);
    }

    public S or(
            boolean enable,
            GetIntegerPropertyFunction<T> getPropertyFunc,
            IFilterOperator<Integer> filterOperator) {
        return filterInternal(FilterCondition.OR, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue(), enable);
    }

    public S and(
            GetLongPropertyFunction<T> getPropertyFunc,
            IFilterOperator<Long> filterOperator) {
        return and(true, getPropertyFunc, filterOperator);
    }

    public S or(
            GetLongPropertyFunction<T> getPropertyFunc,
            IFilterOperator<Long> filterOperator) {
        return or(true, getPropertyFunc, filterOperator);
    }

    public S and(
            boolean enable,
            GetLongPropertyFunction<T> getPropertyFunc,
            IFilterOperator<Long> filterOperator) {
        return filterInternal(FilterCondition.AND, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue(), enable);
    }

    public S or(
            boolean enable,
            GetLongPropertyFunction<T> getPropertyFunc,
            IFilterOperator<Long> filterOperator) {
        return filterInternal(FilterCondition.OR, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue(), enable);
    }

    public S and(
            GetShortPropertyFunction<T> getPropertyFunc,
            IFilterOperator<Short> filterOperator) {
        return and(true, getPropertyFunc, filterOperator);
    }

    public S or(
            GetShortPropertyFunction<T> getPropertyFunc,
            IFilterOperator<Short> filterOperator) {
        return or(true, getPropertyFunc, filterOperator);
    }

    public S and(
            boolean enable,
            GetShortPropertyFunction<T> getPropertyFunc,
            IFilterOperator<Short> filterOperator) {
        return filterInternal(FilterCondition.AND, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue(), enable);
    }

    public S or(
            boolean enable,
            GetShortPropertyFunction<T> getPropertyFunc,
            IFilterOperator<Short> filterOperator) {
        return filterInternal(FilterCondition.OR, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue(), enable);
    }

    public S and(
            GetStringPropertyFunction<T> getPropertyFunc,
            IFilterOperator<String> filterOperator) {
        return and(true, getPropertyFunc, filterOperator);
    }

    public S or(
            GetStringPropertyFunction<T> getPropertyFunc,
            IFilterOperator<String> filterOperator) {
        return or(true, getPropertyFunc, filterOperator);
    }

    public S and(
            boolean enable,
            GetStringPropertyFunction<T> getPropertyFunc,
            IFilterOperator<String> filterOperator) {
        return filterInternal(FilterCondition.AND, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue(), enable);
    }

    public S or(
            boolean enable,
            GetStringPropertyFunction<T> getPropertyFunc,
            IFilterOperator<String> filterOperator) {
        return filterInternal(FilterCondition.OR, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue(), enable);
    }

    public S and(UnaryOperator<FilterGroupDescriptor<T>> groupConsumer) {
        return and(true, groupConsumer);
    }

    public S or(UnaryOperator<FilterGroupDescriptor<T>> groupConsumer) {
        return or(true, groupConsumer);
    }

    public S and(boolean enable, UnaryOperator<FilterGroupDescriptor<T>> groupConsumer) {
        if (enable) {
            FilterGroupDescriptor<T> filterGroupDescriptor = new FilterGroupDescriptor<>();
            filterGroupDescriptor.setCondition(FilterCondition.AND);
            this.addFilters(groupConsumer.apply(filterGroupDescriptor));
        }
        return (S) this;
    }

    public S or(boolean enable, UnaryOperator<FilterGroupDescriptor<T>> groupConsumer) {
        if (enable) {
            FilterGroupDescriptor<T> filterGroupDescriptor = new FilterGroupDescriptor<>();
            filterGroupDescriptor.setCondition(FilterCondition.OR);
            this.addFilters(groupConsumer.apply(filterGroupDescriptor));
        }
        return (S) this;
    }

    @SafeVarargs
    public final S and(boolean enable, BaseFilterDescriptor<T>... filters) {
        if (enable) {
            FilterGroupDescriptor<T> filterGroupDescriptor = new FilterGroupDescriptor<>();
            filterGroupDescriptor.setCondition(FilterCondition.AND);
            filterGroupDescriptor.setFilters(filters);
            this.addFilters(filterGroupDescriptor);
        }
        return (S) this;
    }

    @SafeVarargs
    public final S and(BaseFilterDescriptor<T>... filters) {
        return and(true, filters);
    }

    @SafeVarargs
    public final S or(boolean enable, BaseFilterDescriptor<T>... filters) {
        if (enable) {
            FilterGroupDescriptor<T> filterGroupDescriptor = new FilterGroupDescriptor<>();
            filterGroupDescriptor.setCondition(FilterCondition.OR);
            filterGroupDescriptor.setFilters(filters);
            this.addFilters(filterGroupDescriptor);
        }
        return (S) this;
    }

    public S or(BaseFilterDescriptor<T>... filters) {
        return or(true, filters);
    }

    private <R extends Comparable> S filterInternal(
            FilterCondition condition,
            GetPropertyFunction<T, R> getPropertyFunc,
            FilterOperator operator, Object filterValue, boolean enable) {
        if (enable) {
            FilterDescriptor<T> filterDescriptor = new FilterDescriptor<>();
            String propertyName = CommonsHelper.getPropertyName(getPropertyFunc);
            filterDescriptor.setCondition(condition);
            filterDescriptor.setPropertyName(propertyName);
            filterDescriptor.setOperator(operator);
            filterDescriptor.setValue(filterValue);
            this.addFilters(filterDescriptor);
        }
        return (S) this;
    }
}
