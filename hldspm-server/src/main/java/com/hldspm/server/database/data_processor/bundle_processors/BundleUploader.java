package com.hldspm.server.database.data_processor.bundle_processors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.hldspm.server.ServerApplication;
import com.hldspm.server.connections.requests.upload_requests.BundleUploadRequest;
import com.hldspm.server.connections.responses.StatusResponses;
import com.hldspm.server.database.data_processor.uploads_checks.UploaderVerification;
import com.hldspm.server.database.mappers.IdRowMapper;
import com.hldspm.server.models.CountModel;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Objects;

public class BundleUploader {
    private static final Gson curGson = new GsonBuilder().setPrettyPrinting().create();

    private String generateMapsGettingQuery(BundleUploadRequest request){
        return "SELECT COUNT(*) FROM maps WHERE name IN " + request.getMapsAsDataList();
    }

    private static boolean isBundleNameAvailable(BundleUploadRequest request){
        String query = "SELECT COUNT(*) FROM bundles WHERE name='" + request.getName() + "';";
        RowMapper<CountModel> rowMapper = new IdRowMapper();
        List<CountModel> count = ServerApplication.jdbcTemplate.query(query, rowMapper);
        return count.get(0).getCount() == 0;
    }

    private static String wrapWithQuotes(Object obj){
        return "'" + obj.toString() + "'";
    }

    private static String processMapBundle(BundleUploadRequest request){
       String query = "SELECT id FROM maps WHERE name in " + request.getMapsAsDataList();
       RowMapper<CountModel> rowMapper = new IdRowMapper();
       List<CountModel> ids = ServerApplication.jdbcTemplate.query(query, rowMapper);
       JsonObject response = new JsonObject();
       if (ids.size() != request.getElements().size()){
           response.addProperty("status", 400);
           response.addProperty("description", "Request contains unknown resources");
           return curGson.toJson(response);
       }
       StringBuilder arrayLine = new StringBuilder("'{");
       for (CountModel i : ids){
           arrayLine.append(i.getCount()).append(',');
       }
        arrayLine.deleteCharAt(arrayLine.length() - 1);
        arrayLine.append("}'");
        String insertQuery = "INSERT INTO bundles(content_type, engine, game, name, elements) VALUES (" +  request.contentTypeToId() + ", " + request.engineToId() + ", " + wrapWithQuotes(request.getGame()) + ", " + wrapWithQuotes(request.getName()) + ", " + arrayLine + ");";
        ServerApplication.jdbcTemplate.update(insertQuery);
        response.addProperty("status", 200);
        response.addProperty("description", "Your bundle was successfully added to the database!");
        return curGson.toJson(response);
    }

    private static String processPluginBundle(BundleUploadRequest request){
        // TODO Доделать
        return "Sorry Mario, but your princess is in another castle";
    }

    public static String processBundleUpload(BundleUploadRequest request){
        if (!UploaderVerification.isValidUploader(request.getToken())) {
            return StatusResponses.generateBadTokenError();
        }
        if (!request.isValidRequest()){
            return StatusResponses.generateBadRequestErr();
        }
        if (!isBundleNameAvailable(request)){
            return StatusResponses.generateBadResourceNameError();
        }
        if (Objects.equals(request.getType(), "plugin")){
            return processPluginBundle(request);
        }
        return processMapBundle(request);
    }
}
