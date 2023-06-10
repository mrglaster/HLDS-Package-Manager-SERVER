package com.hldspm.server.models;

public class EngineModel {
    private long id;
    private String name;

    public EngineModel() {

    }

    public EngineModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "EngineModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
