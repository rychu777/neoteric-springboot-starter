package pl.poznachowski.springboot.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitFanoutSubscriber {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitFanoutSubscriber.class);

    public final static String EXCHANGE_NAME = "spring-boot-fanout-exchange";
    public final static String CONSUMER_1 = "consumer1";
    public final static String CONSUMER_2 = "consumer2";

//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(value = CONSUMER_1),
//            exchange = @Exchange(value = EXCHANGE_NAME, durable = "true", type = ExchangeTypes.FANOUT)))
//    private void handleMessage(Message message) {
//        LOG.error("Message: {}", message);
//    }
//
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = CONSUMER_1),
            exchange = @Exchange(value = EXCHANGE_NAME, durable = "true", type = ExchangeTypes.FANOUT)))
    private void handleMessage2(Message message) {
        LOG.warn("SHOULD RETRY: {}", message);
        throw new RuntimeException("AAA");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = CONSUMER_2),
            exchange = @Exchange(value = EXCHANGE_NAME, durable = "true", type = ExchangeTypes.FANOUT)))
    private void handleMessage3(Message message) {
        LOG.info("SHOULD NOT RETRY: {}", message);
        throw new AmqpRejectAndDontRequeueException("XXX");
    }

}