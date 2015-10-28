package com.neoteric.starter.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoteric.request.RequestObject;
import com.neoteric.request.RequestParamsParser;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class RequestParamsCriteriaBuilderTest {

    private static final Logger LOG = LoggerFactory.getLogger(RequestParamsCriteriaBuilderTest.class);

    @Test
    public void testSome() throws Exception {

        byte[] jsonBytes = Files.readAllBytes(Paths.get("src/test/resources/requestWithOrBetweenFields.json"));
//        byte[] jsonBytes = Files.readAllBytes(Paths.get("src/test/resources/request2.json"));
//        byte[] jsonBytes = Files.readAllBytes(Paths.get("src/test/resources/request3.json"));
//        byte[] jsonBytes = Files.readAllBytes(Paths.get("src/test/resources/request4.json"));


        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> mapa = mapper.readValue(jsonBytes, Map.class);

        LOG.warn("MAPA: {}", mapa);

        RequestParamsParser parser = new RequestParamsParser();
        Map<RequestObject, Object> filterMap = parser.parseFilters(mapa);

        RequestParamsCriteriaBuilder criteriaBuilder = new RequestParamsCriteriaBuilder();
        Criteria build = criteriaBuilder.build(filterMap);

        LOG.warn("Criteria: {}", build.getCriteriaObject());
    }
}