package com.neoteric.starter.exception.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.ServletRequestBindingException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ServletExceptionMapper implements ExceptionMapper<ServletRequestBindingException> {

    private static final Logger LOG = LoggerFactory.getLogger(ServletExceptionMapper.class);

    @Override
    public Response toResponse(ServletRequestBindingException exception) {
        LOG.warn("Exception mapped to response", exception);

        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(exception.getMessage())
                .build();
    }

}
