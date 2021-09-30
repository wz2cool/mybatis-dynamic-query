package com.github.wz2cool.dynamic.mybatis;

import com.github.wz2cool.dynamic.FilterOperator;
import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.mybatis.mapper.provider.factory.ProviderColumn;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frank on 7/17/2017.
 */
@SuppressWarnings("squid:S1192")
class ExpressionHelper {

    @SuppressWarnings("squid:MethodCyclomaticComplexity")
    String getExpression(final FilterOperator operator, final ProviderColumn columnInfo, final Object filterValue, final String... paramPlaceholders) {
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
                throw new UnsupportedOperationException(CommonsHelper.format("not support operator: %s", operator.name()));
        }
    }

    String getEqualExpression(final ProviderColumn columnInfo, Object filterValue, final String... paramPlaceholders) {
        if (filterValue == null) {
            return CommonsHelper.format("%s IS NULL", columnInfo.getDbColumn());
        }

        return CommonsHelper.format("%s = #{%s}", columnInfo.getDbColumn(), paramPlaceholders[0]);
    }

    String getNotEqualExpression(final ProviderColumn columnInfo, Object filterValue, final String... paramPlaceholders) {
        if (filterValue == null) {
            return CommonsHelper.format("%s IS NOT NULL", columnInfo.getDbColumn());
        }

        return CommonsHelper.format("%s <> #{%s}", columnInfo.getDbColumn(), paramPlaceholders[0]);
    }

    String getLessThanExpression(final ProviderColumn columnInfo, final String... paramPlaceholders) {
        return CommonsHelper.format("%s < #{%s}", columnInfo.getDbColumn(), paramPlaceholders[0]);
    }

    String getLessThanOrEqualExpression(final ProviderColumn columnInfo, final String... paramPlaceholders) {
        return CommonsHelper.format("%s <= #{%s}", columnInfo.getDbColumn(), paramPlaceholders[0]);
    }

    String getGreaterThanOrEqualExpression(final ProviderColumn columnInfo, final String... paramPlaceholders) {
        return CommonsHelper.format("%s >= #{%s}", columnInfo.getDbColumn(), paramPlaceholders[0]);
    }

    String getGreaterThanExpression(final ProviderColumn columnInfo, final String... paramPlaceholders) {
        return CommonsHelper.format("%s > #{%s}", columnInfo.getDbColumn(), paramPlaceholders[0]);
    }

    String getLikeExpression(final ProviderColumn columnInfo, final String... paramPlaceholders) {
        return CommonsHelper.format("%s LIKE #{%s}", columnInfo.getDbColumn(), paramPlaceholders[0]);
    }

    String getInExpression(final ProviderColumn columnInfo, final String... paramPlaceholders) {
        if (paramPlaceholders.length == 0) {
            return "FALSE";
        }

        String inStr = "%s IN (%s)";
        List<String> formattedParams = new ArrayList<>();
        for (String paramPlaceholder : paramPlaceholders) {
            formattedParams.add(CommonsHelper.format("#{%s}", paramPlaceholder));
        }
        return CommonsHelper.format(inStr, columnInfo.getDbColumn(), String.join(",", formattedParams));
    }

    String getNotInExpression(final ProviderColumn columnInfo, final String... paramPlaceholders) {
        if (paramPlaceholders.length == 0) {
            return "TRUE";
        }

        String notInStr = "%s NOT IN (%s)";
        List<String> formattedParams = new ArrayList<>();
        for (String paramPlaceholder : paramPlaceholders) {
            formattedParams.add(CommonsHelper.format("#{%s}", paramPlaceholder));
        }
        return CommonsHelper.format(notInStr, columnInfo.getDbColumn(), String.join(",", formattedParams));
    }

    String getBetweenExpression(final ProviderColumn columnInfo, final String... paramPlaceholders) {
        int expectedSize = 2;
        if (paramPlaceholders.length != expectedSize) {
            String errMsg = "if \"Between\" operator, the count of paramPlaceholders must be 2";
            throw new InvalidParameterException(errMsg);
        }

        return CommonsHelper.format("%s BETWEEN #{%s} AND #{%s}", columnInfo.getDbColumn(), paramPlaceholders[0], paramPlaceholders[1]);
    }
}
