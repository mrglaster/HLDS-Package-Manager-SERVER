package com.hldspm.server.connections.controllers.maps_controllers;

import com.hldspm.server.connections.requests.get_requests.MapGetRequest;
import com.hldspm.server.connections.requests.upload_requests.MapUploadRequest;
import com.hldspm.server.database.data_processor.maps_processor.MapsGetter;
import com.hldspm.server.database.data_processor.maps_processor.MapsUploader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class MapsRestController {
    /**Map getting routing*/
    @GetMapping(value={"/map", "/map/", "/get-map/", "/get-map"}, produces="application/json")
    public ResponseEntity<String> getMap(@RequestBody MapGetRequest request){
        String game = request.getGame();
        String name = request.getName();
        String engine = request.getEngine();
        String response = new MapsGetter().processGetting(game, name, engine);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    /**Map upload routing*/
    @PostMapping(value={"upload/map"}, produces="application/json")
    @Async
    public CompletableFuture<String> uploadMap(@RequestBody MapUploadRequest request){
        return MapsUploader.processMapUpload(request);
    }
}
