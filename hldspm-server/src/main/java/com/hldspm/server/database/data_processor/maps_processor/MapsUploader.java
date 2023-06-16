package com.hldspm.server.database.data_processor.maps_processor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.hldspm.server.ServerApplication;
import com.hldspm.server.connections.requests.MapUploadRequest;
import com.hldspm.server.constants.MainConstants;
import com.hldspm.server.database.data_processor.uploads_checks.UploadDataChecks;
import com.hldspm.server.database.data_processor.uploads_checks.UploaderVerification;
import com.hldspm.server.ftp_server.cfg.FtpConstants;
import com.hldspm.server.resource_validators.MapValidator;
import org.springframework.http.ResponseEntity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

public class MapsUploader {
    private static boolean isValidGame(String game){
        return MainConstants.games.contains(game);
    }


    private static String generateMapUploadQuery(MapUploadRequest request){
        return "INSERT INTO maps(engine, game, name, gamemode) VALUES (?, ?, ?, ?)";
    }

    private static void addMapToDb(MapUploadRequest request){
        String query = generateMapUploadQuery(request);
        int engine = 2;
        if (Objects.equals(request.getEngine(), "gold")) engine = 1;
        ServerApplication.jdbcTemplate.update(query, engine, request.getGame(), request.getName(), request.getGameMode());
    }

    private static void saveUploadedMap(MapUploadRequest request){
        String mapsPath = FtpConstants.getFtpPath() + "/" + request.getEngine() + "/" + "maps"+ "/"+ request.getGame();
        try {
            byte[] bytes = Base64.getDecoder().decode(request.getUploadedData().getBytes(StandardCharsets.UTF_8));
            FileOutputStream fos = new FileOutputStream(mapsPath + "/" + request.getName() + ".tar.gz");
            fos.write(bytes);
            fos.close();
            addMapToDb(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String processMapUpload(MapUploadRequest request){
        JsonObject response = new JsonObject();
        Gson curGson = new GsonBuilder().setPrettyPrinting().create();

        String uploaderToken = request.getUploaderToken();
        String game = request.getGame();
        String name = request.getName();
        String uploadedData = request.getUploadedData();

        if (!UploaderVerification.isValidUploader(uploaderToken)) {
            response.addProperty("status", 403);
            response.addProperty("description", "Access denied! Bad token!");
            return curGson.toJson(response);
        } else if (!isValidGame(game)) {
            response.addProperty("status", 400);
            response.addProperty("description", "Unknown game: " + game);
            return curGson.toJson(response);
        } else if (!UploadDataChecks.isNameAvailable("map", game, name)) {
            response.addProperty("status", 433);
            response.addProperty("description", "There is already a map with such name!");
            return curGson.toJson(response);
        } else if (!Objects.equals(request.getEngine(), "gold") && !Objects.equals(request.getEngine(), "source")){
            response.addProperty("status", 400);
            response.addProperty("description", "Unknown engine: " + request.getEngine());
        }
        
        MapValidator validator = new MapValidator(uploadedData);
        if (!validator.isValidMap()) {
            response.addProperty("status", 400);
            response.addProperty("description", "Invalid map!");
            return curGson.toJson(response);
        }
        saveUploadedMap(request);
        response.addProperty("status", 200);
        response.addProperty("description", "Your map was successfuly added to the server");
        return curGson.toJson(response);



    }

}
