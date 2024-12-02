package com.tondz.ghephinh.utils;

import com.tondz.ghephinh.models.CauHoi;
import com.tondz.ghephinh.models.Entity;
import com.tondz.ghephinh.models.HinhNen;
import com.tondz.ghephinh.models.KiHieu;
import com.tondz.ghephinh.models.Preview;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Common {
    public static int index=0;
    public static String idTheGioi = "";
    public static String idChauLuc = "";
    public static String idQuocGia= "", idKhuVuc= "", idTinh= "", idHuyen= "", idXa= "";
    public static List<Entity> entityList = new ArrayList<>();
    public static Entity entity = new Entity();
    public static List<KiHieu> kiHieuList = new ArrayList<>();
    public static List<HinhNen> hinhGhepList = new ArrayList<>();
    public static Set<String> loaiKiHieuList= new HashSet<>();
    public static CauHoi CAU_HOI = new CauHoi();
    public static ArrayList<CauHoi> cauHoiArrayList=new ArrayList<>();
    public static List<Preview> previewList = new ArrayList<>();
}
