package com.neoteric.starter.feign;

import feign.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("feign")
public class FeignProperties {

    private String loggerLevel = Logger.Level.BASIC.toString();

    public String getLoggerLevel() {
        return loggerLevel;
    }

    public void setLoggerLevel(String loggerLevel) {
        this.loggerLevel = loggerLevel;
    }
}