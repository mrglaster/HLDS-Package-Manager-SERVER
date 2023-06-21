package com.hldspm.server.connections.controllers.bundle_controllers;

import com.hldspm.server.connections.requests.get_requests.BundleGetRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BundleGetController {
    @GetMapping
    public String getBundleData(@RequestBody BundleGetRequest request){
        return "yay";
    }

}
