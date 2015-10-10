package com.neoteric.starter.tracing;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;

public class RequestIdAppendInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(RequestIdFilter.REQUEST_ID, MDC.get(RequestIdFilter.REQUEST_ID));
    }
}