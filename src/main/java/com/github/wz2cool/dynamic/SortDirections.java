package com.github.wz2cool.dynamic;


import com.github.wz2cool.dynamic.builder.direction.Ascending;
import com.github.wz2cool.dynamic.builder.direction.Descending;

/**
 * The enum Sort direction.
 *
 * @author Frank
 */
public final class SortDirections {
    /// region sort direction

    private static final Ascending ASC = new Ascending();
    private static final Descending DESC = new Descending();

    public Ascending asc() {
        return ASC;
    }

    public Descending desc() {
        return DESC;
    }

    /// endregion
}

