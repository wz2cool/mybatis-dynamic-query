package com.frwan.query.dynamic;

/**
 * Created by Frank on 3/21/2017.
 */
public class SortDescriptor {
    private String propertyPath;
    private SortDirection sortDirection = SortDirection.ASC;

    public SortDescriptor() {
        // just create empty instance.
    }

    public SortDescriptor(String propertyPath, SortDirection sortDirection) {
        this.propertyPath = propertyPath;
        this.sortDirection = sortDirection;
    }

    public String getPropertyPath() {
        return propertyPath;
    }

    public void setPropertyPath(String propertyPath) {
        this.propertyPath = propertyPath;
    }

    public SortDirection getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(SortDirection sortDirection) {
        this.sortDirection = sortDirection;
    }
}
