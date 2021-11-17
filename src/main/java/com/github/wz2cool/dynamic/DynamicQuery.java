package com.github.wz2cool.dynamic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Frank
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
public class DynamicQuery<T> extends BaseDynamicQuery<T, DynamicQuery<T>> {

    private static final long serialVersionUID = -4044703018297658438L;

    public DynamicQuery() {
        // for json
    }

    public DynamicQuery(Class<T> entityClass) {
        this.setEntityClass(entityClass);
    }

    public static <T> DynamicQuery<T> createQuery(Class<T> entityClass) {
        return new DynamicQuery<>(entityClass);
    }
}
