package com.hldspm.server.database.data_processor.users_processor;

import com.hldspm.server.ServerApplication;
import com.hldspm.server.connections.requests.delete_request.DeleteUploaderRequest;
import com.hldspm.server.connections.responses.StatusResponses;

import java.util.Objects;

public class UploaderDeleter {
    public static String deleteUser(DeleteUploaderRequest request){
        if (!UserChecks.isSudoUser(request.getSudoToken())){
            return StatusResponses.generateError(403, "Not sudo token");
        }
        String getNameQuery = "SELECT name  FROM uploaders WHERE token='" + request.getSudoToken() + "';";
        String name = ServerApplication.jdbcTemplate.queryForObject(getNameQuery, String.class);
        if (Objects.equals(name, request.getUploaderName())) {
            return StatusResponses.generateError(400, "You can't delete yourself!");
        }
        try{
            String exChecker = "SELECT COUNT(*) FROM uploaders WHERE name='" + request.getUploaderName() + "'";
            if (ServerApplication.jdbcTemplate.queryForObject(exChecker, Integer.class) == 0){
                return StatusResponses.generateError(404, "Uploader with name " + request.getUploaderName() + " was not found!");
            }
            String query = "DELETE FROM uploaders WHERE name='" + request.getUploaderName() + "'";
            ServerApplication.jdbcTemplate.update(query);
            return StatusResponses.generateError(200, "The admin has been successfully removed from the database!");
        } catch (Exception e){
            return StatusResponses.generateError(500, "Something went wrong");
        }
    }
}
