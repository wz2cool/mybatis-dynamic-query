package com.github.wz2cool.dynamic.model;

/**
 * @author Frank
 */
public class PropertyInfo {
    private String propertyName;
    private Class<?> ownerClass;

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Class<?> getOwnerClass() {
        return ownerClass;
    }

    public void setOwnerClass(Class<?> ownerClass) {
        this.ownerClass = ownerClass;
    }
}
