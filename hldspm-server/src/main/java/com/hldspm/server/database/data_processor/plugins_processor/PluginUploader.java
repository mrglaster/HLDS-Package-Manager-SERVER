package com.hldspm.server.database.data_processor.plugins_processor;

import com.hldspm.server.ServerApplication;
import com.hldspm.server.connections.requests.upload_requests.PluginUploadRequest;
import com.hldspm.server.connections.responses.StatusResponses;
import com.hldspm.server.database.data_processor.uploads_checks.UploadDataChecks;
import com.hldspm.server.database.data_processor.uploads_checks.UploaderVerification;
import com.hldspm.server.database.dumper.DumpCreator;
import com.hldspm.server.ftp_server.cfg.FtpConstants;
import com.hldspm.server.resource_validators.PluginValidator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Objects;

import static com.hldspm.server.connections.requests.parental_requests.BasicGetRequest.isValidGame;

//TODO написать функции для удаления плагинов с учетом бандлов

/**Provides functions for plugins upload*/
public class PluginUploader{
    /**Generates SQL query for plugin upload*/
    private static String generatePluginInsertQuery(){
        return "INSERT INTO plugins(engine, game, name, time) VALUES (?, ?, ?, ?)";
    }

    /**Adds plugin to the database*/
    private static void addPluginToDb(PluginUploadRequest request){
        String query = generatePluginInsertQuery();
        int engine = 2;
        if (Objects.equals(request.getEngine(), "gold")) engine = 1;
        ServerApplication.jdbcTemplate.update(query, engine, request.getGame(), request.getName()+"%"+request.getVersion(), java.sql.Timestamp.from(Instant.now()));
    }


    /**Saves uploaded plugin on the storage*/
    private static void saveUploadedPlugin(PluginUploadRequest request){
        String pluginPath = FtpConstants.getFtpPath() + "/" + request.getEngine() + "/" + "plugins"+ "/"+ request.getGame();
        try {
            byte[] bytes = Base64.getDecoder().decode(request.getData().getBytes(StandardCharsets.UTF_8));
            FileOutputStream fos = new FileOutputStream(pluginPath + "/" + request.generateFileName());
            fos.write(bytes);
            fos.close();
            addPluginToDb(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**Processes the plugin upload*/
    public static String processPluginUpload(PluginUploadRequest request){
        String uploaderToken = request.getToken();
        String game = request.getGame();
        String name = request.getName();
        String uploadedData = request.getData();

        if(!request.isValidRequest()){
            return StatusResponses.generateBadRequestErr();
        }

        if (!UploaderVerification.isValidUploader(uploaderToken)) {
            return StatusResponses.generateBadTokenError();
        } else if (!isValidGame(game)) {
            return StatusResponses.generateBadGameError(game);
        } else if (!UploadDataChecks.isNameAvailable("plugin", game, name)) {
           return StatusResponses.generateBadResourceNameError();
        } else if (!Objects.equals(request.getEngine(), "gold") && !Objects.equals(request.getEngine(), "source")){
            return StatusResponses.generateUnknownEngineError(request.getEngine());
        }

        PluginValidator validator = new PluginValidator(uploadedData);
        try {
            if (!validator.isValidPlugin()) {
                return StatusResponses.generateInvalidResourceDataErr();
            }
        } catch (IOException e) {
            return StatusResponses.generateBadRequestErr();
        }
        saveUploadedPlugin(request);
        DumpCreator.hasChanges = true;
        return StatusResponses.generateSuccessfulUpload();
    }



}
