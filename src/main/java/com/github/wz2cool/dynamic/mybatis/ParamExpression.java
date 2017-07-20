package com.github.wz2cool.dynamic.mybatis;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Frank on 2017/7/9.
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
