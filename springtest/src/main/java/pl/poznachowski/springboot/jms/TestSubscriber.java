package pl.poznachowski.springboot.jms;

import com.neoteric.starter.jms.listeners.QueueJmsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class TestSubscriber {

    private static final Logger LOG = LoggerFactory.getLogger(TestSubscriber.class);

//
//    @JmsListener(destination = "testQueue")
//    private void handleMessage(String message) {
//        LOG.warn("DUPA: {}", message);
//        throw new RuntimeException("XYZ");
//    }

    @QueueJmsListener(destination = "abc")
    private void handleMessage2(Message<String> message) {
        LOG.warn("CYCKI: {}", message);
    }
}