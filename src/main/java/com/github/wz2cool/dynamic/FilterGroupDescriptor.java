package com.github.wz2cool.dynamic;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * The type Filter group descriptor.
 * @author Frank
 */
@SuppressWarnings("squid:S1948")
@JsonTypeName("filterGroupDescriptor")
public class FilterGroupDescriptor extends BaseFilterDescriptor implements Serializable {

    private static final long serialVersionUID = 503860190426402699L;
    private BaseFilterDescriptor[] filters = new BaseFilterDescriptor[]{};

    public BaseFilterDescriptor[] getFilters() {
        return filters;
    }

    public void setFilters(BaseFilterDescriptor[] filters) {
        this.filters = filters;
    }

    @SuppressWarnings("Duplicates")
    public boolean addFilters(BaseFilterDescriptor... newFilters) {
        List<BaseFilterDescriptor> filtersCopy =
                filters == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(filters));
        List<BaseFilterDescriptor> newFilterList = Arrays.asList(newFilters);
        boolean result = filtersCopy.addAll(newFilterList);
        if (result) {
            this.setFilters(filtersCopy.toArray(new BaseFilterDescriptor[filtersCopy.size()]));
        }
        return result;
    }

    @SuppressWarnings("Duplicates")
    public boolean removeFilter(BaseFilterDescriptor removeFilter) {
        List<BaseFilterDescriptor> filtersCopy =
                filters == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(filters));
        boolean result = filtersCopy.remove(removeFilter);
        if (result) {
            this.setFilters(filtersCopy.toArray(new BaseFilterDescriptor[filtersCopy.size()]));
        }
        return result;
    }
}
