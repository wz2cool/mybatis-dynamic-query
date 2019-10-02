package com.github.wz2cool.dynamic;

import com.github.wz2cool.dynamic.builder.opeartor.IFilterOperator;
import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.lambda.*;
import org.apache.commons.lang3.ArrayUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Frank
 **/
public abstract class BaseFilterGroup<T, S extends BaseFilterGroup<T, S>> {

    private BaseFilterDescriptor[] filters = new BaseFilterDescriptor[]{};

    public BaseFilterDescriptor[] getFilters() {
        return filters;
    }

    public void setFilters(BaseFilterDescriptor[] filters) {
        this.filters = filters;
    }

    public void addFilters(BaseFilterDescriptor... newFilters) {
        setFilters(ArrayUtils.addAll(filters, newFilters));
    }

    public void removeFilters(BaseFilterDescriptor... removeFilters) {
        for (BaseFilterDescriptor removeFilter : removeFilters) {
            setFilters(ArrayUtils.removeAllOccurences(filters, removeFilter));
        }
    }

    public S and(
            GetBigDecimalPropertyFunction<T> getPropertyFunc,
            IFilterOperator<BigDecimal> filterOperator) {
        return filterInternal(FilterCondition.AND, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue());
    }

    public S or(
            GetBigDecimalPropertyFunction<T> getPropertyFunc,
            IFilterOperator<BigDecimal> filterOperator) {
        return filterInternal(FilterCondition.OR, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue());
    }

    public S and(
            GetBytePropertyFunction<T> getPropertyFunc,
            IFilterOperator<Byte> filterOperator) {
        return filterInternal(FilterCondition.AND, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue());
    }

    public S or(
            GetBytePropertyFunction<T> getPropertyFunc,
            IFilterOperator<Byte> filterOperator) {
        return filterInternal(FilterCondition.OR, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue());
    }

    public S and(
            GetDatePropertyFunction<T> getPropertyFunc,
            IFilterOperator<Date> filterOperator) {
        return filterInternal(FilterCondition.AND, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue());
    }

    public S or(
            GetDatePropertyFunction<T> getPropertyFunc,
            IFilterOperator<Date> filterOperator) {
        return filterInternal(FilterCondition.OR, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue());
    }

    public S and(
            GetDoublePropertyFunction<T> getPropertyFunc,
            IFilterOperator<Double> filterOperator) {
        return filterInternal(FilterCondition.AND, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue());
    }

    public S or(
            GetDoublePropertyFunction<T> getPropertyFunc,
            IFilterOperator<Double> filterOperator) {
        return filterInternal(FilterCondition.OR, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue());
    }

    public S and(
            GetFloatPropertyFunction<T> getPropertyFunc,
            IFilterOperator<Float> filterOperator) {
        return filterInternal(FilterCondition.AND, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue());
    }

    public S or(
            GetFloatPropertyFunction<T> getPropertyFunc,
            IFilterOperator<Float> filterOperator) {
        return filterInternal(FilterCondition.OR, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue());
    }

    public S and(
            GetIntegerPropertyFunction<T> getPropertyFunc,
            IFilterOperator<Integer> filterOperator) {
        return filterInternal(FilterCondition.AND, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue());
    }

    public S or(
            GetIntegerPropertyFunction<T> getPropertyFunc,
            IFilterOperator<Integer> filterOperator) {
        return filterInternal(FilterCondition.OR, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue());
    }

    public S and(
            GetLongPropertyFunction<T> getPropertyFunc,
            IFilterOperator<Long> filterOperator) {
        return filterInternal(FilterCondition.AND, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue());
    }

    public S or(
            GetLongPropertyFunction<T> getPropertyFunc,
            IFilterOperator<Long> filterOperator) {
        return filterInternal(FilterCondition.OR, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue());
    }

    public S and(
            GetShortPropertyFunction<T> getPropertyFunc,
            IFilterOperator<Short> filterOperator) {
        return filterInternal(FilterCondition.AND, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue());
    }

    public S or(
            GetShortPropertyFunction<T> getPropertyFunc,
            IFilterOperator<Short> filterOperator) {
        return filterInternal(FilterCondition.OR, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue());
    }

    public S and(
            GetStringPropertyFunction<T> getPropertyFunc,
            IFilterOperator<String> filterOperator) {
        return filterInternal(FilterCondition.AND, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue());
    }

    public S or(
            GetStringPropertyFunction<T> getPropertyFunc,
            IFilterOperator<String> filterOperator) {
        return filterInternal(FilterCondition.OR, getPropertyFunc, filterOperator.getOperator(), filterOperator.getValue());
    }

    public InternalFilterGroupBegin<T, S> andGroupBegin() {
        InternalFilterGroupBegin<T, S> internalFilterGroupBegin = new InternalFilterGroupBegin<>();
        internalFilterGroupBegin.setCondition(FilterCondition.AND);
        internalFilterGroupBegin.setOwner((S) this);
        return internalFilterGroupBegin;
    }

    public InternalFilterGroupBegin<T, S> orGroupBegin() {
        InternalFilterGroupBegin<T, S> internalFilterGroupBegin = new InternalFilterGroupBegin<>();
        internalFilterGroupBegin.setCondition(FilterCondition.OR);
        internalFilterGroupBegin.setOwner((S) this);
        return internalFilterGroupBegin;
    }

    private <R extends Comparable> S filterInternal(
            FilterCondition condition,
            GetPropertyFunction<T, R> getPropertyFunc,
            FilterOperator operator, Object filterValue) {
        FilterDescriptor filterDescriptor = new FilterDescriptor();
        String propertyName = CommonsHelper.getPropertyName(getPropertyFunc);
        filterDescriptor.setCondition(condition);
        filterDescriptor.setPropertyName(propertyName);
        filterDescriptor.setOperator(operator);
        filterDescriptor.setValue(filterValue);
        this.addFilters(filterDescriptor);
        return (S) this;
    }
}
