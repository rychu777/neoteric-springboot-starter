package pl.poznachowski.springboot.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TestSubscriber {

    private static final Logger LOG = LoggerFactory.getLogger(TestSubscriber.class);

//    @JmsListener(containerFactory = JmsContainerFactories.QUEUE, destination = "queue2")
//    private void handleMessage2(Message<TestJSON> message) {
//        LOG.warn("CYCKI: {}", message);
//    }
//
//    @JmsListener(containerFactory = JmsContainerFactories.QUEUE, destination = "queue2")
//    private void handleMessage(Message<TestJSON> message) {
//        LOG.warn("HEHESZKI: {}", message);
//    }

//    @JmsListener(containerFactory = JmsContainerFactories.TOPIC_DURABLE_SHARED, destination = "testTopic")
//    private void handleTopicMessage(Message<TestJSON> message) {
//        LOG.warn("Durable Shared TOPIC: {}", message);
//    }
//
//    @JmsListener(containerFactory = JmsContainerFactories.TOPIC_DURABLE_SHARED, destination = "testTopic")
//    private void handleTopicMessage2(Message<TestJSON> message) {
//        LOG.warn("Durable Shared TOPIC 2: {}", message);
//    }

}