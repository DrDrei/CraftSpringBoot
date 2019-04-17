package com.spring;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppService {
    public ObjectMapper mapper = new ObjectMapper();

    public JsonNode parseJson(String input) {
        try {
            return mapper.readTree(input);
        } catch (IOException e) {
            throw new HttpMessageNotReadableException("Failed to parse JSON body.");
        }
    }

    public List<String> getScrubbedJsonText(JsonNode baseNode) {
        List<String> scrubbedText = new ArrayList<>();
        for (JsonNode child : baseNode) {
            if (child.getNodeType() == JsonNodeType.STRING) {
                String text = child.textValue();
                String cleanText = scrubText(text);
                scrubbedText.add(cleanText);
            } else if (child.getNodeType() == JsonNodeType.OBJECT) {
                scrubbedText.addAll(getScrubbedJsonText(child));
            }
        }
        return scrubbedText;
    }

    public String scrubText(String text) {
        return text.replaceAll("[^a-zA-Z0-9]+", "");
    }

}
