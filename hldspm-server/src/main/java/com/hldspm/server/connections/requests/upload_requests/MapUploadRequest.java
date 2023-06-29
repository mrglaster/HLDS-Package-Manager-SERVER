package com.hldspm.server.connections.requests.upload_requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hldspm.server.connections.requests.parental_requests.BasicUploadRequest;

/**Class describing the Map upload request*/
public class MapUploadRequest extends BasicUploadRequest {

    @JsonProperty("gamemode")
    private String gameMode;

    public MapUploadRequest(String game, String name, String engine, String uploaderToken, String uploadedData, String gameMode) {
        super(engine, game, name, uploaderToken, uploadedData);
        this.gameMode = gameMode;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }


    @Override
    public String toString() {
        return "MapRequest{" +
                "game='" + getGame() + '\'' +
                ", name='" + getName() + '\'' +
                ", engine='" + getEngine() + '\'' +
                ", game mode='" + getGameMode() + '\'' +
                ", data length=" + getData().length() + '\'' +
                '}';
    }
}
