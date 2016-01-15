package com.neoteric.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


public class FiltersParserTest {

    private final static ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger LOG = LoggerFactory.getLogger(FiltersParserTest.class);

    @Test
    public void shouldProduceEmptyFilters() throws Exception {
        Map<String, Object> rawFilters = readFiltersFromResources("EmptyFilters.json");

        Map<RequestObject, Object> filters = FiltersParser.parseFilters(rawFilters);
        assertThat(filters)
                .hasSize(0);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionForIncorrectRootElement() throws Exception {
        Map<String, Object> rawFilters = readFiltersFromResources("IncorrectFiltersStartingNeitherFromFieldOrAnOperator.json");

        Map<RequestObject, Object> filters = FiltersParser.parseFilters(rawFilters);
    }

    @Test
    public void testSingleFieldWithOperator() throws Exception {
        Map<String, Object> rawFilters = readFiltersFromResources("SingleFieldWithOperator.json");

        Map<RequestObject, Object> filters = FiltersParser.parseFilters(rawFilters);
        assertThat(filters)
                .hasSize(1)
                .containsOnlyKeys(RequestField.of("name"));

        assertThat((Map) filters.get(RequestField.of("name")))
                .containsEntry(RequestOperator.of(OperatorType.EQUAL.getName()), "John");
    }

    @Test
    public void testMultipleFieldsWithOperator() throws Exception {
        Map<String, Object> rawFilters = readFiltersFromResources("MultipleFieldsWithOperator.json");

        Map<RequestObject, Object> filters = FiltersParser.parseFilters(rawFilters);
        assertThat(filters)
                .hasSize(2)
                .containsOnlyKeys(RequestField.of("name"), RequestField.of("size"));

        assertThat((Map) filters.get(RequestField.of("name")))
                .containsEntry(RequestOperator.of(OperatorType.EQUAL.getName()), "John");
        assertThat((Map) filters.get(RequestField.of("size")))
                .containsEntry(RequestOperator.of(OperatorType.GREATER_THAN_EQUAL.getName()), "5");
    }

    @Test
    public void testLogicalOperator() throws Exception {
        Map<String, Object> rawFilters = readFiltersFromResources("LogicalOperator.json");

        Map<RequestObject, Object> filters = FiltersParser.parseFilters(rawFilters);
        assertThat(filters)
                .hasSize(1)
                .containsOnlyKeys(RequestLogicalOperator.of(LogicalOperatorType.OR));

        assertThat((Map) filters.get(RequestLogicalOperator.of(LogicalOperatorType.OR)))
                .hasSize(2)
                .containsEntry(RequestField.of("name"), ImmutableMap.of(RequestOperator.of(OperatorType.EQUAL), "John"))
                .containsEntry(RequestField.of("surname"), ImmutableMap.of(RequestOperator.of(OperatorType.STARTS_WITH), "Doe"));
    }

    /*
        @Test
        public void testAllOptions() throws Exception {
            Map<String, Object> rawFilters = readFiltersFromResources("TestAllOptions.json");

            Map<RequestObject, Object> filters = FiltersParser.parseFilters(rawFilters);
            assertThat(filters)
                    .hasSize(1)
                    .containsOnlyKeys(RequestField.of("examples"));
        }
    */
    private Map<String, Object> readFiltersFromResources(String resourceName) throws IOException {
        byte[] jsonBytes = Files.readAllBytes(Paths.get("src/test/resources/requests/" + resourceName));
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonBytes, Map.class);
    }


}
