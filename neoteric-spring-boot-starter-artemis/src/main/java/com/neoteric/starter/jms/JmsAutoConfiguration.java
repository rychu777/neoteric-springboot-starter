package com.neoteric.starter.jms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoteric.starter.jms.artemis.JmsContainerFactories;
import com.neoteric.starter.jms.artemis.converter.JacksonAwareMessageConverter;
import com.neoteric.starter.jms.listeners.DefaultErrorHandler;
import com.neoteric.starter.jms.listeners.SetupMissingDestinationsPostProcessor;
import com.neoteric.starter.jms.producers.QueueMessageProducer;
import com.neoteric.starter.jms.producers.TopicMessageProducer;
import org.apache.activemq.artemis.api.core.client.ActiveMQClient;
import org.apache.activemq.artemis.api.core.client.ClientSessionFactory;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import java.util.Map;

@Configuration
@ConditionalOnClass(JmsTemplate.class)
@ConditionalOnBean(ConnectionFactory.class)
@AutoConfigureBefore(org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration.class)
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
    JacksonAwareMessageConverter jacksonMessageConverter;

    @Bean
    JacksonAwareMessageConverter jacksonMessageConverter(ObjectMapper objectMapper) {
        JacksonAwareMessageConverter converter = new JacksonAwareMessageConverter(objectMapper);
        converter.setTypeIdPropertyName(TYPE_PROPERTY_NAME);
        if (jacksonMessageMappings != null) {
            converter.setTypeIdMappings(jacksonMessageMappings);
        }
        return converter;
    }

    @Bean(name = JmsContainerFactories.QUEUE)
    DefaultJmsListenerContainerFactory queueJmsContainerFactory() {
        DefaultJmsListenerContainerFactory listener = new DefaultJmsListenerContainerFactory();
        configureListener(listener);
        listener.setPubSubDomain(false);
        return listener;
    }

    @Bean(name = JmsContainerFactories.TOPIC_DURABLE_SHARED)
    DefaultJmsListenerContainerFactory topicDurableSharedJmsContainerFactory() {
        DefaultJmsListenerContainerFactory listener = new DefaultJmsListenerContainerFactory();
        configureListener(listener);
        listener.setPubSubDomain(true);
        listener.setSubscriptionDurable(true);
        listener.setSubscriptionShared(true);
        return listener;
    }

    @Bean(name = JmsContainerFactories.TOPIC_DURABLE)
    DefaultJmsListenerContainerFactory topicDurableJmsContainerFactory() {
        DefaultJmsListenerContainerFactory listener = new DefaultJmsListenerContainerFactory();
        configureListener(listener);
        listener.setPubSubDomain(true);
        listener.setSubscriptionDurable(true);
        listener.setSubscriptionShared(false);
        return listener;
    }

    @Bean(name = JmsContainerFactories.TOPIC_SHARED)
    DefaultJmsListenerContainerFactory topicSharedJmsContainerFactory() {
        DefaultJmsListenerContainerFactory listener = new DefaultJmsListenerContainerFactory();
        configureListener(listener);
        listener.setPubSubDomain(true);
        listener.setSubscriptionDurable(false);
        listener.setSubscriptionShared(true);
        return listener;
    }

    @Bean(name = JmsContainerFactories.TOPIC)
    DefaultJmsListenerContainerFactory topicJmsContainerFactory() {
        DefaultJmsListenerContainerFactory listener = new DefaultJmsListenerContainerFactory();
        configureListener(listener);
        listener.setPubSubDomain(true);
        listener.setSubscriptionDurable(false);
        listener.setSubscriptionShared(false);
        return listener;
    }

    private void configureListener(DefaultJmsListenerContainerFactory listener) {
        JmsProperties.Listener listenerProps = jmsProperties.getListener();
        listener.setAutoStartup(listenerProps.isAutoStartup());
        if (transactionManager != null) {
            listener.setTransactionManager(transactionManager);
        } else {
            listener.setSessionTransacted(true);
        }
        if (destinationResolver != null) {
            listener.setDestinationResolver(destinationResolver);
        }
        if (listenerProps.getAcknowledgeMode() != null) {
            listener.setSessionAcknowledgeMode(listenerProps.getAcknowledgeMode().getMode());
        }
        String concurrency = listenerProps.formatConcurrency();
        if (concurrency != null) {
            listener.setConcurrency(concurrency);
        }
        listener.setErrorHandler(new DefaultErrorHandler());
        listener.setConnectionFactory(connectionFactory);
        listener.setMessageConverter(jacksonMessageConverter);
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

    @Bean
    TopicMessageProducer topicMessageProducer() {
        return new TopicMessageProducer();
    }

    @Bean
    SetupMissingDestinationsPostProcessor setupDestinationsPostProcessor() {
        return new SetupMissingDestinationsPostProcessor();
    }

    @ConditionalOnClass(JmsMessagingTemplate.class)
    @ConditionalOnMissingBean(JmsMessagingTemplate.class)
    protected static class MessagingTemplateConfiguration {

        @Bean
        public JmsMessagingTemplate jmsQueueMessagingTemplate(@Qualifier("jmsQueueTemplate") JmsTemplate jmsTemplate) {
            return new JmsMessagingTemplate(jmsTemplate);
        }

        @Bean
        public JmsMessagingTemplate jmsTopicMessagingTemplate(@Qualifier("jmsTopicTemplate") JmsTemplate jmsTemplate) {
            return new JmsMessagingTemplate(jmsTemplate);
        }

    }
}