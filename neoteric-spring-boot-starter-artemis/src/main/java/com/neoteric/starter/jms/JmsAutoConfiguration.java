package com.neoteric.starter.jms;

import com.neoteric.starter.jms.artemis.PreconfiguredDefaultJmsListenerContainerFactory;
import com.neoteric.starter.jms.listeners.DefaultErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.jms.ConnectionFactory;

@Configuration
@ConditionalOnClass(JmsTemplate.class)
@ConditionalOnBean(ConnectionFactory.class)
@AutoConfigureBefore(org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration.class)
@EnableConfigurationProperties(JmsProperties.class)
public class JmsAutoConfiguration {

    @Autowired
    JmsProperties jmsProperties;

    @Autowired(required = false)
    private DestinationResolver destinationResolver;

    @Autowired(required = false)
    private JtaTransactionManager transactionManager;

    @Bean
    DefaultJmsListenerContainerFactory queueJmsContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory listener =
                new PreconfiguredDefaultJmsListenerContainerFactory(jmsProperties, transactionManager, destinationResolver);
        listener.setConnectionFactory(connectionFactory);
        listener.setErrorHandler(new DefaultErrorHandler());
        listener.setPubSubDomain(false);
        return listener;
    }

}