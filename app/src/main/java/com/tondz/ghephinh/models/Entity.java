package com.tondz.ghephinh.models;

import android.graphics.Bitmap;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class Entity {
    private String id, info, link, name, single_image_url;
    private List<String> multiple_image_urls;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSingle_image_url() {
        return single_image_url;
    }

    public void setSingle_image_url(String single_image_url) {
        this.single_image_url = single_image_url;
    }

    public List<String> getMultiple_image_urls() {
        return multiple_image_urls;
    }

    public void setMultiple_image_urls(List<String> multiple_image_urls) {
        this.multiple_image_urls = multiple_image_urls;
    }

    public Entity(String id, String info, String link, String name, String single_image_url, List<String> multiple_image_urls) {
        this.id = id;
        this.info = info;
        this.link = link;
        this.name = name;
        this.single_image_url = single_image_url;
        this.multiple_image_urls = multiple_image_urls;
    }

    public Entity() {
    }
}
