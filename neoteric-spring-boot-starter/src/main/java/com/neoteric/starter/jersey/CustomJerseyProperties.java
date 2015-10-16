package com.neoteric.starter.jersey;


import org.springframework.boot.autoconfigure.jersey.JerseyProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("jersey")
public class CustomJerseyProperties extends JerseyProperties {

    /**
     * Jersey Base URI - takes precedense over ApplicationPath annotation
     */
    private String baseURI = "/api";

    public String getBaseURI() {
        return baseURI;
    }

    public void setBaseURI(String baseURI) {
        this.baseURI = baseURI;
    }
}
