package com.hldspm.server.connections.controllers.maps_controllers;

import com.hldspm.server.connections.requests.MapGetRequest;
import com.hldspm.server.database.data_processor.maps_processor.MapsGetter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**Controller for the map getting*/
@RestController
public class MapsGetController {
   @GetMapping(value={"/map", "/map/", "/get-map/", "/get-map"}, produces="application/json")
    public ResponseEntity<String> getMap(@RequestBody MapGetRequest request){
       String game = request.getGame();
       String name = request.getName();
       String engine = request.getEngine();
       String response = new MapsGetter().processGetting(game, name, engine);
       return ResponseEntity.status(HttpStatus.OK).body(response);

   }

}
