package com.github.wz2cool.dynamic;


import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.lambda.GetPropertyFunction;

import java.io.Serializable;

/**
 * The type Sort descriptor.
 */
public class SortDescriptor extends BaseSortDescriptor implements Serializable {
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

    public <T> SortDescriptor(GetPropertyFunction<T> getFieldFunc, SortDirection sortDirection) {
        this.propertyPath = CommonsHelper.getPropertyInfo(getFieldFunc).getPropertyName();
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

    public <T> void setPropertyPath(GetPropertyFunction<T> getFieldFunc) {
        this.propertyPath = CommonsHelper.getPropertyInfo(getFieldFunc).getPropertyName();
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
