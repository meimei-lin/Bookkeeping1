package com.example.bookkeeping;

import android.app.Application;

import com.example.bookkeeping.db.DBManager;

//表示全局應用的類
public class UniteApp extends Application {
    public void onCreate(){
        super.onCreate();
        //初始化資料庫
        DBManager.initDB(getApplicationContext());
    }
}
