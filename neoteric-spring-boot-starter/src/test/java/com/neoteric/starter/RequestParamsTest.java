package com.neoteric.starter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.neoteric.starter.request.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class RequestParamsTest {

    private static final Logger LOG = LoggerFactory.getLogger(RequestParamsTest.class);
    private Map<RequestObject, Object> requestParameters = Maps.newHashMap();

    @Test
    public void testNameA() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"abc\": 34.5}";
        Map<String, Object> mapa = mapper.readValue(json, Map.class);

        mapa.forEach((s, o) -> {
            LOG.info("KEY:{}, VALUE: {}, class: {}", s, o, o.getClass().toString());
        });
    }

    @Test
    public void testName() throws Exception {
//        byte[] jsonBytes = Files.readAllBytes(Paths.get("src/test/resources/requestWithOrBetweenFields.json"));
//        byte[] jsonBytes = Files.readAllBytes(Paths.get("src/test/resources/request2.json"));
//        byte[] jsonBytes = Files.readAllBytes(Paths.get("src/test/resources/request3.json"));
        byte[] jsonBytes = Files.readAllBytes(Paths.get("src/test/resources/request4.json"));


        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> mapa = mapper.readValue(jsonBytes, Map.class);
        LOG.warn("INITIAL MAP: {}", mapa);

        mapa.forEach((key, entry) -> {
            LOG.warn("Key: {}, Entry: {}", key, entry);
            if (isNotFieldNorLogicalOperator(key)) {
                throw new IllegalStateException("isNotFieldNorLogicalOperator");
            }
            if (isField(key)) {
                if (!(entry instanceof Map)) {
                    throw new IllegalStateException("Bad value type for field: " + entry.getClass().toString());
                }
                requestParameters.put(RequestField.of(key), processFieldValue((Map) entry));
            } else {
                if (!(entry instanceof Map)) {
                    throw new IllegalStateException("Bad value type for or operation: " + entry.getClass().toString());
                }
                requestParameters.put(RequestLogicalOperator.of(key), processRootLogicalOperatorValue((Map) entry));
            }
        });

        LOG.error("MAPA: {}", requestParameters);
    }

    //tylko w pierwszym levelu $or moze odnosic sie do pol
    //w kazdym innym wypadku tylko inne operatory

    private Map<RequestObject, Object> processRootLogicalOperatorValue(Map<String, Object> orEntry) {
        Map<RequestObject, Object> orEntryMap = Maps.newHashMap();
        orEntry.forEach((key, entry) -> {
            LOG.warn("Processing Root Or operator: Key: {}, Entry: {}", key, entry);
            if (!isField(key)) {
                throw new IllegalStateException("Or in root node can't be applied with other operators");
            }
            if (!(entry instanceof Map)) {
                throw new IllegalStateException("Bad value type for field: " + entry.getClass().toString());
            }
            orEntryMap.put(RequestField.of(key), processFieldValue((Map) entry));
        });
        return orEntryMap;
    }

    private Map<RequestObject, Object> processLogicalOperatorValue(Map<String, Object> orEntry) {
        Map<RequestObject, Object> orEntryMap = Maps.newHashMap();
        orEntry.forEach((key, entry) -> {
            LOG.warn("Processing Or operator: Key: {}, Entry: {}", key, entry);
            if (!isOperator(key)) {
                throw new IllegalStateException("Nested Or operator can't be applied with fields");
            }
            RequestOperator operator = RequestOperator.of(key);
            ValueType valueType = operator.getOperator().getValueType();
            if (valueType.equals(ValueType.ARRAY) && !(entry instanceof List)) {
                throw new IllegalStateException("Bad value type for operator: " + entry.getClass().toString());
            }
            orEntryMap.put(RequestOperator.of(key), entry);
        });
        return orEntryMap;
    }

    private Map<RequestObject, Object> processFieldValue(Map<String, Object> fieldEntry) {
        Map<RequestObject, Object> fieldEntryMap = Maps.newHashMap();
        fieldEntry.forEach((key, entry) -> {
            LOG.warn("Processing Field: Key: {}, Entry: {}", key, entry);
            if (isNotOperatorNorLogicalOperator(key)) {
                throw new IllegalStateException("isNotOperatorNorLogicalOperator");
            }
            if (isLogicalOperator(key)) {
                if (!(entry instanceof Map)) {
                    throw new IllegalStateException("Bad value type for logical operator: " + entry.getClass().toString());
                }
                fieldEntryMap.put(RequestLogicalOperator.of(key), processLogicalOperatorValue((Map) entry));
            } else {
            RequestOperator operator = RequestOperator.of(key);
                ValueType valueType = operator.getOperator().getValueType();
                if (valueType.equals(ValueType.ARRAY) && !(entry instanceof List)) {
                    throw new IllegalStateException("Bad value type for operator: " + entry.getClass().toString());
                }
            fieldEntryMap.put(RequestOperator.of(key), entry);
            }
        });
        return fieldEntryMap;
    }

    private boolean isLogicalOperator(String key) {
        return LogicalOperatorType.contains(key);
    }

    private boolean isOperator(String key) {
        return OperatorType.contains(key);
    }

    private boolean isField(String key) {
        return !key.startsWith("$");
    }

    private boolean isNotOperatorNorLogicalOperator(String key) {
        return !(isLogicalOperator(key) || isOperator(key));
    }

    private boolean isNotFieldNorLogicalOperator(String key) {
        return !(key.startsWith("$") ? LogicalOperatorType.contains(key) : true);
    }
}
