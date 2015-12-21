package com.neoteric.starter.jms.producers;

import com.neoteric.starter.jms.TopicJmsTemplate;
import org.springframework.jms.core.JmsTemplate;

public class TopicMessageProducer extends AbstractMessageProducer {

    @Override
    @TopicJmsTemplate
    protected void setJmsTemplate(JmsTemplate jmsTemplate) {
        super.jmsTemplate = jmsTemplate;
    }
}
