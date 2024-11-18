package com.tondz.ghephinh.models;

public class HinhNen {
    private String id;
    private String ten;
    private String hinhAnh;

    public HinhNen(String id, String ten, String hinhAnh) {
        this.id = id;
        this.ten = ten;
        this.hinhAnh = hinhAnh;
    }

    public HinhNen() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
}
