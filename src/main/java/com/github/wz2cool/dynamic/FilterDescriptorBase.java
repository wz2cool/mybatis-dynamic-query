package com.github.wz2cool.dynamic;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Created by Frank on 2017/4/14.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FilterDescriptor.class, name = "filterDescriptor"),
        @JsonSubTypes.Type(value = FilterGroupDescriptor.class, name = "filterGroupDescriptor"),
        @JsonSubTypes.Type(value = CustomFilterDescriptor.class, name = "customFilterDescriptor")
})
public abstract class FilterDescriptorBase {
    private FilterCondition condition = FilterCondition.AND;

    public FilterCondition getCondition() {
        return condition;
    }

    public void setCondition(FilterCondition condition) {
        this.condition = condition;
    }
}
