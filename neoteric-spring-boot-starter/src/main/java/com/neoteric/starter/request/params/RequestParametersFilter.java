package com.neoteric.starter.request.params;

import com.neoteric.request.RequestParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public final class RequestParametersFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(RequestParametersFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        initHolder(request,  RequestParametersBuilder.buildFrom(request));

        try {
            filterChain.doFilter(request, response);
        } finally {
            resetHolder();
            LOG.debug("Cleared thread-bound request parameters: {}", request);
        }
    }

    private void initHolder(HttpServletRequest request, RequestParameters requestParameters) {
        RequestParametersHolder.set(requestParameters);
        LOG.debug("Bound request parameters to thread: {}", request);
    }

    private void resetHolder() {
        RequestParametersHolder.reset();
    }
}
