package com.github.wz2cool.dynamic;


/**
 * The type Sort descriptor.
 */
public class SortDescriptor {
    private String propertyPath;
    private SortDirection sortDirection = SortDirection.ASC;

    /**
     * Instantiates a new Sort descriptor.
     */
    public SortDescriptor() {
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
