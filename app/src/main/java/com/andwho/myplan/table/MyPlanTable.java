package com.andwho.myplan.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by zhouf on 2016/5/9.
 */
public interface MyPlanTable {

    void onCreate(SQLiteDatabase db);
    void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion);
}