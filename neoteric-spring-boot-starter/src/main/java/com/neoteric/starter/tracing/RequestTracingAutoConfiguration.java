package com.neoteric.starter.tracing;

import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RequestTracingAutoConfiguration {

    @Bean
    FilterRegistrationBean registerRequestIdFilter() {
        return new FilterRegistrationBean(new RequestIdFilter());
    }
}

