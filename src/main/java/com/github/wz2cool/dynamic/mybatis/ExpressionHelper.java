package com.github.wz2cool.dynamic.mybatis;

import com.github.wz2cool.dynamic.FilterOperator;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frank on 7/17/2017.
 */
@SuppressWarnings("squid:S1192")
class ExpressionHelper {

    @SuppressWarnings("squid:MethodCyclomaticComplexity")
    String getExpression(final FilterOperator operator, final ColumnInfo columnInfo, final Object filterValue, final String... paramPlaceholders) {
        switch (operator) {
            case EQUAL:
                return getEqualExpression(columnInfo, filterValue, paramPlaceholders);
            case NOT_EQUAL:
                return getNotEqualExpression(columnInfo, filterValue, paramPlaceholders);
            case LESS_THAN:
                return getLessThanExpression(columnInfo, paramPlaceholders);
            case LESS_THAN_OR_EQUAL:
                return getLessThanOrEqualExpression(columnInfo, paramPlaceholders);
            case GREATER_THAN_OR_EQUAL:
                return getGreaterThanOrEqualExpression(columnInfo, paramPlaceholders);
            case GREATER_THAN:
                return getGreaterThanExpression(columnInfo, paramPlaceholders);
            case START_WITH:
            case END_WITH:
            case CONTAINS:
                return getLikeExpression(columnInfo, paramPlaceholders);
            case IN:
                return getInExpression(columnInfo, paramPlaceholders);
            case NOT_IN:
                return getNotInExpression(columnInfo, paramPlaceholders);
            case BETWEEN:
                return getBetweenExpression(columnInfo, paramPlaceholders);
            default:
                throw new UnsupportedOperationException(String.format("not support operator: %s", operator));
        }
    }

    String getEqualExpression(final ColumnInfo columnInfo, Object filterValue, final String... paramPlaceholders) {
        if (filterValue == null) {
            return String.format("%s IS NULL", columnInfo.getQueryColumn());
        }

        return String.format("%s = #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0]);
    }

    String getNotEqualExpression(final ColumnInfo columnInfo, Object filterValue, final String... paramPlaceholders) {
        if (filterValue == null) {
            return String.format("%s IS NOT NULL", columnInfo.getQueryColumn());
        }

        return String.format("%s <> #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0]);
    }

    String getLessThanExpression(final ColumnInfo columnInfo, final String... paramPlaceholders) {
        return String.format("%s < #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0]);
    }

    String getLessThanOrEqualExpression(final ColumnInfo columnInfo, final String... paramPlaceholders) {
        return String.format("%s <= #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0]);
    }

    String getGreaterThanOrEqualExpression(final ColumnInfo columnInfo, final String... paramPlaceholders) {
        return String.format("%s >= #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0]);
    }

    String getGreaterThanExpression(final ColumnInfo columnInfo, final String... paramPlaceholders) {
        return String.format("%s > #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0]);
    }

    String getLikeExpression(final ColumnInfo columnInfo, final String... paramPlaceholders) {
        return String.format("%s LIKE #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0]);
    }

    String getInExpression(final ColumnInfo columnInfo, final String... paramPlaceholders) {
        if (paramPlaceholders.length == 0) {
            return "";
        }

        String inStr = "%s IN (%s)";
        List<String> formattedParams = new ArrayList<>();
        for (String paramPlaceholder : paramPlaceholders) {
            formattedParams.add(String.format("#{%s}", paramPlaceholder));
        }
        return String.format(inStr, columnInfo.getQueryColumn(), String.join(",", formattedParams));
    }

    String getNotInExpression(final ColumnInfo columnInfo, final String... paramPlaceholders) {
        if (paramPlaceholders.length == 0) {
            return "";
        }

        String notInStr = "%s NOT IN (%s)";
        List<String> formattedParams = new ArrayList<>();
        for (String paramPlaceholder : paramPlaceholders) {
            formattedParams.add(String.format("#{%s}", paramPlaceholder));
        }
        return String.format(notInStr, columnInfo.getQueryColumn(), String.join(",", formattedParams));
    }

    String getBetweenExpression(final ColumnInfo columnInfo, final String... paramPlaceholders) {
        int expectedSize = 2;
        if (paramPlaceholders.length != expectedSize) {
            String errMsg = "if \"Between\" operator, the count of paramPlaceholders must be 2";
            throw new InvalidParameterException(errMsg);
        }

        return String.format("%s BETWEEN #{%s} AND #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0], paramPlaceholders[1]);
    }
}
