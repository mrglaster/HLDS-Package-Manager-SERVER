package com.hldspm.server.models;

/**Class for id & name fields storage*/
public class SimpleRecordModel {
    private long id;
    private String name;


    public SimpleRecordModel(){}

    public SimpleRecordModel(long id, String name) {
        this.id = id;
        this.name = name;
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
}


