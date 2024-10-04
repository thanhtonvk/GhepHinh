package com.tondz.ghephinh.models;

import androidx.annotation.NonNull;

import java.util.List;

public class KiHieu {
    private String id, name;
    private List<String> multiple_image_urls;

    public KiHieu(String id, String name, List<String> multiple_image_urls) {
        this.id = id;
        this.name = name;
        this.multiple_image_urls = multiple_image_urls;
    }

    public KiHieu() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMultiple_image_urls() {
        return multiple_image_urls;
    }

    public void setMultiple_image_urls(List<String> multiple_image_urls) {
        this.multiple_image_urls = multiple_image_urls;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
