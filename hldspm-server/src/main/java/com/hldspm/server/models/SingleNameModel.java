package com.hldspm.server.models;

public class SingleNameModel {
   private String name;

    public SingleNameModel(String name) {
        if (name.length() != 0) this.name = name;
        else this.name = "None";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
