package com.github.wz2cool.dynamic;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.io.Serializable;

/**
 * @author Frank
 */
@SuppressWarnings("squid:S1948")
@JsonTypeName("customFilterDescriptor")
public class CustomFilterDescriptor implements Serializable, BaseFilterDescriptor {
    private static final long serialVersionUID = 7448086874396793224L;

    private FilterCondition condition = FilterCondition.AND;

    private String expression;
    private Object[] params;

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object... params) {
        this.params = params;
    }

    public CustomFilterDescriptor() {
        // create empty constructor
    }

    public CustomFilterDescriptor(String expression, Object... params) {
        this.setExpression(expression);
        this.setParams(params);
    }

    public CustomFilterDescriptor(FilterCondition condition, String expression, Object... params) {
        this.setCondition(condition);
        this.setExpression(expression);
        this.setParams(params);
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