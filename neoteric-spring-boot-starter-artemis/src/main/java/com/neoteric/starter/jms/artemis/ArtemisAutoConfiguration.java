package com.neoteric.starter.jms.artemis;

import com.neoteric.starter.jms.JmsAutoConfiguration;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;

@Configuration
@AutoConfigureBefore({org.springframework.boot.autoconfigure.jms.artemis.ArtemisAutoConfiguration.class, JmsAutoConfiguration.class})
@EnableConfigurationProperties(ArtemisProperties.class)
@ConditionalOnMissingBean(ConnectionFactory.class)
public class ArtemisAutoConfiguration {

    @Autowired
    ArtemisProperties artemisProperties;

    @Bean
    public ActiveMQConnectionFactory jmsConnectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ArtemisConnectionFactoryFactory(artemisProperties)
                .createConnectionFactory(ActiveMQConnectionFactory.class);
        connectionFactory.setUser(artemisProperties.getUsername());
        connectionFactory.setPassword(artemisProperties.getPassword());
        connectionFactory.setReconnectAttempts(artemisProperties.getMaxRetries());

        if (artemisProperties.getBackOffMode().equals(ArtemisProperties.BackOffMode.FIXED)) {
            connectionFactory.setRetryIntervalMultiplier(1);
            connectionFactory.setRetryInterval(artemisProperties.getFixedBackOff().getInterval());
        } else {
            ArtemisProperties.ExponentialBackOff expProps = artemisProperties.getExponentialBackOff();
            connectionFactory.setRetryIntervalMultiplier(expProps.getMultiplier());
            connectionFactory.setRetryInterval(expProps.getInitialInterval());
            connectionFactory.setMaxRetryInterval(expProps.getMaximumInterval());
        }
        return connectionFactory;
    }
}
