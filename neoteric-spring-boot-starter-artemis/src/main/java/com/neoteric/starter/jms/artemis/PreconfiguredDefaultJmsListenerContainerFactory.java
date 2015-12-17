package com.neoteric.starter.jms.artemis;

import com.neoteric.starter.jms.JmsProperties;
import com.neoteric.starter.jms.listeners.DefaultErrorHandler;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.transaction.jta.JtaTransactionManager;

public class PreconfiguredDefaultJmsListenerContainerFactory
        extends DefaultJmsListenerContainerFactory {

    public PreconfiguredDefaultJmsListenerContainerFactory(JmsProperties jmsProperties,
                                                           JtaTransactionManager transactionManager,
                                                           DestinationResolver destinationResolver) {
        JmsProperties.Listener listenerProps = jmsProperties.getListener();
        this.setAutoStartup(listenerProps.isAutoStartup());
        if (transactionManager != null) {
            this.setTransactionManager(transactionManager);
        } else {
            this.setSessionTransacted(true);
        }
        if (destinationResolver != null) {
            this.setDestinationResolver(destinationResolver);
        }
        if (listenerProps.getAcknowledgeMode() != null) {
            this.setSessionAcknowledgeMode(listenerProps.getAcknowledgeMode().getMode());
        }
        String concurrency = listenerProps.formatConcurrency();
        if (concurrency != null) {
            this.setConcurrency(concurrency);
        }
        this.setErrorHandler(new DefaultErrorHandler());
    }
}