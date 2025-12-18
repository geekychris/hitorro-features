package com.hitorro.features.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.hitorro.features.DataType;

/**
 * Mapping functions MUST implement the read from stream AND the setXXX since they could be at the head of the chain and
 * therefor wont be called with the setXXX
 */
public interface MappingFunction<I extends BaseIO, O extends BaseIO> {
    void init(String name, JsonNode props);

    MappingFunction cloneMapper();

    void setNext(O baseIO);

    DataType inputFormat();

    DataType outputFormat();

    String getName();
}
