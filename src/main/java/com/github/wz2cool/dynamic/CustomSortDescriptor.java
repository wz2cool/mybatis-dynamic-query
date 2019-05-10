package com.github.wz2cool.dynamic;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.io.Serializable;

@SuppressWarnings("squid:S1948")
@JsonTypeName("customSortDescriptor")
public class CustomSortDescriptor extends BaseSortDescriptor implements Serializable {
    private static final long serialVersionUID = -8776490725097358688L;
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

    public CustomSortDescriptor() {
        // create empty constructor
    }

    public CustomSortDescriptor(String expression, Object... params) {
        this.setExpression(expression);
        this.setParams(params);
    }
}