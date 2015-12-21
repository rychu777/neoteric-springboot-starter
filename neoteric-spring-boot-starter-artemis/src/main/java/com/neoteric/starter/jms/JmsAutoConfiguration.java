package com.neoteric.starter.jms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoteric.starter.jms.artemis.PreconfiguredDefaultJmsListenerContainerFactory;
import com.neoteric.starter.jms.producers.QueueMessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import java.util.Map;

@Configuration
@ConditionalOnClass(JmsTemplate.class)
@ConditionalOnBean(ConnectionFactory.class)
@EnableConfigurationProperties(JmsProperties.class)
public class JmsAutoConfiguration {

    private static final String TYPE_PROPERTY_NAME = "OBJECT_TYPE";

    @Autowired
    JmsProperties jmsProperties;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired(required = false)
    private DestinationResolver destinationResolver;

    @Autowired(required = false)
    private JtaTransactionManager transactionManager;


    @Autowired(required = false)
    @Qualifier(value = "jacksonJmsMessageMappings")
    Map<String, Class<?>> jacksonMessageMappings;

    @Autowired
    MappingJackson2MessageConverter jacksonMessageConverter;

    @Bean
    DefaultJmsListenerContainerFactory queueJmsContainerFactory() {
        DefaultJmsListenerContainerFactory listener =
                new PreconfiguredDefaultJmsListenerContainerFactory(jmsProperties, transactionManager, destinationResolver);
        listener.setConnectionFactory(connectionFactory);
        listener.setPubSubDomain(false);
        return listener;
    }

    @Bean
    MappingJackson2MessageConverter jacksonMessageConverter(ObjectMapper objectMapper) {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper);
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName(TYPE_PROPERTY_NAME);
        if (jacksonMessageMappings != null) {
            converter.setTypeIdMappings(jacksonMessageMappings);
        }
        return converter;
    }

    @Bean
    public JmsTemplate jmsQueueTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        configureJmsTemplate(jmsTemplate);
        jmsTemplate.setPubSubDomain(false);
        return jmsTemplate;
    }

    @Bean
    public JmsTemplate jmsTopicTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        configureJmsTemplate(jmsTemplate);
        jmsTemplate.setPubSubDomain(true);
        return jmsTemplate;
    }

    private void configureJmsTemplate(JmsTemplate jmsTemplate) {
        if (destinationResolver != null) {
            jmsTemplate.setDestinationResolver(destinationResolver);
        }
        jmsTemplate.setMessageConverter(jacksonMessageConverter);
        jmsTemplate.setDeliveryMode(DeliveryMode.PERSISTENT);
    }

    @Bean
    QueueMessageProducer queueMessageProducer() {
        return new QueueMessageProducer();
    }
}