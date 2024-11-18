package com.tondz.ghephinh.utils;

import com.tondz.ghephinh.models.CauHoi;
import com.tondz.ghephinh.models.Entity;
import com.tondz.ghephinh.models.HinhNen;
import com.tondz.ghephinh.models.KiHieu;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Common {
    public static int index;
    public static String idTheGioi = "";
    public static String idChauLuc = "";
    public static String idQuocGia, idKhuVuc, idTinh, idHuyen, idXa;
    public static List<Entity> entityList;
    public static Entity entity;
    public static List<KiHieu> kiHieuList = new ArrayList<>();
    public static List<HinhNen> hinhGhepList = new ArrayList<>();
    public static Set<String> loaiKiHieuList;
    public static CauHoi CAU_HOI;
    public static ArrayList<CauHoi> cauHoiArrayList;
}
