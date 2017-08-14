package com.github.wz2cool.dynamic;


import java.io.Serializable;

/**
 * The enum Filter operator.
 */
public enum FilterOperator implements Serializable {
    /**
     * Less than filter operator.
     */
    LESS_THAN,
    /**
     * Less than or equal filter operator.
     */
    LESS_THAN_OR_EQUAL,
    /**
     * Equal filter operator.
     */
    EQUAL,
    /**
     * Not equal filter operator.
     */
    NOT_EQUAL,
    /**
     * Greater than or equal filter operator.
     */
    GREATER_THAN_OR_EQUAL,
    /**
     * Greater than filter operator.
     */
    GREATER_THAN,
    /**
     * Start with filter operator.
     */
    START_WITH,
    /**
     * End with filter operator.
     */
    END_WITH,
    /**
     * Contains filter operator.
     */
    CONTAINS,
    /**
     * In filter operator.
     */
    IN,
    /**
     * Not in filter operator.
     */
    NOT_IN,
    /**
     * Between filter operator.
     */
    BETWEEN
}
