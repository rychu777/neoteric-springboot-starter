package pl.poznachowski.springboot.rabbit;

import com.neoteric.starter.rabbit.TracedRabbitTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.poznachowski.springboot.SampleEndpoint;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Component
@Path("/v1/rabbit")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SendRabbitEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(SampleEndpoint.class);

    @Autowired
    TracedRabbitTemplate rabbitTemplate;

    @GET
    @Path("/queue")
    public String sendToQueue() {
        rabbitTemplate.convertAndSend(RabbitConfiguration.EXCHANGE_NAME, "", new Object());
        rabbitTemplate.send(RabbitConfiguration.EXCHANGE_NAME, "", MessageBuilder.withBody("testBody".getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON).build());
        return "success";
    }

    @GET
    @Path("/topic")
    public String sendToTopic() {
        return "success";
    }
}