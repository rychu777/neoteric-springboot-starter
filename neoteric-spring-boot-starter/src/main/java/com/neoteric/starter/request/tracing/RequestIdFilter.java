package com.neoteric.starter.request.tracing;

import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * Servlet filter to pick up ID of the request. If not found, generates new one and propagates.
 * Available in MDC by REQUEST_ID key
 */
@WebFilter
public class RequestIdFilter implements Filter {

    public static final String REQUEST_ID = "REQUEST_ID";
    private static final Logger LOG = LoggerFactory.getLogger(RequestIdFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String requestId = httpServletRequest.getHeader(REQUEST_ID);

        if (requestId == null || requestId.isEmpty()) {
            requestId = UUID.randomUUID().toString();
            LOG.trace("Request ID header not found. Assigning new Request ID: [{}]", requestId);
        } else {
            LOG.trace("Request ID header found: [{}].", requestId);
        }

        MDC.put(REQUEST_ID, requestId);
        httpServletResponse.setHeader(REQUEST_ID, requestId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(REQUEST_ID);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}