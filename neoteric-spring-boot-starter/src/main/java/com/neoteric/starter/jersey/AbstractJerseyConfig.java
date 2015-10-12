package com.neoteric.starter.jersey;

import com.neoteric.starter.swagger.SwaggerProperties;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.PostConstruct;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api")
@EnableConfigurationProperties(SwaggerProperties.class)
public abstract class AbstractJerseyConfig extends ResourceConfig {

    private static final String SWAGGER_PACKAGE = "io.swagger.jaxrs.listing";

    @Autowired
    protected SwaggerProperties swaggerProperties;


    protected abstract void configure();

    @PostConstruct // in constructor we can't inject properties
    public void register() {
        register(ObjectMapperProvider.class);
        if (swaggerProperties.isEnabled()) {
            this.packages(SWAGGER_PACKAGE);
        }
        configure();
    }
}