package com.neoteric.starter.jms.artemis;

import com.neoteric.starter.jms.JmsProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.transaction.jta.JtaTransactionManager;

public class PreconfiguredDefaultJmsListenerContainerFactory
        extends DefaultJmsListenerContainerFactory {

    private static final Logger LOG = LoggerFactory.getLogger(PreconfiguredDefaultJmsListenerContainerFactory.class);

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

//        this.setClientId(listenerProps.getClientId());
    }
}