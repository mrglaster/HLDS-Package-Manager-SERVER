package com.hldspm.server.connections.controllers.maps_controllers;

import com.hldspm.server.connections.requests.MapUploadRequest;
import com.hldspm.server.database.data_processor.maps_processor.MapsUploader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MapsUploadController {

    @PostMapping(value={"upload/map"}, produces="application/json")
    public String uploadMap(@RequestBody MapUploadRequest request){
        return MapsUploader.processMapUpload(request);
    }

}
