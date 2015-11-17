package com.neoteric.starter.request;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("request")
public class RequestProperties {

    static final String RISON = "rison";
    static final String JSON = "json";
    /**
     * Filters query parameter format. Available are two types: rison / json
     */
    private String filtersFormat = RISON;


    public String getFiltersFormat() {
        return filtersFormat;
    }

    public void setFiltersFormat(String filtersFormat) {
        this.filtersFormat = filtersFormat;
    }
}

