package com.andwho.myplan.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by zhouf on 2016/5/9.
 */
public class MsgTable  implements MyPlanTable {
    public static final String TABLE_NAME = "msg_Table";//消息表
    public static final String ID = "id";//id
    public static final String TITLE = "title";//标题
    public static final String CONTENT = "content";//内容
    public static final String DETAILURL = "detailurl";//
    public static final String IMGURLARRAY ="imgurlarray";//
    public static final String READED ="readed";//已读 boolean
    private static final String CREATE_TABLE ="CREATE TABLE " + TABLE_NAME + "(" + ID +" integer primary key autoincrement," + TITLE +
            " TEXT," + CONTENT + " TEXT," + DETAILURL  + " TEXT," + IMGURLARRAY + " TEXT," + READED  +" bool"+ ")";
    private static final String DROP_EVENT_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String DELETE_TABLE="DELETE FROM "+TABLE_NAME;//清空数据

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_EVENT_TABLE);
        onCreate(db);
    }

    public void dropTable(SQLiteDatabase db){
        db.execSQL(DROP_EVENT_TABLE);
    }

    public void delTable(SQLiteDatabase db){
        db.execSQL(DELETE_TABLE);
    }
}
