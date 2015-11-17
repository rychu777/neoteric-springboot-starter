package com.neoteric.starter.jackson.rison;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(DataProviderRunner.class)
public class RisonTest {

    private static final ObjectMapper JSON = new ObjectMapper(new JsonFactory());
    private static final ObjectMapper RISON = new ObjectMapper(new RisonFactory());

    @DataProvider
    public static Object[][] risonData() {
        return new Object[][]{
                // rison, json
                {"(a:0,b:1)", "{\"a\":0,\"b\":1}"},
                {"(a:0,b:foo,c:'23skidoo')", "{\"a\":0,\"b\":\"foo\",\"c\":\"23skidoo\"}"},
                {"!t", "true"},
                {"!f", "false"},
                {"!n", "null"},
                {"''", "\"\""},
                {"0", "0"},
                {"1.5", "1.5"},
                {"-3", "-3"},
                {"1.0e30", "1.0e+30"},
                {"'2011-12-03T10:15:30Z'", "\"2011-12-03T10:15:30Z\""},
                {"1.0e-30", "1.0e-30"},
                {"a", "\"a\""},
                {"'0a'", "\"0a\""},
                {"'abc def'", "\"abc def\""},
                {"()", "{}"},
                {"(a:0)", "{\"a\":0}"},
                {"(id:!n,type:/common/document)", "{\"id\":null,\"type\":\"/common/document\"}"},
                {"!()", "[]"},
                {"!(!t,!f,!n,'')", "[true,false,null,\"\"]"},
                {"'-h'", "\"-h\""},
                {"a-z", "\"a-z\""},
                {"'wow!!'", "\"wow!\""},
                {"domain.com", "\"domain.com\""},
                {"'user@domain.com'", "\"user@domain.com\""},
                {"'US $10'", "\"US $10\""},
                {"'can!'t'", "\"can't\""},
                {"'Control-F: \u0006'", "\"Control-F: \\u0006\""},
                {"'Unicode: \u0BEB'", "\"Unicode: \\u0BEB\""},
                {"(a:!((b:c,'3':!())))", "{\"a\":[{\"b\":\"c\",\"3\":[]}]}"},
                {"(a:'this\thas\nmultiple\nlines\n')", "{\"a\":\"this\\thas\\nmultiple\\nlines\\n\"}"},
        };
    }

    @Test
    @UseDataProvider("risonData")
    public void testRison(String rison, String json) throws IOException {
        Object jsonObject = JSON.readValue(json, Object.class);
        Object risonObject = RISON.readValue(rison, Object.class);
        assertThat(risonObject).isEqualTo(jsonObject);
        assertThat(RISON.writeValueAsString(jsonObject)).isEqualTo(rison);
    }

    @Test
    public void testName() throws Exception {

        byte[] jsonBytes = Files.readAllBytes(Paths.get("src/test/resources/requestWithOrBetweenFields.json"));
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> mapa = mapper.readValue(jsonBytes, Map.class);
        String s = RISON.writeValueAsString(mapa);

        System.out.println(s);

    }
}

