package pl.poznachowski.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Component
@Path("/v1/test")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SampleEndpoint2 {

    private static final Logger LOG = LoggerFactory.getLogger(SampleEndpoint.class);

    @Autowired
    ObjectMapper objectMapper;

    @GET
    public TestJSON get() {
        LOG.error("MAPPER: {}", objectMapper);
        TestJSON json = new TestJSON("abc", "xyz");
        LOG.info("RETURNED FROM OTHER SERVICE: {}", json);
        return json;
    }
}