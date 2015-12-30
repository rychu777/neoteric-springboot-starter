package com.neoteric.starter.jms.producers;

import com.neoteric.starter.jms.QueueJmsTemplate;
import org.springframework.jms.core.JmsTemplate;

public final class QueueMessageProducer extends AbstractMessageProducer {

    @Override
    @QueueJmsTemplate
    protected void setJmsTemplate(JmsTemplate jmsTemplate) {
        super.jmsTemplate = jmsTemplate;
    }
}