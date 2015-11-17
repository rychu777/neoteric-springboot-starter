package com.neoteric.starter.request.params;

import com.neoteric.request.RequestParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;

public final class RequestParametersBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(RequestParametersBuilder.class);
    private static final String FIRST_PARAM = "first";
    private static final String PAGE_SIZE_PARAM = "pageSize";
    public static final String FILTERS_PARAM = "filters";

    private RequestParametersBuilder() {
        // Prevents instantiation of this class
    }

    public static RequestParameters buildFrom(HttpServletRequest request) throws ServletRequestBindingException {
        int first = ServletRequestUtils.getIntParameter(request, FIRST_PARAM, 0);
        int pageSize = ServletRequestUtils.getIntParameter(request, PAGE_SIZE_PARAM, 0);
        String filters = ServletRequestUtils.getStringParameter(request, FILTERS_PARAM);


        return RequestParameters.builder()
                .setFirst(first)
                .setPageSize(pageSize)
                .build();
    }
}
