package com.neoteric.starter.mongo.request;

import com.google.common.collect.ImmutableMap;
import com.neoteric.request.OperatorType;

public class OperatorMapper {
    static private ImmutableMap<OperatorType, String> MAPPING = ImmutableMap.<OperatorType, String>builder()
            .put(OperatorType.EQUAL, "$eq")
            .put(OperatorType.IN, "$in")
            .put(OperatorType.LESS_THAN, "$lt")
            .put(OperatorType.STARTS_WITH, "$regex")
            .build();

    public static String get(OperatorType operatorType) {
        if (!MAPPING.containsKey(operatorType)) {
            throw new IllegalStateException("No Mongo operator for: " + operatorType.getName());
        }
        return MAPPING.get(operatorType);
    }

}
