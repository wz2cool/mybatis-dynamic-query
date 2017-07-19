package com.frwan.query.dynamic.mybatis;

import com.frwan.query.dynamic.FilterOperator;
import com.frwan.query.helper.CommonsHelper;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frank on 7/17/2017.
 */
@SuppressWarnings("squid:S1192")
class DbExpressionHelper {
    private final DatabaseType databaseType;

    DbExpressionHelper(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    @SuppressWarnings("squid:MethodCyclomaticComplexity")
    String getExpression(final FilterOperator operator, final QueryColumnInfo queryColumnInfo, final Object filterValue, final String... paramPlaceholders) {
        switch (operator) {
            case EQUAL:
                return getEqualExpression(queryColumnInfo, filterValue, paramPlaceholders);
            case NOT_EQUAL:
                return getNotEqualExpression(queryColumnInfo, filterValue, paramPlaceholders);
            case LESS_THAN:
                return getLessThanExpression(queryColumnInfo, paramPlaceholders);
            case LESS_THAN_OR_EQUAL:
                return getLessThanOrEqualExpression(queryColumnInfo, paramPlaceholders);
            case GREATER_THAN_OR_EQUAL:
                return getGreaterThanOrEqualExpression(queryColumnInfo, paramPlaceholders);
            case GREATER_THAN:
                return getGreaterThanExpression(queryColumnInfo, paramPlaceholders);
            case START_WITH:
            case END_WITH:
            case CONTAINS:
                return getLikeExpression(queryColumnInfo, paramPlaceholders);
            case IN:
                return getInExpression(queryColumnInfo, paramPlaceholders);
            case NOT_IN:
                return getNotInExpression(queryColumnInfo, paramPlaceholders);
            case BETWEEN:
                return getBetweenExpression(queryColumnInfo, paramPlaceholders);
            case BITAND_GREATER_ZERO:
                return getBitAndGreaterZero(queryColumnInfo, paramPlaceholders);
            case BITAND_EQUAL_ZERO:
                return getBitAndEqualZero(queryColumnInfo, paramPlaceholders);
            case BITAND_EQUAL_INPUT:
                return getBitAndEqualInput(queryColumnInfo, paramPlaceholders);
            default:
                throw new UnsupportedOperationException(String.format("not support operator: %s", operator));
        }
    }

    String getEqualExpression(final QueryColumnInfo queryColumnInfo, Object filterValue, final String... paramPlaceholders) {
        if (filterValue == null) {
            return String.format("%s IS NULL", queryColumnInfo.getQueryColumn());
        }

        if (databaseType == DatabaseType.POSTRESQL) {
            Class fieldType = queryColumnInfo.getField().getType();
            if (CommonsHelper.isNumeric(fieldType)) {
                return String.format("%s = #{%s}::NUMERIC", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
            } else if (fieldType == String.class) {
                return String.format("%s = #{%s}", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
            } else {
                return String.format("%s::TEXT = #{%s}", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
            }
        }

        return String.format("%s = #{%s}", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
    }

    String getNotEqualExpression(final QueryColumnInfo queryColumnInfo, Object filterValue, final String... paramPlaceholders) {
        if (filterValue == null) {
            return String.format("%s IS NOT NULL", queryColumnInfo.getQueryColumn());
        }

        if (databaseType == DatabaseType.POSTRESQL) {
            Class fieldType = queryColumnInfo.getField().getType();
            if (CommonsHelper.isNumeric(fieldType)) {
                return String.format("%s <> #{%s}::NUMERIC", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
            } else if (fieldType == String.class) {
                return String.format("%s <> #{%s}", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
            } else {
                return String.format("%s::TEXT <> #{%s}", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
            }
        }

        return String.format("%s <> #{%s}", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
    }

    String getLessThanExpression(final QueryColumnInfo queryColumnInfo, final String... paramPlaceholders) {
        if (databaseType == DatabaseType.POSTRESQL) {
            Class fieldType = queryColumnInfo.getField().getType();
            if (CommonsHelper.isNumeric(fieldType)) {
                return String.format("%s < #{%s}::NUMERIC", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
            } else if (fieldType == String.class) {
                return String.format("%s < #{%s}", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
            } else {
                return String.format("%s::TEXT < #{%s}", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
            }
        }

        return String.format("%s < #{%s}", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
    }

    String getLessThanOrEqualExpression(final QueryColumnInfo queryColumnInfo, final String... paramPlaceholders) {
        if (databaseType == DatabaseType.POSTRESQL) {
            Class fieldType = queryColumnInfo.getField().getType();
            if (CommonsHelper.isNumeric(fieldType)) {
                return String.format("%s <= #{%s}::NUMERIC", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
            } else if (fieldType == String.class) {
                return String.format("%s <= #{%s}", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
            } else {
                return String.format("%s::TEXT <= #{%s}", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
            }
        }

        return String.format("%s <= #{%s}", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
    }

    String getGreaterThanOrEqualExpression(final QueryColumnInfo queryColumnInfo, final String... paramPlaceholders) {
        if (databaseType == DatabaseType.POSTRESQL) {
            Class fieldType = queryColumnInfo.getField().getType();
            if (CommonsHelper.isNumeric(fieldType)) {
                return String.format("%s >= #{%s}::NUMERIC", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
            } else if (fieldType == String.class) {
                return String.format("%s >= #{%s}", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
            } else {
                return String.format("%s::TEXT >= #{%s}", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
            }
        }

        return String.format("%s >= #{%s}", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
    }

    String getGreaterThanExpression(final QueryColumnInfo queryColumnInfo, final String... paramPlaceholders) {
        if (databaseType == DatabaseType.POSTRESQL) {
            Class fieldType = queryColumnInfo.getField().getType();
            if (CommonsHelper.isNumeric(fieldType)) {
                return String.format("%s > #{%s}::NUMERIC", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
            } else if (fieldType == String.class) {
                return String.format("%s > #{%s}", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
            } else {
                return String.format("%s::TEXT > #{%s}", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
            }
        }

        return String.format("%s > #{%s}", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
    }

    String getLikeExpression(final QueryColumnInfo queryColumnInfo, final String... paramPlaceholders) {
        if (databaseType == DatabaseType.POSTRESQL) {
            Class fieldType = queryColumnInfo.getField().getType();
            if (fieldType == String.class) {
                return String.format("%s LIKE #{%s}", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
            } else {
                return String.format("%s::TEXT LIKE #{%s}", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
            }
        }

        return String.format("%s LIKE #{%s}", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
    }

    String getInExpression(final QueryColumnInfo queryColumnInfo, final String... paramPlaceholders) {
        if (paramPlaceholders.length == 0) {
            return "";
        }

        String inStr = "%s IN (%s)";
        if (databaseType == DatabaseType.POSTRESQL) {
            Class fieldType = queryColumnInfo.getField().getType();
            if (CommonsHelper.isNumeric(fieldType)) {
                List<String> formattedParams = new ArrayList<>();
                for (String paramPlaceholder : paramPlaceholders) {
                    formattedParams.add(String.format("#{%s}::NUMERIC", paramPlaceholder));
                }
                return String.format(inStr, queryColumnInfo.getQueryColumn(), String.join(",", formattedParams));
            } else if (fieldType == String.class) {
                List<String> formattedParams = new ArrayList<>();
                for (String paramPlaceholder : paramPlaceholders) {
                    formattedParams.add(String.format("#{%s}", paramPlaceholder));
                }
                return String.format(inStr, queryColumnInfo.getQueryColumn(), String.join(",", formattedParams));
            } else {
                List<String> formattedParams = new ArrayList<>();
                for (String paramPlaceholder : paramPlaceholders) {
                    formattedParams.add(String.format("#{%s}", paramPlaceholder));
                }
                return String.format("%s::TEXT IN (%s)", queryColumnInfo.getQueryColumn(), String.join(",", formattedParams));
            }
        }

        List<String> formattedParams = new ArrayList<>();
        for (String paramPlaceholder : paramPlaceholders) {
            formattedParams.add(String.format("#{%s}", paramPlaceholder));
        }
        return String.format(inStr, queryColumnInfo.getQueryColumn(), String.join(",", formattedParams));
    }

    String getNotInExpression(final QueryColumnInfo queryColumnInfo, final String... paramPlaceholders) {
        if (paramPlaceholders.length == 0) {
            return "";
        }

        String notInStr = "%s NOT IN (%s)";
        if (databaseType == DatabaseType.POSTRESQL) {
            Class fieldType = queryColumnInfo.getField().getType();
            if (CommonsHelper.isNumeric(fieldType)) {
                List<String> formattedParams = new ArrayList<>();
                for (String paramPlaceholder : paramPlaceholders) {
                    formattedParams.add(String.format("#{%s}::NUMERIC", paramPlaceholder));
                }
                return String.format(notInStr, queryColumnInfo.getQueryColumn(), String.join(",", formattedParams));
            } else if (fieldType == String.class) {
                List<String> formattedParams = new ArrayList<>();
                for (String paramPlaceholder : paramPlaceholders) {
                    formattedParams.add(String.format("#{%s}", paramPlaceholder));
                }
                return String.format(notInStr, queryColumnInfo.getQueryColumn(), String.join(",", formattedParams));
            } else {
                List<String> formattedParams = new ArrayList<>();
                for (String paramPlaceholder : paramPlaceholders) {
                    formattedParams.add(String.format("#{%s}", paramPlaceholder));
                }
                return String.format("%s::TEXT NOT IN (%s)", queryColumnInfo.getQueryColumn(), String.join(",", formattedParams));
            }
        }

        List<String> formattedParams = new ArrayList<>();
        for (String paramPlaceholder : paramPlaceholders) {
            formattedParams.add(String.format("#{%s}", paramPlaceholder));
        }
        return String.format(notInStr, queryColumnInfo.getQueryColumn(), String.join(",", formattedParams));
    }

    String getBetweenExpression(final QueryColumnInfo queryColumnInfo, final String... paramPlaceholders) {
        if (paramPlaceholders.length != 2) {
            String errMsg = "if \"Between\" operator, the count of paramPlaceholders must be 2";
            throw new InvalidParameterException(errMsg);
        }

        if (databaseType == DatabaseType.POSTRESQL) {
            Class fieldType = queryColumnInfo.getField().getType();
            if (CommonsHelper.isNumeric(fieldType)) {
                return String.format("%s BETWEEN #{%s}::NUMERIC AND #{%s}::NUMERIC", queryColumnInfo.getQueryColumn(), paramPlaceholders[0], paramPlaceholders[1]);
            } else if (fieldType == String.class) {
                return String.format("%s BETWEEN #{%s} AND #{%s}", queryColumnInfo.getQueryColumn(), paramPlaceholders[0], paramPlaceholders[1]);
            } else {
                return String.format("%s::TEXT BETWEEN #{%s} AND #{%s}", queryColumnInfo.getQueryColumn(), paramPlaceholders[0], paramPlaceholders[1]);
            }
        }

        return String.format("%s BETWEEN #{%s} AND #{%s}", queryColumnInfo.getQueryColumn(), paramPlaceholders[0], paramPlaceholders[1]);
    }

    String getBitAndGreaterZero(final QueryColumnInfo queryColumnInfo, final String... paramPlaceholders) {
        if (databaseType == DatabaseType.H2) {
            return String.format("BITAND(%s, #{%s}) > 0", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
        }

        if (databaseType == DatabaseType.POSTRESQL) {
            Class fieldType = queryColumnInfo.getField().getType();
            if (CommonsHelper.isNumeric(fieldType)) {
                return String.format("%s & #{%s}::BIGINT > 0", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
            } else {
                return String.format("%s::BIGINT & #{%s}::BIGINT > 0", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
            }
        }
        return String.format("%s & #{%s} > 0", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
    }

    String getBitAndEqualZero(final QueryColumnInfo queryColumnInfo, final String... paramPlaceholders) {
        if (databaseType == DatabaseType.H2) {
            return String.format("BITAND(%s, #{%s}) = 0", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
        }

        if (databaseType == DatabaseType.POSTRESQL) {
            Class fieldType = queryColumnInfo.getField().getType();
            if (CommonsHelper.isNumeric(fieldType)) {
                return String.format("%s & #{%s}::BIGINT = 0", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
            } else {
                return String.format("%s::BIGINT & #{%s}::BIGINT = 0", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
            }
        }

        return String.format("%s & #{%s} = 0", queryColumnInfo.getQueryColumn(), paramPlaceholders[0]);
    }

    String getBitAndEqualInput(final QueryColumnInfo queryColumnInfo, final String... paramPlaceholders) {

        if (databaseType == DatabaseType.H2) {
            return String.format("BITAND(%s, #{%s}) = #{%s}", queryColumnInfo.getQueryColumn(), paramPlaceholders[0], paramPlaceholders[0]);
        }

        if (databaseType == DatabaseType.POSTRESQL) {
            Class fieldType = queryColumnInfo.getField().getType();
            if (CommonsHelper.isNumeric(fieldType)) {
                return String.format("%s & #{%s}::BIGINT = #{%s}::BIGINT", queryColumnInfo.getQueryColumn(), paramPlaceholders[0], paramPlaceholders[0]);
            } else {
                return String.format("%s::BIGINT & #{%s}::BIGINT = #{%s}::BIGINT", queryColumnInfo.getQueryColumn(), paramPlaceholders[0], paramPlaceholders[0]);
            }
        }

        return String.format("%s & #{%s} = #{%s}", queryColumnInfo.getQueryColumn(), paramPlaceholders[0], paramPlaceholders[0]);
    }
}
