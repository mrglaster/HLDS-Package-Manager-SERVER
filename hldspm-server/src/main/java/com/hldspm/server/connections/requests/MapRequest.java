package com.hldspm.server.connections.requests;

public class MapRequest {
    private String game;
    private String name;
    private String engine;

    public MapRequest(){}

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

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    @Override
    public String toString() {
        return "MapRequest{" +
                "game='" + game + '\'' +
                ", name='" + name + '\'' +
                ", engine='" + engine + '\'' +
                '}';
    }

    public MapRequest(String game, String name, String engine) {
        this.game = game;
        this.name = name;
        this.engine = engine;
    }
}
