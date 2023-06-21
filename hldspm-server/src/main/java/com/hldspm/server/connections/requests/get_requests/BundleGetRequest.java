package com.hldspm.server.connections.requests.get_requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hldspm.server.connections.requests.parental_requests.BasicGetRequest;
import com.hldspm.server.database.data_processor.maps_processor.MapsUploader;
import java.util.Objects;

public class BundleGetRequest extends BasicGetRequest {
    @JsonProperty("type")
    private String type;
    public BundleGetRequest(String engine, String game, String name, String type) {
        super(engine, game, name);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isValidType(){
        return Objects.equals(type, "plugin") || Objects.equals(type, "map") || Objects.equals(type, "module");
    }

    public boolean isValidRequest() {
        return isValidEngine() && MapsUploader.isValidGame(getGame()) && getGame().length() > 0 && isValidType();
    }
}
