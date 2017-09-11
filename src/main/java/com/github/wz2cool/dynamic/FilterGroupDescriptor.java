package com.github.wz2cool.dynamic;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * The type Filter group descriptor.
 */
@SuppressWarnings("squid:S1948")
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

    @SuppressWarnings("Duplicates")
    public boolean addFilters(FilterDescriptorBase... newFilters) {
        List<FilterDescriptorBase> filtersCopy =
                filters == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(filters));
        List<FilterDescriptorBase> newFilterList = Arrays.asList(newFilters);
        boolean result = filtersCopy.addAll(newFilterList);
        if (result) {
            this.setFilters(filtersCopy.toArray(new FilterDescriptorBase[filtersCopy.size()]));
        }
        return result;
    }

    @SuppressWarnings("Duplicates")
    public boolean removeFilter(FilterDescriptorBase removeFilter) {
        List<FilterDescriptorBase> filtersCopy =
                filters == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(filters));
        boolean result = filtersCopy.remove(removeFilter);
        if (result) {
            this.setFilters(filtersCopy.toArray(new FilterDescriptorBase[filtersCopy.size()]));
        }
        return result;
    }
}
