package com.neoteric.starter.mongo.request.processors;

import com.neoteric.request.RequestObject;
import com.neoteric.request.RequestObjectType;
import com.neoteric.starter.mongo.request.FieldMapper;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;
import java.util.Map;

public interface MongoRequestObjectProcessor<T extends RequestObject> {

    Boolean apply(RequestObjectType key);

    List<Criteria> build(T field, Map<RequestObject, Object> fieldValues, FieldMapper fieldMapper);
}
