package com.github.wz2cool.dynamic.builder.direction;

import com.github.wz2cool.dynamic.SortDirection;

/**
 * @author Frank
 */
public class Descending implements ISortDirection {
    @Override
    public SortDirection getDirection() {
        return SortDirection.DESC;
    }
}
