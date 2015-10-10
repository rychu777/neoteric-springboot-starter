package com.neoteric.starter.jersey;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

@Component
@ApplicationPath("/api")
public abstract class AbstractJerseyConfig extends ResourceConfig {

    public AbstractJerseyConfig() {
        register(ObjectMapperProvider.class);
        configure();
    }

    protected abstract void configure();
}