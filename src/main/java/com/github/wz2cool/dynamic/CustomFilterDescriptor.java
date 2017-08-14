package com.github.wz2cool.dynamic;

import java.io.Serializable;

public class CustomFilterDescriptor extends FilterDescriptorBase implements Serializable {
    private static final long serialVersionUID = 7448086874396793224L;

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

    public CustomFilterDescriptor(FilterCondition condition, String expression, Object... params) {
        this.setCondition(condition);
        this.setExpression(expression);
        this.setParams(params);
    }
}