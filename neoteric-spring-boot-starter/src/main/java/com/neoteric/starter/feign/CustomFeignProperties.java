package com.neoteric.starter.feign;

import feign.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("feign")
public class CustomFeignProperties {

    /**
     * Controls the level of logging. Possible values:
     *  NONE - No logging.
     *  BASIC - Log only the request method and URL and the response status code and execution time.
     *  HEADERS - Log the basic information along with request and response headers.
     *  FULL - Log the headers, body, and metadata for both requests and responses.
     */

    private String loggerLevel = Logger.Level.BASIC.toString();

    public String getLoggerLevel() {
        return loggerLevel;
    }

    public void setLoggerLevel(String loggerLevel) {
        this.loggerLevel = loggerLevel;
    }
}