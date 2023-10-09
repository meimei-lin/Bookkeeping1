package com.example.bookkeeping.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.bookkeeping.R;

import java.io.File;

public class DBOpenHelper extends SQLiteOpenHelper {
    public DBOpenHelper(@Nullable Context context) {
        super(context,"tally.db",null,1);
    }
    //創建資料庫方法
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table typetb(id integer primary key autoincrement, typename varchar(10),imageId integer, sImageId integer, kind integer)";
        db.execSQL(sql);
        insertType(db);
        //創建記帳表
        sql = "create table accounttb(id integer primary key autoincrement, typename varchar(10), sImageId integer, remark varchar(80), money float," +
                "time varchar(60), year integer, month integer, day integer, kind integer)";
        db.execSQL(sql);
    }
    public void insertType(SQLiteDatabase db){
        String sql = "insert into typetb (typename, imageId, sImageId, kind) values (?,?,?,?)";
        db.execSQL(sql,new Object[]{"其他", R.drawable.pending,R.drawable.ic_other_fs,0});
        db.execSQL(sql,new Object[]{"飲食", R.drawable.ic_nofood,R.drawable.ic_food,0});
        db.execSQL(sql,new Object[]{"交通", R.drawable.ic_bus_fs,R.drawable.ic_bus,0});
        db.execSQL(sql,new Object[]{"購物", R.drawable.ic_shopping_fs,R.drawable.ic_shopping,0});
        db.execSQL(sql,new Object[]{"服飾", R.drawable.ic_clothes_fs,R.drawable.ic_clothes,0});
        db.execSQL(sql,new Object[]{"日用品", R.drawable.ic_day_fs,R.drawable.ic_day,0});
        db.execSQL(sql,new Object[]{"娛樂", R.drawable.ic_play_fs,R.drawable.ic_play,0});
        db.execSQL(sql,new Object[]{"零食", R.drawable.ic_snack_fs,R.drawable.ic_snack,0});
        db.execSQL(sql,new Object[]{"教育", R.drawable.ic_teach_fs,R.drawable.ic_teach,0});
        db.execSQL(sql,new Object[]{"醫療", R.drawable.ic_hospital_fs,R.drawable.ic_hospital,0});
        db.execSQL(sql,new Object[]{"水電", R.drawable.ic_energy,R.drawable.ic_energy_fs,0});
        db.execSQL(sql,new Object[]{"通訊", R.drawable.ic_phone,R.drawable.ic_phone_fs,0});

        db.execSQL(sql,new Object[]{"其他", R.drawable.ic_other1,R.drawable.ic_other_fs1,1});
        db.execSQL(sql,new Object[]{"薪資", R.drawable.ic_salary,R.drawable.ic_salary_fs,1});
        db.execSQL(sql,new Object[]{"獎金", R.drawable.ic_gift,R.drawable.ic_gift_fs,1});;
        db.execSQL(sql,new Object[]{"收債", R.drawable.ic_debt,R.drawable.ic_debt_fs,1});
        db.execSQL(sql,new Object[]{"股票", R.drawable.ic_investment,R.drawable.ic_investment_fs,1});
        db.execSQL(sql,new Object[]{"利息收入", R.drawable.ic_tax,R.drawable.ic_tax_fs,1});;
        db.execSQL(sql,new Object[]{"二手交易", R.drawable.ic_secondhand,R.drawable.ic_secondhand_fs,1});
        db.execSQL(sql,new Object[]{"意外所得", R.drawable.ic_income,R.drawable.ic_income_fs,1});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void deleteDatabase(Context context) {
        String dbPath = context.getDatabasePath("tally.db").getPath();

        // 關閉資料庫連接
        close();

        // 刪除資料庫檔案
        SQLiteDatabase.deleteDatabase(new File(dbPath));

        // 重新開啟資料庫（如果需要的話）
        getWritableDatabase();
    }
}
