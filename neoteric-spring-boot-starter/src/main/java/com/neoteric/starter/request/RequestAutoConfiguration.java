package com.neoteric.starter.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoteric.starter.ConfigBeans;
import com.neoteric.starter.Constants;
import com.neoteric.starter.jackson.rison.RisonFactory;
import com.neoteric.starter.request.params.RequestParametersFilter;
import com.neoteric.starter.request.tracing.RequestIdFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RequestProperties.class)
@AutoConfigureAfter(JacksonAutoConfiguration.class)
public class RequestAutoConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(RequestAutoConfiguration.class);

    @Autowired
    RequestProperties requestProperties;

    @Autowired
    ObjectMapper objectMapper;

    @Bean
    FilterRegistrationBean registerRequestIdFilter() {
        return new FilterRegistrationBean(new RequestIdFilter());
    }

    @Bean
    FilterRegistrationBean registerRequestParamsFilter() throws Exception {
        String filtersFormat = requestProperties.getFiltersFormat();
        ObjectMapper requestMapper;
        switch(filtersFormat) {
            case RequestProperties.RISON:
                LOG.trace("{}Picked Rison Mapper", Constants.LOG_PREFIX);
                requestMapper = new ObjectMapper(new RisonFactory());
                break;
            case RequestProperties.JSON:
                LOG.trace("{}Picked default Json Mapper", Constants.LOG_PREFIX);
                requestMapper = objectMapper;
                break;
            default:
                LOG.error("{}{} format not supported.", Constants.LOG_PREFIX, filtersFormat);
                throw new Exception(filtersFormat + " not supported.");
        }

        return new FilterRegistrationBean(new RequestParametersFilter(requestMapper));
    }
}