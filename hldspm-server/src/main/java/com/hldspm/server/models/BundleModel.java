package com.hldspm.server.models;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class BundleModel {
    private long id;
    private String name;
    private Array pluginIds;

    public BundleModel(){}

    public BundleModel(long id, String name, Array pluginIds) {
        this.id = id;
        this.name = name;
        this.pluginIds = pluginIds;
    }

    @Override
    public String toString() {
        return "BundleModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pluginIds=" + pluginIds.toString() +
                '}';
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

    public Array getPluginIds() {
        return pluginIds;
    }

    public void setPluginIds(Array pluginIds) {
        this.pluginIds = pluginIds;
    }
}
