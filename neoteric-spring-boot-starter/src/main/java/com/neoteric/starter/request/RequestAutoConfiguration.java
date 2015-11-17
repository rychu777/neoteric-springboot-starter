package com.neoteric.starter.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoteric.starter.ConfigBeans;
import com.neoteric.starter.jackson.rison.RisonFactory;
import com.neoteric.starter.request.params.RequestParametersFilter;
import com.neoteric.starter.request.tracing.RequestIdFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RequestProperties.class)
public class RequestAutoConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(RequestAutoConfiguration.class);

    @Autowired
    RequestProperties requestProperties;

    @Autowired
    ObjectMapper objectMapper;

    @Bean(name = ConfigBeans.REQUEST_MAPPER)
    ObjectMapper registerRequestMapper() throws Exception {
        String filtersFormat = requestProperties.getFiltersFormat();
        switch(filtersFormat) {
            case RequestProperties.RISON:
                return new ObjectMapper(new RisonFactory());
            case RequestProperties.JSON:
                return objectMapper;
            default:
                LOG.error("{} format not supported.");
                throw new Exception(filtersFormat + " not supported.");
        }
    }


    @Bean
    FilterRegistrationBean registerRequestIdFilter() {
        return new FilterRegistrationBean(new RequestIdFilter());
    }

    @Bean
    FilterRegistrationBean registerRequestParamsFilter() {
        return new FilterRegistrationBean(new RequestParametersFilter());
    }
}

