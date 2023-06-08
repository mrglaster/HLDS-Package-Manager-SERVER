package com.hldspm.server.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    private final Gson gson;
    private final TestResponse testResponse;

    public TestController() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        testResponse = new TestResponse(200, "asddfdfdfdfdfdf");
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        return gson.toJson(testResponse);
    }
}

record TestResponse(int status, String data) {
}
