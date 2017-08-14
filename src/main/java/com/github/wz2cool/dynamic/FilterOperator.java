package com.github.wz2cool.dynamic;


import java.io.Serializable;

/**
 * The enum Filter operator.
 */
public enum FilterOperator implements Serializable {
    /**
     * Less than filter operator.
     */
    LESS_THAN("LESS_THAN", 1),
    /**
     * Less than or equal filter operator.
     */
    LESS_THAN_OR_EQUAL("LESS_THAN_OR_EQUAL", 2),
    /**
     * Equal filter operator.
     */
    EQUAL("EQUAL", 3),
    /**
     * Not equal filter operator.
     */
    NOT_EQUAL("NOT_EQUAL", 4),
    /**
     * Greater than or equal filter operator.
     */
    GREATER_THAN_OR_EQUAL("GREATER_THAN_OR_EQUAL", 5),
    /**
     * Greater than filter operator.
     */
    GREATER_THAN("GREATER_THAN", 6),
    /**
     * Start with filter operator.
     */
    START_WITH("START_WITH", 7),
    /**
     * End with filter operator.
     */
    END_WITH("END_WITH", 8),
    /**
     * Contains filter operator.
     */
    CONTAINS("CONTAINS", 9),
    /**
     * In filter operator.
     */
    IN("IN", 10),
    /**
     * Not in filter operator.
     */
    NOT_IN("NOT_IN", 11),
    /**
     * Between filter operator.
     */
    BETWEEN("BETWEEN", 12);

    private String name;
    private int value;

    FilterOperator(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
