package com.github.wz2cool.dynamic;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author Frank
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FilterDescriptor.class, name = "filterDescriptor"),
        @JsonSubTypes.Type(value = FilterGroupDescriptor.class, name = "filterGroupDescriptor"),
        @JsonSubTypes.Type(value = CustomFilterDescriptor.class, name = "customFilterDescriptor")
})
@SuppressWarnings("java:S2326")
public interface BaseFilterDescriptor<T> {
    /**
     * get condition of and
     *
     * @return condition of and
     */
    FilterCondition getCondition();

    /**
     * set condition
     *
     * @param condition condition of and
     */
    void setCondition(FilterCondition condition);
}
