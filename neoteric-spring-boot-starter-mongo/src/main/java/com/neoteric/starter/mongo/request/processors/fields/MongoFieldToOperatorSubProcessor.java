package com.neoteric.starter.mongo.request.processors.fields;

import com.neoteric.request.RequestField;
import com.neoteric.request.RequestObjectType;
import com.neoteric.request.RequestOperator;
import com.neoteric.starter.mongo.request.FieldMapper;
import org.springframework.data.mongodb.core.query.Criteria;

import static com.neoteric.starter.mongo.request.Mappings.OPERATORS;

public enum MongoFieldToOperatorSubProcessor implements MongoFieldSubProcessor<RequestOperator> {

    INSTANCE;

    @Override
    public Boolean apply(RequestObjectType key) {
        return RequestObjectType.OPERATOR.equals(key);
    }

    @Override
    // TODO : how to check value, it can be String, Boolean, Integer, List !!, Double and maybe more
    public Criteria build(RequestField field, RequestOperator operator, Object operatorValue, FieldMapper fieldMapper) {
        String remappedName = fieldMapper.get(field.getFieldName());
        Criteria fieldCriteria = Criteria.where(remappedName);
        return OPERATORS.get(operator.getOperator()).apply(fieldCriteria, operatorValue);
    }
}
