package com.tondz.ghephinh.models;

import androidx.annotation.NonNull;

import java.util.List;

public class KiHieu {
    private String id, group, info, link, name, single_image_url;
    private List<String> multiple_image_urls;

    public KiHieu() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
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

    public KiHieu(String id, String group, String info, String link, String name, String single_image_url, List<String> multiple_image_urls) {
        this.id = id;
        this.group = group;
        this.info = info;
        this.link = link;
        this.name = name;
        this.single_image_url = single_image_url;
        this.multiple_image_urls = multiple_image_urls;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
