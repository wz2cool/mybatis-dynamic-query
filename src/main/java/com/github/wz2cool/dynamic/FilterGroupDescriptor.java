package com.github.wz2cool.dynamic;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Frank
 **/
@SuppressWarnings("squid:S1172")
@JsonTypeName("filterGroupDescriptor")
public class FilterGroupDescriptor<T>
        extends BaseFilterGroup<T, FilterGroupDescriptor<T>>
        implements BaseFilterDescriptor<T> {

    private FilterCondition condition = FilterCondition.AND;

    private Class<T> clazz;

    public FilterGroupDescriptor() {
        // hide construct
    }

    public FilterGroupDescriptor(Class<T> clazz) {
        this.clazz = clazz;
    }

    public static <T> FilterGroupDescriptor<T> create(Class<T> clazz) {
        return new FilterGroupDescriptor<>(clazz);
    }

    public Class<T> getClazz() {
        return clazz;
    }

    @Override
    public FilterCondition getCondition() {
        return condition;
    }

    @Override
    public void setCondition(FilterCondition condition) {
        this.condition = condition;
    }
}
