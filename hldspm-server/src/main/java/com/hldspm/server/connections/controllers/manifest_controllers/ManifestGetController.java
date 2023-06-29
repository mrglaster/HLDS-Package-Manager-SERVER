package com.hldspm.server.connections.controllers.manifest_controllers;

import com.hldspm.server.connections.requests.get_requests.ManifestGetRequest;
import com.hldspm.server.database.data_processor.manifest_processors.ManifestGetter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ManifestGetController {
    /**Manifest get routing*/
    @GetMapping(value = "/manifest", produces="application/json")
    public String getManifestData(@RequestBody ManifestGetRequest request){
        return ManifestGetter.processManifestGetting(request);
    }

}
