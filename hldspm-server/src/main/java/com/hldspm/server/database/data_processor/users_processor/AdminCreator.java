package com.hldspm.server.database.data_processor.users_processor;
import com.hldspm.server.ServerApplication;
import com.hldspm.server.connections.requests.upload_requests.CreateAdminRequest;
import com.hldspm.server.connections.responses.StatusResponses;

public class AdminCreator {
    public static String processAdminCreation(CreateAdminRequest request) {
        if (!UserChecks.isSudoUser(request.getSudoToken())) {
            return StatusResponses.generateError(403, "Access denied! Not superuser token!");
        }
        if (!UserChecks.isNameFree(request.getName())) {
            return StatusResponses.generateError(400, "There is an admin with such name!");
        }
        if (!request.isValidRequest()) {
            return StatusResponses.generateBadRequestErr();
        }
        String query = "INSERT INTO uploaders(name, token) VALUES(?, ?)";
        try {
            ServerApplication.jdbcTemplate.update(query, request.getName(), request.getUploaderToken());
        } catch (Exception e) {
            return StatusResponses.generateBadRequestErr();
        }
        return StatusResponses.generateSuccessfulUpload();
    }
}
