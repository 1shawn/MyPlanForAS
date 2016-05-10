package com.andwho.myplan.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.andwho.myplan.table.MsgTable;

/**
 * Created by zhouf on 2016/5/9.
 * 消息中心
 */
public class MyPlanDatabaseHelper extends SQLiteOpenHelper {
    private static MyPlanDatabaseHelper helper;
    public static final int VERSION = 1;
    public static final String mDbName = "MessageCenterDB";

    MsgTable msgTable=new MsgTable();
    public MyPlanDatabaseHelper(Context context) {
        super(context, mDbName, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        msgTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        msgTable.onUpgrade(db, oldVersion, newVersion);
    }

    //清空Table表
    public boolean delDTable() {
        boolean ret = false;
        try {
            SQLiteDatabase db = helper.getReadableDatabase();
            if (db.isDbLockedByCurrentThread() || db.isReadOnly()) {
                db = null;
            }
            if (db != null) {
              msgTable.delTable(db);
            }
        } catch (Exception ex) {

        }
        return ret;
    }

    public static synchronized SQLiteDatabase getSQLiteDatabase(Context context) {
        helper = getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        if (db.isDbLockedByCurrentThread() || db.isReadOnly()) {
            db = null;
        }
        return db;
    }

    public static MyPlanDatabaseHelper getInstance(Context context) {
        if (helper == null) {
            helper = new MyPlanDatabaseHelper(context);
        }
        return helper;
    }

    public static SQLiteDatabase getDatabase(Context context) {
        return getSQLiteDatabase(context);
    }
}

