package com.neoteric.starter.mongo;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.neoteric.request.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;
import java.util.Map;

public class RequestParamsCriteriaBuilder {

    private Criteria criteria;

    static ImmutableMap<LogicalOperatorType, LogicalOperatorFunction> LOGICAL_OPERATOR_MAPPING = ImmutableMap.<LogicalOperatorType, LogicalOperatorFunction>builder()
            .put(LogicalOperatorType.OR, Criteria::orOperator)
            .build();

    private static final Logger LOG = LoggerFactory.getLogger(RequestParamsCriteriaBuilder.class);

    private RequestParamsCriteriaBuilder(Criteria criteria) {
        this.criteria = criteria;
    }

    public static RequestParamsCriteriaBuilder newBuilder() {
        return new RequestParamsCriteriaBuilder(new Criteria());
    }


    public Criteria build(Map<RequestObject, Object> requestParams) {
        return build(requestParams, FieldMapper.of(Maps.<String, String>newHashMap()));
    }

    public Criteria build(Map<RequestObject, Object> requestParams, FieldMapper fieldMapper) {

        requestParams.forEach(((key, value) -> {
            LOG.info("KEY: {}. VALUE: {}", key, value);
            LOG.info("KEY: {}. VALUE: {}", key.getClass(), value.getClass());
            if (key.getType().equals(RequestObjectType.FIELD)) {
                criteria.and(fieldMapper.get(((RequestField) key).getFieldName()));
            } else if (key.getType().equals((RequestObjectType.LOGICAL_OPERATOR))) {
                //Check and throw if not in LOGICAL_OPERATOR_MAPPING
                LOGICAL_OPERATOR_MAPPING.get(((RequestLogicalOperator) key).getOperator()).apply(criteria, resolveRootLogicalOperatorCriteria((Map) value));
            } else {
                throw new IllegalStateException("BAD TYPE");
            }
        }));

        return criteria;
    }

    private Criteria[] resolveRootLogicalOperatorCriteria(Map<RequestObject, Object> logicalOperatorElements) {

        List<Criteria> criteriaList = Lists.newArrayList();

        logicalOperatorElements.forEach((key, value) -> {
            LOG.info("KEY: {}. VALUE: {}", key, value);
            LOG.info("KEY: {}. VALUE: {}", key.getClass(), value.getClass());

            if (!(key instanceof RequestField)) {
                throw new IllegalStateException("NOOOT");
            }
            RequestField field = (RequestField)key;

            Criteria newFieldCriteria = Criteria.where(field.getFieldName());

            criteriaList.add(newFieldCriteria);
        });
        return criteriaList.toArray(new Criteria[criteriaList.size()]);
    }
}
