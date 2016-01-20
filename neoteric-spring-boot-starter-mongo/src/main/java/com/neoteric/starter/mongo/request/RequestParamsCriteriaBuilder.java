package com.neoteric.starter.mongo.request;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.neoteric.request.RequestObject;
import com.neoteric.starter.mongo.request.processors.MongoRequestFieldProcessor;
import com.neoteric.starter.mongo.request.processors.MongoRequestLogicalOperatorProcessor;
import com.neoteric.starter.mongo.request.processors.MongoRequestObjectProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;
import java.util.Map;

public class RequestParamsCriteriaBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(RequestParamsCriteriaBuilder.class);
    private static final List<MongoRequestObjectProcessor> REQUEST_OBJECT_PROCESSORS = ImmutableList.of(
            MongoRequestFieldProcessor.INSTANCE, MongoRequestLogicalOperatorProcessor.INSTANCE
    );

    public static RequestParamsCriteriaBuilder newBuilder() {
        return new RequestParamsCriteriaBuilder();
    }

    public Criteria build(Map<RequestObject, Object> requestParams) {
        return build(requestParams, FieldMapper.of(Maps.<String, String>newHashMap()));
    }

    public Criteria build(Map<RequestObject, Object> requestParams, FieldMapper fieldMapper) {
        List<Criteria> joinedCriteria = Lists.newArrayList();

        requestParams.forEach(((key, value) -> {
            if (!(value instanceof Map)) {
                throw new IllegalArgumentException("Root RequestObject expect Map as argument, but get: " + value);
            }
            List<Criteria> fieldCriteria = REQUEST_OBJECT_PROCESSORS.stream()
                    .filter(mongoRequestObjectProcessor -> mongoRequestObjectProcessor.apply(key.getType()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Illegal Root type: " + key.getType()))
                    .build(key, (Map) value);
            joinedCriteria.addAll(fieldCriteria);
        }));
        Criteria criteria = new Criteria();
        if(!joinedCriteria.isEmpty()){
            criteria = criteria.andOperator(joinedCriteria.stream().toArray(Criteria[]::new));
        }
        return criteria;
    }
}
