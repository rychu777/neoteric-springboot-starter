package pl.poznachowski.springboot.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class QueueSender {

    private static final Logger LOG = LoggerFactory.getLogger(QueueSender.class);

    @Autowired
    JmsTemplate jmsTemplate;

    public void sendMessage(String message) {

        jmsTemplate.setPubSubDomain(false);
        LOG.warn("Template: {}", jmsTemplate.getConnectionFactory().getClass());
        jmsTemplate.send(session -> session.createTextMessage(ZonedDateTime.now().toString()));
    }

}
