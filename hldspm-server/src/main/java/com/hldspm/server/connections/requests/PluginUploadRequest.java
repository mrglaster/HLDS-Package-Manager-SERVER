package com.hldspm.server.connections.requests;

import com.hldspm.server.database.data_processor.maps_processor.MapsUploader;

public class PluginUploadRequest {
    private String engine;
    private String game;
    private String token;
    private String name;
    private String version;
    private String data;


    public PluginUploadRequest(String engine, String game, String token, String name, String version, String data) {
        this.engine = engine;
        this.game = game;
        this.token = token;
        this.name = name;
        this.version = version;
        this.data = data;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String generateFileName(){
        return this.name + '%' + this.version + ".tar.gz";
    }

    public boolean isValidRequest(){
        return this.data.length() !=0 && this.name.length() != 0 && this.version.length() != 0 && MapsUploader.isValidGame(this.game) && (this.engine.equals("source") || this.engine.equals("gold"));
    }


}
