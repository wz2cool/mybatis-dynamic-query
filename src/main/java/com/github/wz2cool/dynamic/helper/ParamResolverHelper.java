package com.github.wz2cool.dynamic.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParamResolverHelper {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("[#|\\$]\\{([A-Za-z0-9_]+)\\}");

    private static final Map<String, String> EXPRESSION_CACHE = new ConcurrentHashMap<>();

    private ParamResolverHelper() {
    }

    public static String resolveExpression(String expression) {
        if (EXPRESSION_CACHE.containsKey(expression)) {
            return EXPRESSION_CACHE.get(expression);
        }

        String newExpression = expression;
        List<String> params = listParams(expression);
        for (String param : params) {
            String newParam = toNewParam(param);
            newExpression = newExpression.replace(param, newParam);
        }
        EXPRESSION_CACHE.put(expression, newExpression);
        return newExpression;
    }

    private static List<String> listParams(String expression) {
        List<String> result = new ArrayList<>();
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(expression);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }

    private static String toNewParam(String param) {
        String substring = param.substring(2);
        // 补充前缀
        String newSubString = "dynamicQueryParams." + substring;
        return param.replace(substring, newSubString);
    }
}
