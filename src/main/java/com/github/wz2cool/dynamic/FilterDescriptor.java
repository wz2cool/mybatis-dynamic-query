package com.github.wz2cool.dynamic;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.lambda.GetPropertyFunction;

import java.io.Serializable;

/**
 * The type Filter descriptor.
 *
 * @author Frank
 */
@SuppressWarnings("squid:S1948")
@JsonTypeName("filterDescriptor")
public class FilterDescriptor extends BaseFilterDescriptor implements Serializable {
    private static final long serialVersionUID = -5311044437700352259L;

    private FilterOperator operator = FilterOperator.EQUAL;
    private String propertyName;
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
     * @param propertyName the property path
     * @param operator     the operator
     * @param value        the value
     */
    public FilterDescriptor(FilterCondition condition, String propertyName, FilterOperator operator, Object value) {
        this.setCondition(condition);
        this.operator = operator;
        this.propertyName = propertyName;
        this.value = value;
    }

    public FilterDescriptor(String propertyName, FilterOperator operator, Object value) {
        this.operator = operator;
        this.propertyName = propertyName;
        this.value = value;
    }

    public <T, R extends Comparable> FilterDescriptor(
            GetPropertyFunction<T, R> getFieldFunc,
            FilterOperator operator,
            R value) {
        this.operator = operator;
        this.propertyName = CommonsHelper.getPropertyInfo(getFieldFunc).getPropertyName();
        this.value = value;
    }

    public <T, R extends Comparable> FilterDescriptor(
            FilterCondition condition,
            GetPropertyFunction<T, R> getFieldFunc,
            FilterOperator operator,
            R value) {
        this.setCondition(condition);
        this.operator = operator;
        this.propertyName = CommonsHelper.getPropertyInfo(getFieldFunc).getPropertyName();
        this.value = value;
    }

    public <T, R extends Comparable> FilterDescriptor(
            GetPropertyFunction<T, R> getFieldFunc,
            FilterOperator operator,
            R[] value) {
        this.operator = operator;
        this.propertyName = CommonsHelper.getPropertyInfo(getFieldFunc).getPropertyName();
        this.value = value;
    }

    public <T, R extends Comparable> FilterDescriptor(
            FilterCondition condition,
            GetPropertyFunction<T, R> getFieldFunc,
            FilterOperator operator,
            R[] value) {
        this.setCondition(condition);
        this.operator = operator;
        this.propertyName = CommonsHelper.getPropertyInfo(getFieldFunc).getPropertyName();
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
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Sets property path.
     *
     * @param propertyName the property path
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public <T> void setPropertyPath(GetPropertyFunction<T, Comparable> getFieldFunc) {
        this.propertyName = CommonsHelper.getPropertyInfo(getFieldFunc).getPropertyName();
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