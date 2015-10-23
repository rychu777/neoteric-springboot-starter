package pl.poznachowski.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import pl.poznachowski.springboot.mongo.Person;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@Component
@Path("/v1/test")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SampleEndpoint2 {

    private static final Logger LOG = LoggerFactory.getLogger(SampleEndpoint.class);

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @GET
    public List<Person> get() {

        LOG.error("OM: {}", objectMapper);
        List<Person> all = mongoTemplate.findAll(Person.class);
        mongoTemplate.aggregate(newAggregation(match(Criteria.where("abc").and())), "abc", Person.class);
//        Person person = new Person("name", 10, LocalDateTime.now(), ZonedDateTime.now());
        return all;
    }

    @POST
    public Response post(Person person) {
        mongoTemplate.insert(person);
        LOG.info("Created: {}", person);
        return Response.created(URI.create("abc")).build();
    }
}