package com.neoteric.starter.date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.ZoneId;
import java.util.TimeZone;

@Configuration
public class DateTimeAutoConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(DateTimeAutoConfiguration.class);

    @PostConstruct
    public static void init() {
        LOG.debug("Setting default timezone to UTC");
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("UTC")));
    }
}