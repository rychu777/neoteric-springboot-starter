package com.neoteric.starter.request;

import com.neoteric.starter.request.params.RequestParametersFilter;
import com.neoteric.starter.request.tracing.RequestIdFilter;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RequestAutoConfiguration {

    @Bean
    FilterRegistrationBean registerRequestIdFilter() {
        return new FilterRegistrationBean(new RequestIdFilter());
    }

    @Bean
    FilterRegistrationBean registerRequestParamsFilter() {
        return new FilterRegistrationBean(new RequestParametersFilter());
    }
}

