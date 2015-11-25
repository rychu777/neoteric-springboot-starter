package pl.poznachowski.springboot;

import com.neoteric.starter.mongo.test.EmbeddedMongoTest;
import com.neoteric.starter.test.ReinjectableSpringBootTest;
import com.neoteric.starter.test.reinject.ReinjectBean;
import com.neoteric.starter.test.reinject.ReinjectableSpringApplicationContextLoader;
import com.neoteric.starter.test.restassured.ContainerIntegrationTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.poznachowski.springboot.mongo.Person;

import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static com.jayway.restassured.RestAssured.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ReinjectableSpringBootTest(classes = {SpringbootTestApplication.class}, initializers = ReinjectableSpringApplicationContextLoader.ReinjectInitializer.class)
@ContainerIntegrationTest
@EmbeddedMongoTest
public class SampleEndpoint1Test {

    private static final Logger LOG = LoggerFactory.getLogger(SampleEndpoint3Test.class);

    @ReinjectBean("returner")
    public static TextReturner mocked() {
        return new TextReturner() {
            @Override
            public String returnString() {
                return "HEHESZKI";
            }
        };
    }

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    public void testName() throws Exception {
        LOG.error("TEST1_SAMPLE1");
        mongoTemplate.insert(new Person("DSK", 50, LocalDateTime.now(), ZonedDateTime.now()));

        when()
                .get("api/v1/test")
                .then()
                .log().all()
                .assertThat()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    public void testName2() throws Exception {
        LOG.error("TEST1_SAMPLE2");
        mongoTemplate.insert(new Person("DSK", 50, LocalDateTime.now(), ZonedDateTime.now()));

        when()
                .get("api/v1/test")
                .then()
                .log().all()
                .assertThat()
                .statusCode(Response.Status.OK.getStatusCode());
    }
}
