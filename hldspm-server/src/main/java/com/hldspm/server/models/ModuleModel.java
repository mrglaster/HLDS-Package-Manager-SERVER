package com.hldspm.server.models;

public class ModuleModel {
    private long id;
    private int engine;
    private String game;
    private String platform;
    private String name;

    public ModuleModel(){}

    public ModuleModel(long id, int engine, String game, String platform, String name) {
        this.id = id;
        this.engine = engine;
        this.game = game;
        this.platform = platform;
        this.name = name;
    }

    @Override
    public String toString() {
        return "ModuleModel{" +
                "id=" + id +
                ", engine=" + engine +
                ", game='" + game + '\'' +
                ", platform='" + platform + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getEngine() {
        return engine;
    }

    public void setEngine(int engine) {
        this.engine = engine;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
