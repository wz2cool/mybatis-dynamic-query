package com.github.wz2cool.dynamic;


import com.github.wz2cool.helper.CommonsHelper;

import java.io.Serializable;
import java.util.function.Function;

/**
 * The type Sort descriptor.
 */
public class SortDescriptor implements Serializable {
    private static final long serialVersionUID = 819843464658066502L;

    private String propertyPath;
    private SortDirection sortDirection = SortDirection.ASC;

    /**
     * Instantiates a new Sort descriptor.
     */
    public SortDescriptor() {
        // create a empty instance.
    }

    /**
     * Instantiates a new Sort descriptor.
     *
     * @param propertyPath  the property path
     * @param sortDirection the sort direction
     */
    public SortDescriptor(String propertyPath, SortDirection sortDirection) {
        this.propertyPath = propertyPath;
        this.sortDirection = sortDirection;
    }

    public <T> SortDescriptor(Class<T> entityClass, Function<T, Object> getFieldFunc, SortDirection sortDirection) {
        this.propertyPath = CommonsHelper.getPropertyName(entityClass, getFieldFunc);
        this.sortDirection = sortDirection;
    }

    /**
     * Gets property path.
     *
     * @return the property path
     */
    public String getPropertyPath() {
        return propertyPath;
    }

    /**
     * Sets property path.
     *
     * @param propertyPath the property path
     */
    public void setPropertyPath(String propertyPath) {
        this.propertyPath = propertyPath;
    }

    public <T> void setPropertyPath(Class<T> entityClass, Function<T, Object> getFieldFunc) {
        this.propertyPath = CommonsHelper.getPropertyName(entityClass, getFieldFunc);
    }

    /**
     * Gets sort direction.
     *
     * @return the sort direction
     */
    public SortDirection getSortDirection() {
        return sortDirection;
    }

    /**
     * Sets sort direction.
     *
     * @param sortDirection the sort direction
     */
    public void setSortDirection(SortDirection sortDirection) {
        this.sortDirection = sortDirection;
    }
}
