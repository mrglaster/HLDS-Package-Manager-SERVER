package com.hldspm.server.models;

/**Model for the uploader class*/
public class UploaderModel {
    private long id;
    private String name;
    private String token;

    public UploaderModel(){}

    public UploaderModel(long id, String name, String token) {
        this.id = id;
        this.name = name;
        this.token = token;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "UploaderModule{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
