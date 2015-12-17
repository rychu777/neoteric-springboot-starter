package com.neoteric.starter.jms.producers;

import com.neoteric.starter.jms.QueueTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class QueueMessageProducer {

    @QueueTemplate
    JmsTemplate jmsTemplate;

    public void sendMessage(String queueDestination, MessageCreator messageCreator) {
//        messageCreator.
        jmsTemplate.convertAndSend("abc", );
    }
}
