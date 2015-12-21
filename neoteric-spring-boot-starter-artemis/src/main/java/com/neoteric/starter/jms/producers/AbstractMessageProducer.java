package com.neoteric.starter.jms.producers;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;

public abstract class AbstractMessageProducer {

    protected JmsTemplate jmsTemplate;

    public void send(String queueDestination, Object objectToSend, MessagePostProcessor messagePostProcessor) {
        jmsTemplate.convertAndSend(queueDestination, objectToSend, messagePostProcessor);
    }

    public void send(String queueDestination, Object objectToSend) {
        jmsTemplate.convertAndSend(queueDestination, objectToSend);
    }

    protected abstract void setJmsTemplate(JmsTemplate jmsTemplate);
}
