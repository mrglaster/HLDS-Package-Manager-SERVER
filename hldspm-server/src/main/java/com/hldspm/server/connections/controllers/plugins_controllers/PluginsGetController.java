package com.hldspm.server.connections.controllers.plugins_controllers;

import com.hldspm.server.connections.requests.MapGetRequest;
import com.hldspm.server.database.data_processor.plugins_processor.PluginGetter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PluginsGetController {
    @GetMapping({"/plugin", "/plugin/", "/get-plugin/", "/get-plugin"})
    public ResponseEntity<String> getPlugin(@RequestBody MapGetRequest request){
        String game = request.getGame();
        String name = request.getName();
        String engine = request.getEngine();
        String response = new PluginGetter().processGetting(game, name, engine);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
}
