package pl.poznachowski.springboot;

import com.jayway.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.WebApplicationContext;

import javax.ws.rs.core.Response;

import static com.jayway.restassured.RestAssured.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(SpringbootTestApplication.class)
@WebIntegrationTest("server.port:0")
public class SampleEndpoint2Test {

    @Value("${local.server.port}")
    int port;

    @Autowired
    EmbeddedWebApplicationContext server;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void testName() throws Exception {

        System.out.println("PORT: " + port);

        when()
                .get("api/v1/test")
                .then()
                .log().all()
                .assertThat()
                .statusCode(Response.Status.OK.getStatusCode());
    }
}