package com.neoteric.starter.mongo;

import org.springframework.data.mongodb.core.query.Criteria;

@FunctionalInterface
public interface LogicalOperatorFunction {
    Criteria apply(Criteria initial, Criteria... criteria);
}
