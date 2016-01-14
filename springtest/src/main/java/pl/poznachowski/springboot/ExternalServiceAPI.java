package pl.poznachowski.springboot;

import org.springframework.cloud.netflix.feign.FeignClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@FeignClient(value = "http://localhost:9099", url = "http://localhost:9099")
public interface ExternalServiceAPI {

    @GET
    @Path("/api/v1/test")
    TestJSON get();
}
