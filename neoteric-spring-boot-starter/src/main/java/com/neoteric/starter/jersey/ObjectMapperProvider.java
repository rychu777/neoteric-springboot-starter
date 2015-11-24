package com.neoteric.starter.jersey;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;


//TODO: Check - not needed since 1.3.0.RC1
@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return objectMapper;
    }
}