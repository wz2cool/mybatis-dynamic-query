package com.github.wz2cool.dynamic.builder;

import com.github.wz2cool.dynamic.*;
import com.github.wz2cool.dynamic.builder.opeartor.IFilterOperator;
import com.github.wz2cool.dynamic.helper.CommonsHelper;
import com.github.wz2cool.dynamic.lambda.GetPropertyFunction;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Frank
 */
public class ConditionClauseBuilder<T> extends BaseConditionClauseBuilder<ConditionClauseBuilder<T>, T> {

    private final FilterCondition condition;
    private final String propertyName;
    private final FilterOperator filterOperator;
    private final Object value;
    private final ConditionClauseBuilder<T>[] subConditionClauseBuilders;

    public <R extends Comparable> ConditionClauseBuilder(
            FilterCondition condition,
            GetPropertyFunction<T, R> getPropertyFunction,
            IFilterOperator<R> operator,
            ConditionClauseBuilder<T>[] subConditionClauseBuilders) {
        this.condition = condition;
        this.propertyName = CommonsHelper.getPropertyName(getPropertyFunction);
        this.filterOperator = operator.getOperator();
        this.value = operator.getValue();
        this.subConditionClauseBuilders = subConditionClauseBuilders;
    }

    public BaseFilterDescriptor toFilter() {
        FilterDescriptor filterDescriptor = new FilterDescriptor();

        filterDescriptor.setCondition(this.condition);
        filterDescriptor.setPropertyName(this.propertyName);
        filterDescriptor.setOperator(this.filterOperator);
        filterDescriptor.setValue(this.value);

        if (ArrayUtils.isEmpty(subConditionClauseBuilders)) {
            return filterDescriptor;
        }

        FilterGroupDescriptor filterGroupDescriptor = new FilterGroupDescriptor();
        filterGroupDescriptor.setCondition(this.condition);
        filterGroupDescriptor.addFilters(filterDescriptor);

        for (ConditionClauseBuilder<T> subClause : subConditionClauseBuilders) {
            filterGroupDescriptor.addFilters(subClause.toFilter());
        }
        return filterGroupDescriptor;
    }
}
