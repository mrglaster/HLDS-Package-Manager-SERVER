package com.hldspm.server.connections.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MapUploadRequest extends MapGetRequest{

    @JsonProperty("token")
    private String uploaderToken;

    @JsonProperty("data")
    private String uploadedData;

    @JsonProperty("gamemode")
    private String gameMode;

    public MapUploadRequest(String game, String name, String engine, String uploaderToken, String uploadedData, String gameMode) {
        super(game, name, engine);
        this.uploaderToken = uploaderToken;
        this.uploadedData = uploadedData;
        this.gameMode = gameMode;
    }

    public String getUploadedData() {
        return uploadedData;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public void setUploadedData(String uploadedData) {
        this.uploadedData = uploadedData;
    }

    public String getUploaderToken() {
        return uploaderToken;
    }

    public void setUploaderToken(String uploaderToken) {
        this.uploaderToken = uploaderToken;
    }

    @Override
    public String toString() {
        return "MapRequest{" +
                "game='" + getGame() + '\'' +
                ", name='" + getName() + '\'' +
                ", engine='" + getEngine() + '\'' +
                ", token='" + getUploaderToken() + '\'' +
                ", game mode='" + getGameMode() + '\'' +
                ", data length=" + getUploadedData().length() + '\'' +
                '}';
    }
}
