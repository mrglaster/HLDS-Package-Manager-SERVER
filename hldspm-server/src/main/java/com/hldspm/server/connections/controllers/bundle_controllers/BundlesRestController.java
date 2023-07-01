package com.hldspm.server.connections.controllers.bundle_controllers;

import com.hldspm.server.connections.requests.get_requests.BundleGetRequest;
import com.hldspm.server.connections.requests.upload_requests.BundleUploadRequest;
import com.hldspm.server.database.data_processor.bundle_processors.BundleGetter;
import com.hldspm.server.database.data_processor.bundle_processors.BundleUploader;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class BundlesRestController {
    /**Bundle getting routing*/
    @GetMapping(value={"/bundle", "bundle"}, produces = "application/json")
    @Async
    public CompletableFuture<String> getBundleData(@RequestBody BundleGetRequest request){
        return BundleGetter.processBundleGetting(request);
    }

    /**Bundle upload routing*/
    @PostMapping(value={"upload/bundle", "/upload/bundle"}, produces = "application/json")
    @Async
    public CompletableFuture<String> uploadBundle(@RequestBody BundleUploadRequest request){
        return BundleUploader.processBundleUpload(request);
    }
}
