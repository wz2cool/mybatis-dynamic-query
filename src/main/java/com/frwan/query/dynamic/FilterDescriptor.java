package com.frwan.query.dynamic;

public class FilterDescriptor extends FilterDescriptorBase {
    private FilterOperator operator = FilterOperator.EQUAL;
    private String propertyPath;
    private Object value;

    public FilterDescriptor() {
        // just new empty instance.
    }

    public FilterDescriptor(FilterCondition condition, String propertyPath, FilterOperator operator, Object value) {
        this.setCondition(condition);
        this.operator = operator;
        this.propertyPath = propertyPath;
        this.value = value;
    }

    public FilterOperator getOperator() {
        return operator;
    }

    public void setOperator(FilterOperator operator) {
        this.operator = operator;
    }

    public String getPropertyPath() {
        return propertyPath;
    }

    public void setPropertyPath(String propertyPath) {
        this.propertyPath = propertyPath;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}