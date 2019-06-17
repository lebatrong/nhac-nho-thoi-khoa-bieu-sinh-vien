package com.student.thoikhoabieu.Models.objectClass;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.student.thoikhoabieu.R;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class objthoikhoabieu implements Serializable {
    private int ID;
    private String TenMonHoc;
    private String GiaoVien;
    private String Tiet;
    private String Phong;
    private String Tuan;
    private String Thu;
    private int MonThucHanh;
    private String Ca;

    public objthoikhoabieu() {
    }

    public objthoikhoabieu(String tenMonHoc,
                           String giaoVien,
                           String phong,
                           String tiet,
                           String tuan,
                           String thu,
                           int monThucHanh,
                           String ca) {
        TenMonHoc = tenMonHoc;
        GiaoVien = giaoVien;
        Tiet = tiet;
        Phong = phong;
        Tuan = tuan;
        Thu = thu;
        MonThucHanh = monThucHanh;
        Ca = ca;
    }

    public String getGiaoVien() {
        return GiaoVien;
    }

    public void setGiaoVien(String giaoVien) {
        GiaoVien = giaoVien;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTenMonHoc() {
        String[] strTen = TenMonHoc.split("-");
        if(strTen.length > 1)
            return strTen[1];
        else
            return TenMonHoc;
    }

    public void setTenMonHoc(String tenMonHoc) {
        TenMonHoc = tenMonHoc;
    }

    public String getTiet() {
        return Tiet;
    }

    public void setTiet(String tiet) {
        Tiet = tiet;
    }

    public String getPhong() {
        return Phong;
    }

    public void setPhong(String phong) {
        Phong = phong;
    }

    public String getTuan() {
        return Tuan;
    }

    public void setTuan(String tuan) {
        Tuan = tuan;
    }

    public String getThu() {
        return Thu;
    }

    public void setThu(String thu) {
        Thu = thu;
    }

    public int getMonThucHanh() {
        return MonThucHanh;
    }

    public void setMonThucHanh(int monThucHanh) {
        MonThucHanh = monThucHanh;
    }

    public String getCa() {
        return Ca;
    }

    public void setCa(String ca) {
        Ca = ca;
    }

    //Chuyển thứ 5 tuần 23 năm 2019 về thời gian cụ thể (Millisecond)
    //Trả về -1 (không có thời gian hoặc convert lỗi) hoặc Millisecond của thời gian đó
    public long chuyenTietVeThoiGian(Context context) {
        long thoigian = -1;

        if ((!getCa().equals("[]") || !getTiet().equals("[]")) && !getTuan().equals("")) {

            Calendar calendar = Calendar.getInstance();
            // format về định dạnh thứ (int)-tuần(tuần trong năm)-năm
            SimpleDateFormat format = new SimpleDateFormat("u-w-yyyy - HH:mm");

            //Chuyển thứ kiểu String về int
            int thuTKB;
            if (getThu().matches(context.getResources().getStringArray(R.array.arrThu)[0]))
                thuTKB = 1;
            else if (getThu().matches(context.getResources().getStringArray(R.array.arrThu)[1]))
                thuTKB = 2;
            else if (getThu().matches(context.getResources().getStringArray(R.array.arrThu)[2]))
                thuTKB = 3;
            else if (getThu().matches(context.getResources().getStringArray(R.array.arrThu)[3]))
                thuTKB = 4;
            else if (getThu().matches(context.getResources().getStringArray(R.array.arrThu)[4]))
                thuTKB = 5;
            else if (getThu().matches(context.getResources().getStringArray(R.array.arrThu)[5]))
                thuTKB = 6;
            else
                thuTKB = 7;

            //chuyển tuần 2-3-4 về mảng int
            //Kiểm tra tuần hiện tại có nằm trong tuần học của tkb hay không. nếu không return -1
            String[] tuanTKB = getTuan().split("-");
            if (calendar.get(Calendar.WEEK_OF_YEAR) <= Integer.parseInt(tuanTKB[0]) ||
                    calendar.get(Calendar.WEEK_OF_YEAR) >= Integer.parseInt(tuanTKB[tuanTKB.length - 1])) {
                return -1;
            }

            String strTime = thuTKB + "-" + calendar.get(Calendar.WEEK_OF_YEAR) + "-" + calendar.get(Calendar.YEAR) + " - " + chuyenTietHoacCaVeGio();

            try {
                calendar.setTime(format.parse(strTime));
                thoigian = calendar.getTimeInMillis();
            } catch (ParseException e) {
                Log.e("kiemtra", e.toString());
                e.printStackTrace();
            }

        }
        return thoigian;
    }

    private String chuyenTietHoacCaVeGio() {
        String strGio = "";
        //La mon thuc hanh
        if (getMonThucHanh() == 1) {
            //Chuyên json array về arraylist
            ArrayList<Integer> cathuchanh = new Gson().fromJson(getCa(), new TypeToken<ArrayList<Integer>>() {
            }.getType());
            switch (cathuchanh.get(0)) {
                case 1:
                    strGio = "06:30";
                    break;
                case 2:
                    strGio = "09:30";
                    break;
                case 3:
                    strGio = "12:30";
                    break;
                default:
                    strGio = "15:30";
                    break;
            }
        } else {
            //Chuyên json array về arraylist
            ArrayList<Integer> tiethuchanh = new Gson().fromJson(getTiet(), new TypeToken<ArrayList<Integer>>() {
            }.getType());
            switch (tiethuchanh.get(0)) {
                case 1:
                    strGio = "07:00";
                    break;
                case 2:
                    strGio = "07:50";
                    break;
                case 3:
                    strGio = "09:00";
                    break;
                case 4:
                    strGio = "10:00";
                    break;
                case 5:
                    strGio = "10:50";
                    break;
                case 6:
                    strGio = "13:00";
                    break;
                case 7:
                    strGio = "13:50";
                    break;
                case 8:
                    strGio = "15:00";
                    break;
                case 9:
                    strGio = "16:00";
                    break;
                case 10:
                    strGio = "16:50";
                    break;
                case 11:
                    strGio = "18:30";
                    break;
                case 12:
                    strGio = "19:20";
                    break;
                default:
                    strGio = "20:10";
                    break;
            }
        }
        return strGio;
    }

    //Sap xep giảm dần
    public static ArrayList<objthoikhoabieu> sapXepTKBTheoThoiGian(ArrayList<objthoikhoabieu> mList, final Context context) {
        Collections.sort(mList, new Comparator<objthoikhoabieu>() {
            @Override
            public int compare(objthoikhoabieu o1, objthoikhoabieu o2) {
                if (o1.chuyenTietVeThoiGian(context) > o2.chuyenTietVeThoiGian(context))
                    return 1;
                else if (o1.chuyenTietVeThoiGian(context) < o2.chuyenTietVeThoiGian(context))
                    return -1;
                else
                    return 0;
            }
        });
        return mList;
    }

    public static objthoikhoabieu getThongTinMonHocGanNhat(ArrayList<objthoikhoabieu> listTKB, Context context) {
        listTKB = sapXepTKBTheoThoiGian(listTKB, context);
        objthoikhoabieu tkb = listTKB.get(0);
        for (objthoikhoabieu i : listTKB) {
            if (i.chuyenTietVeThoiGian(context) > Calendar.getInstance().getTimeInMillis()) {
                tkb = i;
                return tkb;
            }
        }
        return tkb;
    }

    public static String getStringThoiGianGanNhat(objthoikhoabieu tkbGanNhat,Context context) {
        if ((!tkbGanNhat.getCa().equals("[]") || !tkbGanNhat.getTiet().equals("[]")) && !tkbGanNhat.getTuan().equals("")) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(tkbGanNhat.chuyenTietVeThoiGian(context));
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            if (tkbGanNhat.getMonThucHanh() == 0)
                return "Tiết " + tkbGanNhat.getTiet().substring(1, tkbGanNhat.getTiet().length() - 1) + " (" + tkbGanNhat.getThu() + " - " + dateFormat.format(calendar.getTime()) + ")";
            else
                return "Ca " + tkbGanNhat.getCa().substring(1, tkbGanNhat.getCa().length() - 1) + " (" + tkbGanNhat.getThu() + " - " + dateFormat.format(calendar.getTime()) + ")";

        }else
            return "";
    }
}
