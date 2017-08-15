package com.github.wz2cool.dynamic;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * The type Filter group descriptor.
 */
@JsonTypeName("filterGroupDescriptor")
public class FilterGroupDescriptor extends FilterDescriptorBase implements Serializable {

    private static final long serialVersionUID = 503860190426402699L;
    private FilterDescriptorBase[] filters = new FilterDescriptorBase[]{};

    public FilterDescriptorBase[] getFilters() {
        return filters;
    }

    public void setFilters(FilterDescriptorBase[] filters) {
        this.filters = filters;
    }

    public boolean addFilters(FilterDescriptorBase... newFilters) {
        if (filters == null) {
            filters = new FilterDescriptorBase[]{};
        }

        List<FilterDescriptorBase> filtersCopy = new ArrayList<>(Arrays.asList(filters));
        List<FilterDescriptorBase> newFilterList = Arrays.asList(newFilters);
        boolean result = filtersCopy.addAll(newFilterList);
        this.setFilters(filtersCopy.toArray(new FilterDescriptorBase[filtersCopy.size()]));
        return result;
    }

    public boolean removeFilter(FilterDescriptorBase removeFilter) {
        if (filters == null) {
            filters = new FilterDescriptorBase[]{};
        }
        List<FilterDescriptorBase> filtersCopy = new ArrayList<>(Arrays.asList(filters));
        boolean result = filtersCopy.remove(removeFilter);
        this.setFilters(filtersCopy.toArray(new FilterDescriptorBase[filtersCopy.size()]));
        return result;
    }
}
