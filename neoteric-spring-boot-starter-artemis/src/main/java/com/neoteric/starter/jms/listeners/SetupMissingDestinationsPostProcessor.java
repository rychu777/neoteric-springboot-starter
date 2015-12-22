package com.neoteric.starter.jms.listeners;

import org.apache.activemq.artemis.api.jms.ActiveMQJMSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.annotation.JmsListeners;
import org.springframework.jms.core.JmsTemplate;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SetupMissingDestinationsPostProcessor implements BeanPostProcessor, Ordered, BeanFactoryAware {

    private static final Logger LOG = LoggerFactory.getLogger(SetupMissingDestinationsPostProcessor.class);

    private BeanFactory beanFactory;

    private final Set<Class<?>> nonAnnotatedClasses = Collections.newSetFromMap(new ConcurrentHashMap<>(64));

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!this.nonAnnotatedClasses.contains(bean.getClass())) {
            Class<?> targetClass = AopUtils.getTargetClass(bean);
            Map<Method, Set<JmsListener>> annotatedMethods = MethodIntrospector.selectMethods(targetClass,
                    (MethodIntrospector.MetadataLookup<Set<JmsListener>>) method -> {
                        Set<JmsListener> listenerMethods =
                                AnnotationUtils.getRepeatableAnnotations(method, JmsListener.class, JmsListeners.class);
                        return (!listenerMethods.isEmpty() ? listenerMethods : null);
                    });
            if (annotatedMethods.isEmpty()) {
                this.nonAnnotatedClasses.add(bean.getClass());
            } else {
                for (Map.Entry<Method, Set<JmsListener>> entry : annotatedMethods.entrySet()) {
                    Method method = entry.getKey();
                    for (JmsListener listener : entry.getValue()) {
                        setupDestinations(listener, method, bean);
                    }
                }
                LOG.debug("{} @JmsListener methods processed on bean '{}': {}", annotatedMethods.size(), beanName, annotatedMethods);
            }
        }
        return bean;
    }

    private void setupDestinations(JmsListener listener, Method method, Object bean) {
        String destinationName = listener.destination();
        String factoryBeanName = listener.containerFactory();

        LOG.warn("destination {}, bean {}", destinationName, factoryBeanName);

        if (factoryBeanName.startsWith("queue")) {
            LOG.warn("Creating queue");
            ActiveMQJMSClient.createQueue(destinationName);
//            createQueue(destinationName);
        } else {
            LOG.warn("Creating topicws");
            ActiveMQJMSClient.createTopic(destinationName);
//            createTopic(destinationName);
        }

    }

    private void createQueue(String destinationName) {
        JmsTemplate jmsQueueTemplate = (JmsTemplate) beanFactory.getBean("jmsQueueTemplate");
        jmsQueueTemplate.execute(session -> {
            LOG.warn("creating Queue");
            return session.createQueue(destinationName);
        });
    }

    private void createTopic(String destinationName) {
        JmsTemplate jmsTopicTemplate = (JmsTemplate) beanFactory.getBean("jmsTopicTemplate");
        jmsTopicTemplate.execute(session -> {
            LOG.warn("creating Topic");
            return session.createTopic(destinationName);
        });
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

}
