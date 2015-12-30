package com.neoteric.starter.jms.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ErrorHandler;

public class DefaultErrorHandler implements ErrorHandler {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultErrorHandler.class);

    @Override
    public void handleError(Throwable t) {
        LOG.error("Error processing JMS Message: {}", t.getMessage(), t);
    }
}