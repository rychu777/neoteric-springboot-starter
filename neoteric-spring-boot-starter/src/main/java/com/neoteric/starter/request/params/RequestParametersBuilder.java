package com.neoteric.starter.request.params;

import com.neoteric.request.RequestParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public abstract class RequestParametersBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(RequestParametersBuilder.class);

    private RequestParametersBuilder() {
    }

    public static RequestParameters buildFrom(HttpServletRequest request) {

        LOG.warn("QueryString: {}", request.getQueryString());
        LOG.warn("PathInfo: {}", request.getPathInfo());
        LOG.warn("RequestURI: {}", request.getRequestURI());

        return RequestParameters.builder()
                .build();
    }
}
