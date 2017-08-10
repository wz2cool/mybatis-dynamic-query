package com.github.wz2cool.dynamic;

import java.util.*;

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
    private List<Object> params = new ArrayList<>();

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Object[] getParams() {
        return params.toArray();
    }

    public boolean addParam(Object param) {
        return params.add(param);
    }

    public boolean removeParam(Object param) {
        return params.remove(param);
    }
}