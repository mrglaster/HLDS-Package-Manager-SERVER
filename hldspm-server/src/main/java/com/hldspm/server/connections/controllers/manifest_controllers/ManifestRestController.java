package com.hldspm.server.connections.controllers.manifest_controllers;

import com.hldspm.server.connections.requests.get_requests.ManifestGetRequest;
import com.hldspm.server.database.data_processor.manifest_processors.ManifestGetter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;


@RestController
public class ManifestRestController {
    /**Manifest get routing*/
    @GetMapping(value = "/manifest", produces="application/json")
    @Async
    public CompletableFuture<String> getManifestData(@RequestBody ManifestGetRequest request){
        return ManifestGetter.processManifestGetting(request);
    }

}
