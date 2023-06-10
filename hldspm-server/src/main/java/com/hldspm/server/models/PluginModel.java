package com.hldspm.server.models;

import com.sun.source.util.Plugin;

public class PluginModel {
    private long id;
    private int engine;
    private String game;

    private String name;


    public PluginModel(){}

    public PluginModel(long id, int engine, String game, String uploader, String name) {
        this.id = id;
        this.engine = engine;
        this.game = game;
        this.name = name;
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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PluginModel{" +
                "id=" + id +
                ", engine=" + engine +
                ", game='" + game + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
