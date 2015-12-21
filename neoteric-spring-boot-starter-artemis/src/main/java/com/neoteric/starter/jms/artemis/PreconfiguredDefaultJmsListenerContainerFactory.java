package com.neoteric.starter.jms.artemis;

import com.neoteric.starter.jms.JmsProperties;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.transaction.jta.JtaTransactionManager;

public class PreconfiguredDefaultJmsListenerContainerFactory
        extends DefaultJmsListenerContainerFactory {

    public PreconfiguredDefaultJmsListenerContainerFactory(JmsProperties jmsProperties,
                                                           JtaTransactionManager transactionManager,
                                                           DestinationResolver destinationResolver) {

    }
}