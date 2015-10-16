package pl.poznachowski.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class AbcTest {

    @Test
    public void shouldabc() throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("UTC")));
        ObjectMapper mapper = Jackson2ObjectMapperBuilder.json()
                .failOnUnknownProperties(false)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) //ISODate
                .build();


        TestJSON json = new TestJSON("text", "abc");

        String s = mapper.writeValueAsString(json);
        System.out.println(s);
    }

    private static final Logger LOG = LoggerFactory.getLogger(AbcTest.class);

    @Test
    public void testDate() throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("UTC")));

        LOG.error("Local: {}", LocalDateTime.now());
        LOG.error("Offset: {}", OffsetDateTime.now());
        LOG.error("Zoned: {}", ZonedDateTime.now());

    }

    @Test
    public void testMapperJSr310() throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("UTC")));
        LocalDateTime date = LocalDateTime.now();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        SimpleModule module = new SimpleModule();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH");
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        mapper.registerModule(module);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String result = mapper.writeValueAsString(date);

        LOG.error("RESULT: {}", result);
    }

    @Test
    public void testMapperJodaTime() throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("UTC")));
        DateTime date = DateTime.now();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm"));

        String result = mapper.writeValueAsString(date);

        LOG.error("RESULT: {}", result);
    }

    @Test
    public void testMapperDate() throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("UTC")));
        Date now = new Date();

        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));

        String result = mapper.writeValueAsString(now);

        LOG.error("RESULT: {}", result);
    }
}
