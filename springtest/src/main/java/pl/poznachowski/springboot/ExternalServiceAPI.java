package pl.poznachowski.springboot;

import org.springframework.cloud.netflix.feign.FeignClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@FeignClient(url = "http://localhost:9099/api/v1/")
public interface ExternalServiceAPI {

    @GET
    @Path("test")
    TestJSON get();
}
