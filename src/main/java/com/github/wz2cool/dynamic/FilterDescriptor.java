package com.github.wz2cool.dynamic;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Frank
 * @date 2019/10/01
 **/
@SuppressWarnings({"squid:S1172", "squid:S2326"})
@JsonTypeName("filterDescriptor")
public class FilterDescriptor implements BaseFilterDescriptor {

    private FilterCondition condition = FilterCondition.AND;
    private FilterOperator operator = FilterOperator.EQUAL;
    private String propertyName;
    private Object value;

    @Override
    public FilterCondition getCondition() {
        return condition;
    }

    @Override
    public void setCondition(FilterCondition condition) {
        this.condition = condition;
    }

    public FilterOperator getOperator() {
        return operator;
    }

    public void setOperator(FilterOperator operator) {
        this.operator = operator;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public FilterDescriptor() {
        // for json
    }

    public FilterDescriptor(FilterCondition condition, String propertyName, FilterOperator operator, Object value) {
        this.condition = condition;
        this.propertyName = propertyName;
        this.operator = operator;
        this.value = value;
    }
}
