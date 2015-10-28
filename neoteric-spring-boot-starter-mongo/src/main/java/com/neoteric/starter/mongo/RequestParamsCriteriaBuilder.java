package com.neoteric.starter.mongo;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.neoteric.request.LogicalOperatorType;
import com.neoteric.request.RequestField;
import com.neoteric.request.RequestObject;
import com.neoteric.request.RequestObjectType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Map;

public class RequestParamsCriteriaBuilder {

    private Criteria criteria = new Criteria();

    static ImmutableMap<LogicalOperatorType, LogicalOperatorFunction> logicOperatorMapping = ImmutableMap.<LogicalOperatorType, LogicalOperatorFunction>builder()
            .put(LogicalOperatorType.OR, (initial, criteria) -> initial.orOperator(criteria))
            .build();

    private static final Logger LOG = LoggerFactory.getLogger(RequestParamsCriteriaBuilder.class);

    public Criteria build(Map<RequestObject, Object> requestParams) {
        return build(requestParams, FieldMapper.of(Maps.<String, String>newHashMap()));
    }

    public Criteria build(Map<RequestObject, Object> requestParams, FieldMapper fieldMapper) {

        requestParams.forEach(((key, value) -> {
            LOG.info("KEY: {}. VALUE: {}", key, value.getClass());
            if (key.getType().equals(RequestObjectType.FIELD)) {
                criteria.and(fieldMapper.get(((RequestField) key).getFieldName()));
            } else if (key.getType().equals((RequestObjectType.LOGICAL_OPERATOR))) {
//                logicOperatorMapping.get(key).apply(criteria, )
            } else {
                throw new IllegalStateException("BAD TYPE");
            }
        }));

        return criteria;
    }
}
