package com.neoteric.starter.jms.producers;

import com.neoteric.starter.jms.QueueTemplate;
import org.slf4j.MDC;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;

public class QueueMessageProducer {

    private static final String REQUEST_ID = "REQUEST_ID";

    @QueueTemplate
    private JmsTemplate jmsTemplate;

    public void send(String queueDestination, Object objectToSend, MessagePostProcessor messagePostProcessor) {
        jmsTemplate.convertAndSend(queueDestination, objectToSend, mergedMessagePostProcessing(messagePostProcessor));
    }

    public void send(String queueDestination, Object objectToSend) {
        jmsTemplate.convertAndSend(queueDestination, objectToSend, defaultMessagePostProcessing());
    }

    private MessagePostProcessor mergedMessagePostProcessing(MessagePostProcessor messagePostProcessor) {
        return message -> {
            message = messagePostProcessor.postProcessMessage(message);
            message = defaultMessagePostProcessing().postProcessMessage(message);
            return message;
        };
    }

    private MessagePostProcessor defaultMessagePostProcessing() {
        return message -> {
            message = addRequestIdHeader().postProcessMessage(message);
            return message;
        };
    }

    private MessagePostProcessor addRequestIdHeader() {
        return message -> {
            message.setStringProperty(REQUEST_ID, MDC.get(REQUEST_ID));
            return message;
        };
    }

}
