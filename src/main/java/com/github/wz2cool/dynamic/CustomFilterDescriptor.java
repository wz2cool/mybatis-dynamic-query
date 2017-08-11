package com.github.wz2cool.dynamic;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: Frank
 * \* Date: 8/8/2017
 * \* Time: 1:49 PM
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class CustomFilterDescriptor extends FilterDescriptorBase {
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