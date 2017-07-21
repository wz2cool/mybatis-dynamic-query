package com.github.wz2cool.dynamic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


/**
 * The type Filter group descriptor.
 */
public class FilterGroupDescriptor extends FilterDescriptorBase {

    private final List<FilterDescriptorBase> filters = new ArrayList<>();

    /**
     * Get filters filter descriptor base [ ].
     *
     * @return the filter descriptor base [ ]
     */
    public FilterDescriptorBase[] getFilters() {
        return filters.toArray(new FilterDescriptorBase[filters.size()]);
    }

    /**
     * Add filters boolean.
     *
     * @param newFilters the new filters
     * @return the boolean
     */
    public boolean addFilters(FilterDescriptorBase... newFilters) {
        if (newFilters == null) {
            return false;
        }

        return addFilters(Arrays.asList(newFilters));
    }

    /**
     * Add filters boolean.
     *
     * @param newFilters the new filters
     * @return the boolean
     */
    public boolean addFilters(Collection<FilterDescriptorBase> newFilters) {
        if (newFilters == null) {
            return false;
        }

        return filters.addAll(newFilters);
    }

    /**
     * Remove filter boolean.
     *
     * @param filterDescriptor the filter descriptor
     * @return the boolean
     */
    public boolean removeFilter(FilterDescriptorBase filterDescriptor) {
        return filters.remove(filterDescriptor);
    }
}
