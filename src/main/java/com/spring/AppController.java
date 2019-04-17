package com.spring;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AppController {
    public Logger logger = LoggerFactory.getLogger(AppController.class);

    @Autowired
    public AppService appService;

    @RequestMapping(value = "/scrubJson", method = RequestMethod.POST)
    @ResponseBody
    public String scrubJson(@RequestBody String body) {
        JsonNode node = appService.parseJson(body);
        List<String> scrubbedText = appService.getScrubbedJsonText(node);
        logger.info(scrubbedText.toString());
        return body;
    }
}
