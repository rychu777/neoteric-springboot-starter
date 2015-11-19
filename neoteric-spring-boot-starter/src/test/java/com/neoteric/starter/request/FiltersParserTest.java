package com.neoteric.starter.request;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoteric.request.RequestField;
import com.neoteric.request.RequestObject;
import com.neoteric.starter.request.params.FiltersParser;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


public class FiltersParserTest {

    private final static ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger LOG = LoggerFactory.getLogger(FiltersParserTest.class);

    @Test
    public void testName() throws Exception {

        String simpleJson = "{\"name\":{\"$eq\":\"John\"}}";

        Map<String, Object> rawFilters = MAPPER.readValue(simpleJson, new TypeReference<Map<String, Object>>() {
        });

        Map<RequestObject, Object> filters = FiltersParser.parseFilters(rawFilters);
        assertThat(filters)
                .hasSize(1)
                .containsOnlyKeys(RequestField.of("name"));


        LOG.info("FIlters: {}", filters);
    }
}
