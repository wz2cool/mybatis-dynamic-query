package com.github.wz2cool.dynamic;


import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.lambda.GetPropertyFunction;

import java.io.Serializable;

/**
 * The type Sort descriptor.
 *
 * @author Frank
 */
public class SortDescriptor extends BaseSortDescriptor implements Serializable {
    private static final long serialVersionUID = 819843464658066502L;

    private String propertyName;
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
     * @param propertyName  the property path
     * @param sortDirection the sort direction
     */
    public SortDescriptor(String propertyName, SortDirection sortDirection) {
        this.propertyName = propertyName;
        this.sortDirection = sortDirection;
    }

    public <T> SortDescriptor(GetPropertyFunction<T, Comparable> getFieldFunc, SortDirection sortDirection) {
        this.propertyName = CommonsHelper.getPropertyInfo(getFieldFunc).getPropertyName();
        this.sortDirection = sortDirection;
    }

    /**
     * Gets property path.
     *
     * @return the property path
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Sets property path.
     *
     * @param propertyName the property path
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public <T> void setPropertyPath(GetPropertyFunction<T, Comparable> getFieldFunc) {
        this.propertyName = CommonsHelper.getPropertyInfo(getFieldFunc).getPropertyName();
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
