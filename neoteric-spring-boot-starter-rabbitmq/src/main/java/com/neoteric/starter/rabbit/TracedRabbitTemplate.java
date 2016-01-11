package com.neoteric.starter.rabbit;

import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Arrays;
import java.util.List;

public class TracedRabbitTemplate extends RabbitTemplate {

    private static final Logger LOG = LoggerFactory.getLogger(TracedRabbitTemplate.class);
    public static final String REQUEST_ID = "REQUEST_ID";

    private final static MessagePostProcessor SET_REQUEST_ID_TO_MESSAGE = message -> {
        LOG.debug("Setting Request ID for message: {}", MDC.get(REQUEST_ID));
        message.getMessageProperties().setHeader(REQUEST_ID, MDC.get(REQUEST_ID));
        return message;
    };

    public TracedRabbitTemplate(ConnectionFactory connectionFactory) {
        super(connectionFactory);
        super.setBeforePublishPostProcessors(SET_REQUEST_ID_TO_MESSAGE);
    }

    @Override
    public void setBeforePublishPostProcessors(MessagePostProcessor... beforePublishPostProcessors) {
        List<MessagePostProcessor> postProcessors = Arrays.asList(beforePublishPostProcessors);
        postProcessors.add(SET_REQUEST_ID_TO_MESSAGE);
        super.setAfterReceivePostProcessors(postProcessors.toArray(new MessagePostProcessor[postProcessors.size()]));
    }
}
