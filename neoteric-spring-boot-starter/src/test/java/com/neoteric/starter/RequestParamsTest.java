package com.neoteric.starter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.neoteric.starter.request.RequestObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class RequestParamsTest {

    private static final Logger LOG = LoggerFactory.getLogger(RequestParamsTest.class);

    @Test
    public void testName() throws Exception {

        byte[] jsonBytes = Files.readAllBytes(Paths.get("src/test/resources/requestWithOrBetweenFields.json"));


        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonNode = mapper.readTree(jsonBytes);

        LOG.info("{}", jsonNode);

        Map<RequestObject, RequestObject> requestParameters = Maps.newHashMap();

        jsonNode.fields().forEachRemaining(stringJsonNodeEntry -> LOG.error("{}", stringJsonNodeEntry));
    }
}
