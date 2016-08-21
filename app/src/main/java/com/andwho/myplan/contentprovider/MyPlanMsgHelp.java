package com.andwho.myplan.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhouf on 16/4/24.
 */
public class MyPlanMsgHelp extends SQLiteOpenHelper {

    // 数据库名
    public static final String DB_NAME = "myplan_provider.db";// 数据库名
    // 表名
    public static final String TABLE_NAME = "myplanMsgCenter";// 表名
    // 表字段
    public static final String ID = "_id";// 表键
    public static final String TITILE = "title";// 标题
    public static final String CONTENT = "content";// 内容
    public static final String AUTHORITY = "com.andwho.myplanprovider";//
    // URL
    public static final String CONTENT_URI = "content://" + AUTHORITY + "/"
            + TABLE_NAME;

    public MyPlanMsgHelp(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(" + ID
                + " integer primary key autoincrement," + TITILE + " TEXT,"
                + CONTENT +  " TEXT)");

    }

    // 当数据库版本号发生变化时调用该方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {

    }
}
