package com.hldspm.server.models;

public class CountModel {
    private long count;

    public CountModel(){}

    public CountModel(long count){
        this.count = count;
    }

    @Override
    public String toString() {
        return "CountModel{" +
                "count=" + count +
                '}';
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
