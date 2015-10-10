package pl.poznachowski.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Path("/v1/test")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SampleEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(SampleEndpoint.class);

    @GET
    public Response get() {
        LOG.info("IN SECOND SERVICE");
        TestJSON json = new TestJSON("someText", "someINfo");
        return Response.ok(json).build();
    }
}
