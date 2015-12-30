package com.neoteric.starter.jms.artemis;

public final class JmsContainerFactories {

    private JmsContainerFactories() {
        // Prevents class instantiation
    }

    public static final String QUEUE = "queueJmsContainerFactory";
    public static final String TOPIC = "topicJmsContainerFactory";
    public static final String TOPIC_SHARED = "topicSharedJmsContainerFactory";
    public static final String TOPIC_DURABLE = "topicDurableJmsContainerFactory";
    public static final String TOPIC_DURABLE_SHARED = "topicDurableSharedJmsContainerFactory";
}
