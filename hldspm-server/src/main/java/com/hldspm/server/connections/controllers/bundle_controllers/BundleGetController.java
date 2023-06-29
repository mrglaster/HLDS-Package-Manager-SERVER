package com.hldspm.server.connections.controllers.bundle_controllers;

import com.hldspm.server.connections.requests.get_requests.BundleGetRequest;
import com.hldspm.server.database.data_processor.bundle_processors.BundleGetter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BundleGetController {

    /**Bundle getting routing*/
    @GetMapping(value={"/bundle", "bundle"}, produces = "application/json")
    public String getBundleData(@RequestBody BundleGetRequest request){
        return BundleGetter.processBundleGetting(request);
    }

}
