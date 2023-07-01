package com.hldspm.server.connections.controllers.user_controllers;

import com.hldspm.server.connections.requests.delete_request.DeleteUploaderRequest;
import com.hldspm.server.connections.requests.upload_requests.CreateAdminRequest;
import com.hldspm.server.database.data_processor.users_processor.AdminCreator;
import com.hldspm.server.database.data_processor.users_processor.UploaderDeleter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersRestController {

    /**Uploader delete routing*/
    @PostMapping(value = {"/delete/uploader"}, produces = "application/json")
    public String deleteUploader(@RequestBody DeleteUploaderRequest request){
        return UploaderDeleter.deleteUser(request);
    }

    /**Uploader creation routing*/
    @PostMapping(value={"/create/uploader"}, produces = "application/json")
    public String createUploader(@RequestBody CreateAdminRequest request){
        return AdminCreator.processAdminCreation(request);
    }

}
