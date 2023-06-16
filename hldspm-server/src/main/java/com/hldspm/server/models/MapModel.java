package com.hldspm.server.models;

/**Model for the Map object*/
public class MapModel {
    private long id;
    private int engine;
    private String game;
    private String name;
    private String gameMode;


    /**Empty constructor*/
    public MapModel(){

    }

    /**Default Constructor*/
    public MapModel(long id, int engine, String game, String name, String gameMode) {
        this.id = id;
        this.engine = engine;
        this.game = game;
        this.name = name;
        this.gameMode = gameMode;
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

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    @Override
    public String toString() {
        return "MapModel{" +
                "id=" + id +
                ", engine=" + engine +
                ", game='" + game + '\'' +
                ", name='" + name + '\'' +
                ", gameMode='" + gameMode + '\'' +
                '}';
    }
}
