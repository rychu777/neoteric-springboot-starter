package com.neoteric.starter.mongo.request.processors.fields;

import com.neoteric.request.RequestField;
import com.neoteric.request.RequestObjectType;
import com.neoteric.request.RequestOperator;
import org.springframework.data.mongodb.core.query.Criteria;

import static com.neoteric.starter.mongo.request.Mappings.OPERATORS;

public enum MongoFieldToOperatorSubProcessor implements MongoFieldSubProcessor<RequestOperator> {

    INSTANCE;

    @Override
    public Boolean apply(RequestObjectType key) {
        return RequestObjectType.OPERATOR.equals(key);
    }

    @Override
    public Criteria build(RequestField field, RequestOperator operator, Object operatorValue) {
        Criteria fieldCriteria = Criteria.where(field.getFieldName());
        // TODO : how to check value, it can be String, Boolean, Integer, List !!, Double and maybe more
        return OPERATORS.get(operator.getOperator()).apply(fieldCriteria, operatorValue);
    }
}
