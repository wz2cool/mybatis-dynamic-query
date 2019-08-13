package com.github.wz2cool.dynamic.builder;

import com.github.wz2cool.dynamic.BaseSortDescriptor;
import com.github.wz2cool.dynamic.DynamicQuery;
import com.github.wz2cool.dynamic.SortDescriptor;
import com.github.wz2cool.dynamic.builder.direction.Ascending;
import com.github.wz2cool.dynamic.builder.direction.ISortDirection;
import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.lambda.GetCommonPropertyFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Frank
 */
public class OrderByClauseBuilder<T> implements IDynamicQueryBuilder<T> {

    private final List<BaseSortDescriptor> sortDescriptors = new ArrayList<>();
    private final DynamicQueryBuilder<T> dynamicQueryBuilder;

    public OrderByClauseBuilder(
            DynamicQueryBuilder<T> dynamicQueryBuilder,
            GetCommonPropertyFunction<T> getPropertyFunction, ISortDirection sortDirection) {
        this.dynamicQueryBuilder = dynamicQueryBuilder;
        dynamicQueryBuilder.setOrderByClauseBuilder(this);
        addSort(getPropertyFunction, sortDirection);
    }

    public OrderByClauseBuilder(
            DynamicQueryBuilder<T> dynamicQueryBuilder,
            GetCommonPropertyFunction<T> getPropertyFunction) {
        this.dynamicQueryBuilder = dynamicQueryBuilder;
        dynamicQueryBuilder.setOrderByClauseBuilder(this);
        addSort(getPropertyFunction, new Ascending());
    }

    public OrderByClauseBuilder<T> thenBy(
            GetCommonPropertyFunction<T> getPropertyFunction, ISortDirection sortDirection) {
        addSort(getPropertyFunction, sortDirection);
        return this;
    }

    public OrderByClauseBuilder<T> thenBy(
            GetCommonPropertyFunction<T> getPropertyFunction) {
        addSort(getPropertyFunction, new Ascending());
        return this;
    }

    @Override
    public DynamicQuery<T> build() {
        return dynamicQueryBuilder.build();
    }

    BaseSortDescriptor[] getSorts() {
        return this.sortDescriptors.toArray(new BaseSortDescriptor[0]);
    }

    private void addSort(GetCommonPropertyFunction<T> getPropertyFunction, ISortDirection sortDirection) {
        String propertyName = CommonsHelper.getPropertyName(getPropertyFunction);
        SortDescriptor sortDescriptor = new SortDescriptor();
        sortDescriptor.setPropertyName(propertyName);
        sortDescriptor.setDirection(sortDirection.getDirection());
        this.sortDescriptors.add(sortDescriptor);
    }
}
