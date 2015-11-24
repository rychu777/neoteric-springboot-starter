package pl.poznachowski.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoteric.request.RequestParameters;
import com.neoteric.starter.request.params.RequestParametersHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
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

    @Autowired
    TextReturner returner;

    @GET
    public List<Person> get() {

        RequestParameters current = RequestParametersHolder.current();
        LOG.warn("USING: {}", objectMapper);
        LOG.info("Params: {}", current.getFilters());

        LOG.error("{}", returner.returnString());
        List<Person> all = mongoTemplate.findAll(Person.class);
//        mongoTemplate.aggregate(newAggregation(match(Criteria.where("abc").and())), "abc", Person.class);
//        Person person = new Person("name", 10, LocalDateTime.now(), ZonedDateTime.now());
        return all;
    }

    @GET
    @Path("/2")
    public List<Person> get2() {

//        Criteria criteria = Criteria.where("name").is("name").orOperator(Criteria.where("age").lt(15));
        Criteria criteria = new Criteria().orOperator(Criteria.where("name").is("name"), Criteria.where("name").regex("Joh"));
        LOG.error("criteria: {}", criteria.getCriteriaObject());

        TypedAggregation<Person> aggregation = newAggregation(Person.class, match(criteria));
        AggregationResults<Person> result = mongoTemplate.aggregate(aggregation, Person.class);
        return result.getMappedResults();
    }

    @POST
    public Response post(Person person) {
        mongoTemplate.insert(person);
        LOG.info("Created: {}", person);
        return Response.created(URI.create("abc")).build();
    }
}