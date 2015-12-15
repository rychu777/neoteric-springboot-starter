package com.neoteric.starter.jms.listeners;

import org.springframework.core.annotation.AliasFor;
import org.springframework.jms.annotation.JmsListener;

@JmsListener(containerFactory = "queueJmsContainerFactory", destination = "testQueue")
public @interface QueueJmsListener {

    /**
     * The destination name for this listener, resolved through the container-wide
     * {@link org.springframework.jms.support.destination.DestinationResolver} strategy.
     */
    @AliasFor(annotation = JmsListener.class, attribute = "destination")
    String destination();

    /**
     * The JMS message selector expression, if any.
     * <p>See the JMS specification for a detailed definition of selector expressions.
     */
    @AliasFor(annotation = JmsListener.class, attribute = "selector")
    String selector() default "";

    /**
     * The concurrency limits for the listener, if any.
     * <p>The concurrency limits can be a "lower-upper" String &mdash; for example,
     * "5-10" &mdash; or a simple upper limit String &mdash; for example, "10", in
     * which case the lower limit will be 1.
     * <p>Note that the underlying container may or may not support all features.
     * For instance, it may not be able to scale, in which case only the upper limit
     * is used.
     */
    @AliasFor(annotation = JmsListener.class, attribute = "concurrency")
    String concurrency() default "";
}
