package com.frwan.query.dynamic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by Frank on 2017/4/15.
 */
public class FilterGroupDescriptor extends FilterDescriptorBase {

    private final List<FilterDescriptorBase> filters = new ArrayList<>();

    public FilterDescriptorBase[] getFilters() {
        return filters.toArray(new FilterDescriptorBase[filters.size()]);
    }

    public boolean addFilters(FilterDescriptorBase... newFilters) {
        if (newFilters == null) {
            return false;
        }

        return addFilters(Arrays.asList(newFilters));
    }

    public boolean addFilters(Collection<FilterDescriptorBase> newFilters) {
        if (newFilters == null) {
            return false;
        }

        return filters.addAll(newFilters);
    }

    public boolean removeFilter(FilterDescriptorBase filterDescriptor) {
        return filters.remove(filterDescriptor);
    }
}
