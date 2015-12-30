package com.neoteric.starter.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Autowired
@Qualifier("jmsQueueTemplate")
public @interface QueueJmsTemplate {
}
