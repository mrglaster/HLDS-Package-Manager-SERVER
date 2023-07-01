package com.hldspm.server.connections.controllers.plugins_controllers;

import com.hldspm.server.connections.requests.get_requests.PluginGetRequest;
import com.hldspm.server.connections.requests.upload_requests.PluginUploadRequest;
import com.hldspm.server.database.data_processor.plugins_processor.PluginGetter;
import com.hldspm.server.database.data_processor.plugins_processor.PluginUploader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class PluginsRestController {

    /**Routing for plugins getting*/
    @GetMapping(value={"/plugin", "/plugin/", "/get-plugin/", "/get-plugin"}, produces="application/json")
    public ResponseEntity<String> getPlugin(@RequestBody PluginGetRequest request){
        String game = request.getGame();
        String name = request.getName();
        String engine = request.getEngine();
        String response = new PluginGetter().processGetting(game, name, engine);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    /**Routing for plugins upload*/
    @PostMapping(value={"upload/plugin"}, produces="application/json")
    @Async
    public CompletableFuture<String> uploadPlugin(@RequestBody PluginUploadRequest request){
        return PluginUploader.processPluginUpload(request);
    }
}
