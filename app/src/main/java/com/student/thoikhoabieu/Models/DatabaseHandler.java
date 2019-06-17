package com.student.thoikhoabieu.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.student.thoikhoabieu.Models.objectClass.objthoikhoabieu;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "thoikhoabieusinhvien";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "thoikhoabieu";

    private static final String ID = "id";
    private static final String TENMONHOC = "TenMonHoc";
    private static final String GIAOVIEN = "GiaoVien";
    private static final String TIET = "Tiet";
    private static final String PHONG = "Phong";
    private static final String TUAN = "Tuan";
    private static final String THU = "Thu";
    private static final String MONTHUCHANH = "MonThucHanh";
    private static final String CA = "CaHoc";


    private Context context;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_Table =  String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s INTEGER, %s TEXT)",
                TABLE_NAME,
                ID,
                TENMONHOC,
                GIAOVIEN,
                TIET,
                PHONG,
                TUAN,
                THU,
                MONTHUCHANH,
                CA);
        db.execSQL(create_Table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop_students_table = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        db.execSQL(drop_students_table);

        onCreate(db);
    }

    public boolean updateRecodeIfExit(objthoikhoabieu thoikhoabieu){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = String.format("Select ID from %s where ID = %s",
                TABLE_NAME,
                thoikhoabieu.getID());

        Cursor cursor = db.rawQuery(sql,null);

        //Update
        if(cursor.moveToNext())
            return updateRecord(thoikhoabieu);
        //Add mới
        else
            return addRecord(thoikhoabieu);

    }

    public boolean updateRecord(objthoikhoabieu thoikhoabieu){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TENMONHOC,thoikhoabieu.getTenMonHoc());
        values.put(GIAOVIEN,thoikhoabieu.getGiaoVien());
        values.put(TIET,thoikhoabieu.getTiet());
        values.put(PHONG,thoikhoabieu.getPhong());
        values.put(TUAN,thoikhoabieu.getTuan());
        values.put(THU,thoikhoabieu.getThu());
        values.put(MONTHUCHANH,thoikhoabieu.getMonThucHanh());
        values.put(CA,thoikhoabieu.getCa());
        if(db.update(TABLE_NAME,values,ID + " = ?", new String[]{String.valueOf(thoikhoabieu.getID())}) != -1){
            db.close();
            return true;
        }else{
            db.close();
            return false;
        }
    }


    public boolean addRecord(objthoikhoabieu thoikhoabieu){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TENMONHOC,thoikhoabieu.getTenMonHoc());
        values.put(GIAOVIEN,thoikhoabieu.getGiaoVien());
        values.put(TIET,thoikhoabieu.getTiet());
        values.put(PHONG,thoikhoabieu.getPhong());
        values.put(TUAN,thoikhoabieu.getTuan());
        values.put(THU,thoikhoabieu.getThu());
        values.put(MONTHUCHANH,thoikhoabieu.getMonThucHanh());
        values.put(CA,thoikhoabieu.getCa());
        if(db.insert(TABLE_NAME,null,values)!=-1){
            db.close();
            return true;
        }else{
            db.close();
            return false;
        }

    }

//    public objthoikhoabieu getThoiKhoaBieu(int ID){
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(TABLE_NAME,
//                null,
//                this.ID+ " = ?",
//                new String[]{ String.valueOf(this.ID) },
//                null,
//                null,
//                null);
//
//    }

    public ArrayList<objthoikhoabieu> getAllThoiKhoaBieu(){
        ArrayList<objthoikhoabieu> arr = new ArrayList<>();
        String SELECT = "SELECT * FROM "+ TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(SELECT,null);
        if(cursor.moveToFirst()){
            do{
                objthoikhoabieu tkb = new objthoikhoabieu();
                //ID
                tkb.setID(cursor.getInt(0));
                //TEN MON HOC
                tkb.setTenMonHoc(cursor.getString(1));
                //GIAO VIEN
                tkb.setGiaoVien(cursor.getString(2));
                //TIET
                tkb.setTiet(cursor.getString(3));
                //PHONG
                tkb.setPhong(cursor.getString(4));
                //TUAN
                tkb.setTuan(cursor.getString(5));
                //THU
                tkb.setThu(cursor.getString(6));
                //MON THUC HANH
                tkb.setMonThucHanh(cursor.getInt(7));
                //CA
                tkb.setCa(cursor.getString(8));

                arr.add(tkb);

            }while (cursor.moveToNext());
        }
        db.close();
        return arr;
    }

    public void deleteAllRecord(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();
    }

    public void deleteRecord(objthoikhoabieu tkb){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE ID = "+ tkb.getID());
        db.close();
    }
}