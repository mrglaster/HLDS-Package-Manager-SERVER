package com.hldspm.server.connections.controllers.bundle_controllers;

import com.hldspm.server.connections.requests.upload_requests.BundleUploadRequest;
import com.hldspm.server.database.data_processor.bundle_processors.BundleUploader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BundleUploadController {

    @PostMapping(value={"upload/bundle", "/upload/bundle"}, produces = "application/json")
    public String uploadBundle(@RequestBody BundleUploadRequest request){
        return BundleUploader.processBundleUpload(request);
    }

}
