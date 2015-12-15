package pl.poznachowski.springboot.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import pl.poznachowski.springboot.SampleEndpoint;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.ZonedDateTime;

@Component
@Path("/v1/jms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SendJmsEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(SampleEndpoint.class);

    @Autowired
    JmsTemplate jmsTemplate;

    @GET
    public String sendJms() {
        LOG.warn("Template: {}", jmsTemplate.getConnectionFactory().getClass());
        jmsTemplate.send("testQueue", session ->  session.createTextMessage(ZonedDateTime.now().toString()));
        return "success";
    }
}