package com.github.wz2cool.dynamic.mybatis;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Frank
 */
public class ParamExpression {
    private String expression = "";
    private Map<String, Object> paramMap = new LinkedHashMap<>();

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }
}
