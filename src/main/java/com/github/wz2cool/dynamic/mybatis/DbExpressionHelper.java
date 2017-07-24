package com.github.wz2cool.dynamic.mybatis;

import com.github.wz2cool.dynamic.FilterOperator;
import com.github.wz2cool.helper.CommonsHelper;

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
            case BITAND_GREATER_ZERO:
                return getBitAndGreaterZero(columnInfo, paramPlaceholders);
            case BITAND_EQUAL_ZERO:
                return getBitAndEqualZero(columnInfo, paramPlaceholders);
            case BITAND_EQUAL_INPUT:
                return getBitAndEqualInput(columnInfo, paramPlaceholders);
            default:
                throw new UnsupportedOperationException(String.format("not support operator: %s", operator));
        }
    }

    String getEqualExpression(final ColumnInfo columnInfo, Object filterValue, final String... paramPlaceholders) {
        if (filterValue == null) {
            return String.format("%s IS NULL", columnInfo.getQueryColumn());
        }

        if (databaseType == DatabaseType.POSTRESQL) {
            Class fieldType = columnInfo.getField().getType();
            if (CommonsHelper.isNumeric(fieldType)) {
                return String.format("%s = #{%s}::NUMERIC", columnInfo.getQueryColumn(), paramPlaceholders[0]);
            } else if (fieldType == String.class) {
                return String.format("%s = #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0]);
            } else {
                return String.format("%s::TEXT = #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0]);
            }
        }

        return String.format("%s = #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0]);
    }

    String getNotEqualExpression(final ColumnInfo columnInfo, Object filterValue, final String... paramPlaceholders) {
        if (filterValue == null) {
            return String.format("%s IS NOT NULL", columnInfo.getQueryColumn());
        }

        if (databaseType == DatabaseType.POSTRESQL) {
            Class fieldType = columnInfo.getField().getType();
            if (CommonsHelper.isNumeric(fieldType)) {
                return String.format("%s <> #{%s}::NUMERIC", columnInfo.getQueryColumn(), paramPlaceholders[0]);
            } else if (fieldType == String.class) {
                return String.format("%s <> #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0]);
            } else {
                return String.format("%s::TEXT <> #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0]);
            }
        }

        return String.format("%s <> #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0]);
    }

    String getLessThanExpression(final ColumnInfo columnInfo, final String... paramPlaceholders) {
        if (databaseType == DatabaseType.POSTRESQL) {
            Class fieldType = columnInfo.getField().getType();
            if (CommonsHelper.isNumeric(fieldType)) {
                return String.format("%s < #{%s}::NUMERIC", columnInfo.getQueryColumn(), paramPlaceholders[0]);
            } else if (fieldType == String.class) {
                return String.format("%s < #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0]);
            } else {
                return String.format("%s::TEXT < #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0]);
            }
        }

        return String.format("%s < #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0]);
    }

    String getLessThanOrEqualExpression(final ColumnInfo columnInfo, final String... paramPlaceholders) {
        if (databaseType == DatabaseType.POSTRESQL) {
            Class fieldType = columnInfo.getField().getType();
            if (CommonsHelper.isNumeric(fieldType)) {
                return String.format("%s <= #{%s}::NUMERIC", columnInfo.getQueryColumn(), paramPlaceholders[0]);
            } else if (fieldType == String.class) {
                return String.format("%s <= #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0]);
            } else {
                return String.format("%s::TEXT <= #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0]);
            }
        }

        return String.format("%s <= #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0]);
    }

    String getGreaterThanOrEqualExpression(final ColumnInfo columnInfo, final String... paramPlaceholders) {
        if (databaseType == DatabaseType.POSTRESQL) {
            Class fieldType = columnInfo.getField().getType();
            if (CommonsHelper.isNumeric(fieldType)) {
                return String.format("%s >= #{%s}::NUMERIC", columnInfo.getQueryColumn(), paramPlaceholders[0]);
            } else if (fieldType == String.class) {
                return String.format("%s >= #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0]);
            } else {
                return String.format("%s::TEXT >= #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0]);
            }
        }

        return String.format("%s >= #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0]);
    }

    String getGreaterThanExpression(final ColumnInfo columnInfo, final String... paramPlaceholders) {
        if (databaseType == DatabaseType.POSTRESQL) {
            Class fieldType = columnInfo.getField().getType();
            if (CommonsHelper.isNumeric(fieldType)) {
                return String.format("%s > #{%s}::NUMERIC", columnInfo.getQueryColumn(), paramPlaceholders[0]);
            } else if (fieldType == String.class) {
                return String.format("%s > #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0]);
            } else {
                return String.format("%s::TEXT > #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0]);
            }
        }

        return String.format("%s > #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0]);
    }

    String getLikeExpression(final ColumnInfo columnInfo, final String... paramPlaceholders) {
        if (databaseType == DatabaseType.POSTRESQL) {
            Class fieldType = columnInfo.getField().getType();
            if (fieldType == String.class) {
                return String.format("%s LIKE #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0]);
            } else {
                return String.format("%s::TEXT LIKE #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0]);
            }
        }

        return String.format("%s LIKE #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0]);
    }

    String getInExpression(final ColumnInfo columnInfo, final String... paramPlaceholders) {
        if (paramPlaceholders.length == 0) {
            return "";
        }

        String inStr = "%s IN (%s)";
        if (databaseType == DatabaseType.POSTRESQL) {
            Class fieldType = columnInfo.getField().getType();
            if (CommonsHelper.isNumeric(fieldType)) {
                List<String> formattedParams = new ArrayList<>();
                for (String paramPlaceholder : paramPlaceholders) {
                    formattedParams.add(String.format("#{%s}::NUMERIC", paramPlaceholder));
                }
                return String.format(inStr, columnInfo.getQueryColumn(), String.join(",", formattedParams));
            } else if (fieldType == String.class) {
                List<String> formattedParams = new ArrayList<>();
                for (String paramPlaceholder : paramPlaceholders) {
                    formattedParams.add(String.format("#{%s}", paramPlaceholder));
                }
                return String.format(inStr, columnInfo.getQueryColumn(), String.join(",", formattedParams));
            } else {
                List<String> formattedParams = new ArrayList<>();
                for (String paramPlaceholder : paramPlaceholders) {
                    formattedParams.add(String.format("#{%s}", paramPlaceholder));
                }
                return String.format("%s::TEXT IN (%s)", columnInfo.getQueryColumn(), String.join(",", formattedParams));
            }
        }

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
        if (databaseType == DatabaseType.POSTRESQL) {
            Class fieldType = columnInfo.getField().getType();
            if (CommonsHelper.isNumeric(fieldType)) {
                List<String> formattedParams = new ArrayList<>();
                for (String paramPlaceholder : paramPlaceholders) {
                    formattedParams.add(String.format("#{%s}::NUMERIC", paramPlaceholder));
                }
                return String.format(notInStr, columnInfo.getQueryColumn(), String.join(",", formattedParams));
            } else if (fieldType == String.class) {
                List<String> formattedParams = new ArrayList<>();
                for (String paramPlaceholder : paramPlaceholders) {
                    formattedParams.add(String.format("#{%s}", paramPlaceholder));
                }
                return String.format(notInStr, columnInfo.getQueryColumn(), String.join(",", formattedParams));
            } else {
                List<String> formattedParams = new ArrayList<>();
                for (String paramPlaceholder : paramPlaceholders) {
                    formattedParams.add(String.format("#{%s}", paramPlaceholder));
                }
                return String.format("%s::TEXT NOT IN (%s)", columnInfo.getQueryColumn(), String.join(",", formattedParams));
            }
        }

        List<String> formattedParams = new ArrayList<>();
        for (String paramPlaceholder : paramPlaceholders) {
            formattedParams.add(String.format("#{%s}", paramPlaceholder));
        }
        return String.format(notInStr, columnInfo.getQueryColumn(), String.join(",", formattedParams));
    }

    String getBetweenExpression(final ColumnInfo columnInfo, final String... paramPlaceholders) {
        if (paramPlaceholders.length != 2) {
            String errMsg = "if \"Between\" operator, the count of paramPlaceholders must be 2";
            throw new InvalidParameterException(errMsg);
        }

        if (databaseType == DatabaseType.POSTRESQL) {
            Class fieldType = columnInfo.getField().getType();
            if (CommonsHelper.isNumeric(fieldType)) {
                return String.format("%s BETWEEN #{%s}::NUMERIC AND #{%s}::NUMERIC", columnInfo.getQueryColumn(), paramPlaceholders[0], paramPlaceholders[1]);
            } else if (fieldType == String.class) {
                return String.format("%s BETWEEN #{%s} AND #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0], paramPlaceholders[1]);
            } else {
                return String.format("%s::TEXT BETWEEN #{%s} AND #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0], paramPlaceholders[1]);
            }
        }

        return String.format("%s BETWEEN #{%s} AND #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0], paramPlaceholders[1]);
    }

    String getBitAndGreaterZero(final ColumnInfo columnInfo, final String... paramPlaceholders) {
        if (databaseType == DatabaseType.H2) {
            return String.format("BITAND(%s, #{%s}) > 0", columnInfo.getQueryColumn(), paramPlaceholders[0]);
        }

        if (databaseType == DatabaseType.POSTRESQL) {
            Class fieldType = columnInfo.getField().getType();
            if (CommonsHelper.isNumeric(fieldType)) {
                return String.format("%s & #{%s}::BIGINT > 0", columnInfo.getQueryColumn(), paramPlaceholders[0]);
            } else {
                return String.format("%s::BIGINT & #{%s}::BIGINT > 0", columnInfo.getQueryColumn(), paramPlaceholders[0]);
            }
        }
        return String.format("%s & #{%s} > 0", columnInfo.getQueryColumn(), paramPlaceholders[0]);
    }

    String getBitAndEqualZero(final ColumnInfo columnInfo, final String... paramPlaceholders) {
        if (databaseType == DatabaseType.H2) {
            return String.format("BITAND(%s, #{%s}) = 0", columnInfo.getQueryColumn(), paramPlaceholders[0]);
        }

        if (databaseType == DatabaseType.POSTRESQL) {
            Class fieldType = columnInfo.getField().getType();
            if (CommonsHelper.isNumeric(fieldType)) {
                return String.format("%s & #{%s}::BIGINT = 0", columnInfo.getQueryColumn(), paramPlaceholders[0]);
            } else {
                return String.format("%s::BIGINT & #{%s}::BIGINT = 0", columnInfo.getQueryColumn(), paramPlaceholders[0]);
            }
        }

        return String.format("%s & #{%s} = 0", columnInfo.getQueryColumn(), paramPlaceholders[0]);
    }

    String getBitAndEqualInput(final ColumnInfo columnInfo, final String... paramPlaceholders) {

        if (databaseType == DatabaseType.H2) {
            return String.format("BITAND(%s, #{%s}) = #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0], paramPlaceholders[0]);
        }

        if (databaseType == DatabaseType.POSTRESQL) {
            Class fieldType = columnInfo.getField().getType();
            if (CommonsHelper.isNumeric(fieldType)) {
                return String.format("%s & #{%s}::BIGINT = #{%s}::BIGINT", columnInfo.getQueryColumn(), paramPlaceholders[0], paramPlaceholders[0]);
            } else {
                return String.format("%s::BIGINT & #{%s}::BIGINT = #{%s}::BIGINT", columnInfo.getQueryColumn(), paramPlaceholders[0], paramPlaceholders[0]);
            }
        }

        return String.format("%s & #{%s} = #{%s}", columnInfo.getQueryColumn(), paramPlaceholders[0], paramPlaceholders[0]);
    }
}
