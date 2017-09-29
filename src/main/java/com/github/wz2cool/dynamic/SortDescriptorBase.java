package com.github.wz2cool.dynamic;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SortDescriptor.class, name = "SortDescriptor"),
        @JsonSubTypes.Type(value = CustomSortDescriptor.class, name = "CustomSortDescriptor")
})
public abstract class SortDescriptorBase {
}