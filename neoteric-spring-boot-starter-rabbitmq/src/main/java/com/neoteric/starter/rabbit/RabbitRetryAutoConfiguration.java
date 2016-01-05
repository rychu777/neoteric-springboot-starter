package com.neoteric.starter.rabbit;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.classify.Classifier;
import org.springframework.classify.SubclassClassifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import javax.annotation.PostConstruct;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@ConditionalOnClass({RabbitTemplate.class, Channel.class})
@AutoConfigureAfter(RabbitAutoConfiguration.class)
public class RabbitRetryAutoConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitRetryAutoConfiguration.class);
    public static final String ERROR_EXCHANGE = "DLE";

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    @Qualifier("rabbitListenerContainerFactory")
    SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory;

    @PostConstruct
    public void addRetryPolicy() {
        RetryOperationsInterceptor retryOperationsInterceptor = RetryInterceptorBuilder.stateless()
                .retryOperations(defaultRetryTemplate())
                .recoverer(new RepublishMessageRecoverer(rabbitTemplate, ERROR_EXCHANGE))
                .build();
        rabbitListenerContainerFactory.setAdviceChain(retryOperationsInterceptor);
    }

    private RetryTemplate defaultRetryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.registerListener(new LogOnRetryListener());
        retryTemplate.setBackOffPolicy(defaultBackOffPolicy());
        retryTemplate.setRetryPolicy(defaultRetryPolicy());
        return retryTemplate;
    }

    private RetryPolicy defaultRetryPolicy() {
        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy();
        simpleRetryPolicy.setMaxAttempts(3);

        Map<Class<? extends Throwable>, RetryPolicy> dontRetryExceptions = new HashMap<>();
        dontRetryExceptions.put(AmqpRejectAndDontRequeueException.class, new NeverRetryPolicy());

        return new ListenerExceptionClassifierRetryPolicy(dontRetryExceptions, simpleRetryPolicy);
    }

    private ExponentialBackOffPolicy defaultBackOffPolicy() {
        ExponentialBackOffPolicy policy = new ExponentialBackOffPolicy();
        policy.setInitialInterval(1000);
        policy.setMultiplier(1);
        policy.setMaxInterval(500000);
        return policy;
    }


}
