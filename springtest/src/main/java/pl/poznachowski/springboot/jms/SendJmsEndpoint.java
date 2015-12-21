package pl.poznachowski.springboot.jms;

import com.neoteric.starter.jms.producers.QueueMessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.poznachowski.springboot.SampleEndpoint;
import pl.poznachowski.springboot.TestJSON;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Component
@Path("/v1/jms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SendJmsEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(SampleEndpoint.class);

    @Autowired
    QueueMessageProducer queueMessageProducer;

    @GET
    public String sendJms() {
        queueMessageProducer.send("testQueue", new TestJSON("aaa", "bbb"));
        return "success";
    }
}