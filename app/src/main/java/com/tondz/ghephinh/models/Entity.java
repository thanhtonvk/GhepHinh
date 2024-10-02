package com.tondz.ghephinh.models;

import android.graphics.Bitmap;

import com.squareup.picasso.Picasso;

import java.io.IOException;

public class Entity {
    private String info, name, url;

    public Entity() {
    }

    public Entity(String info, String name, String url) {
        this.info = info;
        this.name = name;
        this.url = url;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
