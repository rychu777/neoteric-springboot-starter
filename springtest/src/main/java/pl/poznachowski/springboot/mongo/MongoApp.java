package pl.poznachowski.springboot.mongo;

import com.mongodb.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import static org.springframework.data.mongodb.core.query.Criteria.where;

public class MongoApp {

    private static final Logger LOG = LoggerFactory.getLogger(MongoApp.class);

    public static void main(String[] args) throws Exception {

        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("UTC")));
        MongoClient mongoClient = new MongoClient();
        MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongoClient, "database");
        MongoMappingContext context = new MongoMappingContext();
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory);
        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver,
                context);
        mappingConverter.setCustomConversions(new CustomConversions(
                Arrays.asList(
                        DateToZonedDateTimeConverter.INSTANCE,
                        ZonedDateTimeToDateConverter.INSTANCE
                )
        ));
        mappingConverter.afterPropertiesSet();
        MongoOperations mongoOps = new MongoTemplate(mongoDbFactory, mappingConverter);
//        MongoOperations mongoOps = new MongoTemplate(new MongoClient(), "database");
//        mongoOps.insert(new Person("Joe", 34, LocalDateTime.now(), ZonedDateTime.now()));
        mongoOps.insert(new Person("Joe", 34, LocalDateTime.now(), ZonedDateTime.now()));


        Person one = mongoOps.findOne(new Query(where("name").is("Joe")), Person.class);

        LOG.info("ONE: {}", one);

        mongoOps.dropCollection("person");
    }

    public enum DateToZonedDateTimeConverter implements Converter<Date, ZonedDateTime> {
        INSTANCE;

        @Override
        public ZonedDateTime convert(Date source) {
            return source == null ? null : source.toInstant().atZone(ZoneId.systemDefault());
        }
    }

    public enum ZonedDateTimeToDateConverter implements Converter<ZonedDateTime, Date> {
        INSTANCE;

        @Override
        public Date convert(ZonedDateTime source) {
            return source == null ? null : Date.from(source.toInstant());
        }
    }
}
