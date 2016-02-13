package com.andwho.myplan.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author ouyyx 数据库相关信息
 * */
public class MyPlanDBOpenHelper extends SQLiteOpenHelper {

	// 数据库名
	public static final String DB_NAME = "myplan_provider.db";// 数据库名
	// 表名
	public static final String TABLE_NAME = "myplan";// 表名
	// 表字段
	public static final String PLANID = "_id";// 表键
	public static final String CONTENT = "content";// 内容
	public static final String CREATETIME = "createtime";// 创建时间
	public static final String COMPLETETIME = "completetime";// 完成时间
	public static final String UPDATETIME = "updatetime";// 更新时间
	public static final String ISCOMPLETED = "iscompleted";// 是否完成
	public static final String ISNOTIFY = "isnotify";// 是否通知
	public static final String NOTIFYTIME = "notifytime";// 通知时间
	public static final String PLANTYPE = "plantype";// 计划类型
	public static final String ISDELETED = "isdeleted";// 是否已删除

	// authority
	public static final String AUTHORITY = "com.andwho.myplanprovider";//
	// URL
	public static final String CONTENT_URI = "content://" + AUTHORITY + "/"
			+ TABLE_NAME;

	public MyPlanDBOpenHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + TABLE_NAME + "(" + PLANID
				+ " integer primary key autoincrement," + CONTENT + " TEXT,"
				+ CREATETIME + " TEXT," + COMPLETETIME + " TEXT," + UPDATETIME
				+ " TEXT," + ISCOMPLETED + " TEXT," + ISNOTIFY + " TEXT,"
				+ NOTIFYTIME + " TEXT," + PLANTYPE + " TEXT," + ISDELETED
				+ " TEXT)");

	}

	// 当数据库版本号发生变化时调用该方法
	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// db.execSQL("ALTER TABLE person ADD phone varchar(12) NULL");
		// db.execSQL("ALTER TABLE person ADD salary Integer NULL");
		//
		// if (oldVersion < 4) {
		// db.execSQL("DROP TABLE IF EXISTS " + "stat_table"); // 删除不用的表
		// db.execSQL("DROP TABLE IF EXISTS " + DB_BASE_INFO_TABLE);

		// onCreate(db);
		// } else if (oldVersion == 4) {
		// db.execSQL(ADD_CALLLOG_REPORT_TIME_COLUMN_SQL);

		// }

	}
}