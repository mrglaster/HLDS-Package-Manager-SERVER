package com.hldspm.server.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**Model for a manifest element*/
public class ManifestElementModel {

    @JsonProperty("type")
    private String type;
    @JsonProperty("name")
    private String name;


    public ManifestElementModel(String type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public String toString() {
        return "ManifestRecordModel{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
