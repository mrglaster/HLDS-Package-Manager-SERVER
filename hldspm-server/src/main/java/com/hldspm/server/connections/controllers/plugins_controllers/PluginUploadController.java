package com.hldspm.server.connections.controllers.plugins_controllers;

import com.hldspm.server.connections.requests.PluginUploadRequest;
import com.hldspm.server.database.data_processor.plugins_processor.PluginUploader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PluginUploadController {
    @PostMapping(value={"upload/plugin"}, produces="application/json")
    public String uploadPlugin(@RequestBody PluginUploadRequest request){
        return PluginUploader.processPluginUpload(request);
    }
}
