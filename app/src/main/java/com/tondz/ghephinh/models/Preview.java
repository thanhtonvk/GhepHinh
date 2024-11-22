package com.tondz.ghephinh.models;

public class Preview {
    private String id, url;

    public Preview(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public Preview() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
