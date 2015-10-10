package com.neoteric.starter.jersey;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.ext.ContextResolver;

@Component
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return objectMapper;
    }
}