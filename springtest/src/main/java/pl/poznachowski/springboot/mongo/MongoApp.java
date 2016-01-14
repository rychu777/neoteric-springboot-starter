package pl.poznachowski.springboot.mongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoApp {

    private static final Logger LOG = LoggerFactory.getLogger(MongoApp.class);

//    public static void main(String[] args) throws Exception {
//
//        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("UTC")));
//        MongoClient mongoClient = new MongoClient();
//        MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongoClient, "database");
//        MongoMappingContext context = new MongoMappingContext();
//        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory);
//        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver,
//                context);
//        mappingConverter.setCustomConversions(new CustomConversions(
//                Arrays.asList(
//                        DateToZonedDateTimeConverter.INSTANCE,
//                        ZonedDateTimeToDateConverter.INSTANCE
//                )
//        ));
//        mappingConverter.afterPropertiesSet();
//        MongoOperations mongoOps = new MongoTemplate(mongoDbFactory, mappingConverter);
////        MongoOperations mongoOps = new MongoTemplate(new MongoClient(), "database");
////        mongoOps.insert(new Person("Joe", 34, LocalDateTime.now(), ZonedDateTime.now()));
//        mongoOps.insert(new Person("Joe", 34, LocalDateTime.now(), ZonedDateTime.now()));
//
//
//        Person one = mongoOps.findOne(new Query(where("name").is("Joe")), Person.class);
//
//        LOG.info("ONE: {}", one);
//
//        mongoOps.dropCollection("person");
//    }
//
//    public enum DateToZonedDateTimeConverter implements Converter<Date, ZonedDateTime> {
//        INSTANCE;
//
//        @Override
//        public ZonedDateTime convert(Date source) {
//            return source == null ? null : source.toInstant().atZone(ZoneId.systemDefault());
//        }
//    }
//
//    public enum ZonedDateTimeToDateConverter implements Converter<ZonedDateTime, Date> {
//        INSTANCE;
//
//        @Override
//        public Date convert(ZonedDateTime source) {
//            return source == null ? null : Date.from(source.toInstant());
//        }
//    }
}
