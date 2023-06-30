package com.hldspm.server.database.data_processor.maps_processor;
import com.hldspm.server.ServerApplication;
import com.hldspm.server.connections.requests.parental_requests.BasicGetRequest;
import com.hldspm.server.connections.requests.upload_requests.MapUploadRequest;
import com.hldspm.server.connections.responses.StatusResponses;
import com.hldspm.server.database.data_processor.uploads_checks.UploadDataChecks;
import com.hldspm.server.database.data_processor.uploads_checks.UploaderVerification;
import com.hldspm.server.database.dumper.DumpCreator;
import com.hldspm.server.ftp_server.cfg.FtpConstants;
import com.hldspm.server.resource_validators.MapValidator;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

/**Provides functions for the maps upload*/
public class MapsUploader {

    /**Generates SQL query for map upload*/
    private static String generateMapUploadQuery(){
        return "INSERT INTO maps(engine, game, name, gamemode) VALUES (?, ?, ?, ?)";
    }

    /**Adds the uploaded map into the database*/
    private static void addMapToDb(MapUploadRequest request){
        String query = generateMapUploadQuery();
        int engine = 2;
        if (Objects.equals(request.getEngine(), "gold")) engine = 1;
        ServerApplication.jdbcTemplate.update(query, engine, request.getGame(), request.getName(), request.getGameMode());
    }

    /**Saves the uploaded map on the storage*/
    private static void saveUploadedMap(MapUploadRequest request){
        String mapsPath = FtpConstants.getFtpPath() + "/" + request.getEngine() + "/" + "maps"+ "/"+ request.getGame();
        try {
            byte[] bytes = Base64.getDecoder().decode(request.getData().getBytes(StandardCharsets.UTF_8));
            FileOutputStream fos = new FileOutputStream(mapsPath + "/" + request.getName() + ".tar.gz");
            fos.write(bytes);
            fos.close();
            addMapToDb(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**Processes the map upload*/
    public static String processMapUpload(MapUploadRequest request){

        String uploaderToken = request.getToken();
        String game = request.getGame();
        String name = request.getName();
        String uploadedData = request.getData();

        if (!UploaderVerification.isValidUploader(uploaderToken)) {
            return StatusResponses.generateBadTokenError();
        } else if (!BasicGetRequest.isValidGame(game)) {
            return StatusResponses.generateBadGameError(game);
        } else if (!UploadDataChecks.isNameAvailable("map", game, name)) {
            return StatusResponses.generateBadResourceNameError();
        } else if (!Objects.equals(request.getEngine(), "gold") && !Objects.equals(request.getEngine(), "source")){
            return StatusResponses.generateUnknownEngineError(request.getEngine());
        }

        String query = "SELECT COUNT(*) FROM maps where name='" + request.getName() + "';";
        if (ServerApplication.jdbcTemplate.queryForObject(query, Integer.class) != 0){
            return StatusResponses.generateBadResourceNameError();
        }


        MapValidator validator = new MapValidator(uploadedData);
        if (!validator.isValidMap()) {
           return StatusResponses.generateInvalidResourceDataErr();
        }
        saveUploadedMap(request);
        DumpCreator.hasChanges = true;
        return StatusResponses.generateSuccessfulUpload();
    }
}
