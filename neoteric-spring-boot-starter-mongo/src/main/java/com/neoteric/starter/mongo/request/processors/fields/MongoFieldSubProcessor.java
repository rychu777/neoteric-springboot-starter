package com.neoteric.starter.mongo.request.processors.fields;

import com.neoteric.request.RequestField;
import com.neoteric.request.RequestObject;
import com.neoteric.request.RequestObjectType;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

public interface MongoFieldSubProcessor<T extends RequestObject> {

    Boolean apply(RequestObjectType key);

    Criteria build(RequestField parentField, T field, Object fieldValues);
}
