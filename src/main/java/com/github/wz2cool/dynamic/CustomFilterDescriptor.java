package com.github.wz2cool.dynamic;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
    private Map<String, Object> paramMap = new HashMap<>();

    public CustomFilterDescriptor() {
    }

    public CustomFilterDescriptor(FilterCondition condition, String expression, Map<String, Object> paramMap) {
        this.setCondition(condition);
        this.expression = expression;
        this.paramMap = paramMap;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }
}