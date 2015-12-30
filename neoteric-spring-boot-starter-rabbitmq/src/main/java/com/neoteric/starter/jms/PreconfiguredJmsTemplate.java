package com.neoteric.starter.jms;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DestinationResolver;

import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;

public class PreconfiguredJmsTemplate extends JmsTemplate {

    public PreconfiguredJmsTemplate(ConnectionFactory connectionFactory, DestinationResolver destinationResolver) {
        this.setConnectionFactory(connectionFactory);
        if (destinationResolver != null) {
            this.setDestinationResolver(destinationResolver);
        }
        this.setDeliveryMode(DeliveryMode.PERSISTENT);
    }
}
