package com.hldspm.server.connections.requests;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hldspm.server.database.data_processor.maps_processor.MapsUploader;
import com.hldspm.server.models.ManifestElementModel;


import java.util.List;
import java.util.Objects;

public class ManifestGetRequest {

    @JsonProperty("engine")
    private String engine;
    @JsonProperty("game")
    private String game;

    @JsonProperty("manifest")
    private List<ManifestElementModel> manifestList;


    @Override
    public String toString() {
        return "ManifestGetRequest{" +
                "engine='" + engine + '\'' +
                ", game='" + game + '\'' +
                ", elements amount=" + manifestList.size() +
                '}';
    }

    public ManifestGetRequest(String engine, String game, List<ManifestElementModel> manifestList) {
        this.engine = engine;
        this.game = game;
        this.manifestList = manifestList;
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

    public List<ManifestElementModel> getManifestList() {
        return manifestList;
    }

    public void setManifestList(List<ManifestElementModel> manifestList) {
        this.manifestList = manifestList;
    }

    public  boolean isValidRequest(){
        return manifestList.size() > 0 && MapsUploader.isValidGame(game) && (Objects.equals(engine, "gold") || Objects.equals(engine, "source"));
    }
}
