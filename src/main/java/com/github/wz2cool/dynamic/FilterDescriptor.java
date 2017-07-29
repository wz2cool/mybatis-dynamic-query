package com.github.wz2cool.dynamic;

import com.github.wz2cool.helper.CommonsHelper;

import java.util.function.Function;

/**
 * The type Filter descriptor.
 */
public class FilterDescriptor extends FilterDescriptorBase {
    private FilterOperator operator = FilterOperator.EQUAL;
    private String propertyPath;
    private Object value;

    /**
     * Instantiates a new Filter descriptor.
     */
    public FilterDescriptor() {
        // create a empty instance.
    }

    /**
     * Instantiates a new Filter descriptor.
     *
     * @param condition    the condition
     * @param propertyPath the property path
     * @param operator     the operator
     * @param value        the value
     */
    public FilterDescriptor(FilterCondition condition, String propertyPath, FilterOperator operator, Object value) {
        this.setCondition(condition);
        this.operator = operator;
        this.propertyPath = propertyPath;
        this.value = value;
    }

    public <T> FilterDescriptor(FilterCondition condition,
                                Class<T> entityClass,
                                Function<T, Object> getFieldFunc,
                                FilterOperator operator,
                                Object value) {
        this.setCondition(condition);
        this.operator = operator;
        this.propertyPath = CommonsHelper.getPropertryName(entityClass, getFieldFunc);
        this.value = value;
    }

    /**
     * Gets operator.
     *
     * @return the operator
     */
    public FilterOperator getOperator() {
        return operator;
    }

    /**
     * Sets operator.
     *
     * @param operator the operator
     */
    public void setOperator(FilterOperator operator) {
        this.operator = operator;
    }

    /**
     * Gets property path.
     *
     * @return the property path
     */
    public String getPropertyPath() {
        return propertyPath;
    }

    /**
     * Sets property path.
     *
     * @param propertyPath the property path
     */
    public void setPropertyPath(String propertyPath) {
        this.propertyPath = propertyPath;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(Object value) {
        this.value = value;
    }
}