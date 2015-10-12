package pl.poznachowski.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.ZoneId;
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
}
