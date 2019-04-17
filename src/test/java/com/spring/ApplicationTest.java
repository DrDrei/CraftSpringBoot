package com.spring;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AppController.class)
public class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppService appService;

    @Test
    public void scrubJson_returns_input_json() throws Exception {
        String emptyJson = "{}";

        ObjectNode emptyNode = new ObjectMapper().createObjectNode();
        Mockito.when(appService.parseJson(any(String.class))).thenReturn(emptyNode);

        List<String> stringList = new ArrayList<>();
        Mockito.when(appService.getScrubbedJsonText(any(JsonNode.class))).thenReturn(stringList);

        mockMvc.perform(post("/scrubJson").contentType(MediaType.APPLICATION_JSON).content(emptyJson))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(emptyJson)));

        Mockito.verify(appService).parseJson(any());
        Mockito.verify(appService).getScrubbedJsonText(any());
    }
}
