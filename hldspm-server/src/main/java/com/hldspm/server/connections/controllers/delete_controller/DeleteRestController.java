package com.hldspm.server.connections.controllers.delete_controller;
import com.hldspm.server.connections.requests.delete_request.DeleteResourceRequest;
import com.hldspm.server.database.data_processor.delete_processor.DataDeleter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class DeleteRestController {
    @PostMapping(value={"/delete"}, produces = "application/json")
    @Async
    public CompletableFuture<String> deleteResource(@RequestBody DeleteResourceRequest request){
        return DataDeleter.processDataDelete(request);
    }
}
