package com.spring;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AppServiceTest {

    private AppService appService;

    @Before
    public void setUp() {
        appService = new AppService();
    }

    @Test
    public void scrubText_pass() {
        String expected = "testcase";
        String input = "te#@#s$t!ca*s^e?|";

        assertEquals(expected, appService.scrubText(input));
    }

    @Test
    public void getScrubbedJsonText_simple_pass() throws JSONException, IOException {
        String key1 = "testkey1";
        String key2 = "testkey2";
        String key3 = "testkey3";
        String nestKey = "nestKey";

        String jsonStr = new JSONObject()
                .put("key1", key1)
                .put("key2", key2)
                .put("key3", key3)
                .put("number", 1)
                .put("bool", false)
                .put("nest", new JSONObject().put("nestKey", nestKey))
                .toString();

        List<String> expectedList = new ArrayList<>();
        expectedList.add(key1);
        expectedList.add(key2);
        expectedList.add(key3);
        expectedList.add(nestKey);

        JsonNode node = appService.mapper.readTree(jsonStr);
        assertEquals(expectedList, appService.getScrubbedJsonText(node));
    }

    @Test
    public void parseJson_pass() throws JSONException {
        String key = "key";
        String value = "value";

        String inputStr = new JSONObject()
                .put(key, value)
                .toString();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode expected = mapper.createObjectNode();
        expected.put(key, value);

        assertEquals(expected, appService.parseJson(inputStr));
    }

    @Test(expected = HttpMessageNotReadableException.class)
    public void parseJson_throw_execption() {
        String invalidJson = "{\"key\": \"value}";
        appService.parseJson(invalidJson);
    }
}