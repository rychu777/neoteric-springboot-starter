package pl.poznachowski.springboot;

import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Path("/v1/endpoint")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(tags = "/v1/endpoint")
public class SampleEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(SampleEndpoint.class);

    @Autowired
    ExternalServiceAPI externalServiceAPI;

    @GET
    public Response get(@QueryParam("dupa") String dupa) {
        LOG.error("ERROR");

        TestJSON json = externalServiceAPI.get();
        LOG.info("RETURNED FROM OTHER SERVICE: {}", json);
        return Response.ok(json).build();
    }
}