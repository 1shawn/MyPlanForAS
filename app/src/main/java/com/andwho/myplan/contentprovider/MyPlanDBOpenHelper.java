package com.andwho.myplan.contentprovider;

import android.content.Context;
import android.database.Cursor;
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
	//通过该适配类可以用一连续的游标(Coursor)对象访问数据库，并将查询出来的数据展示到可伸缩的列表视图(ExpandableListView)部件上。顶层游标(Cursor)对象(在构造器中指定)显示全部组，后面的游标(Cursor)对象从getChildrenCursor(Cursor)获取并展示子元素组。
	// 其中游标携带的结果集中必须有个名为“_id”的列，否则这个类不起任何作用。
	public static final String ID="_id";
	public static final String PLANID = "planid";// 表键
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
		super(context, DB_NAME, null, 2);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + TABLE_NAME + "(" + PLANID
				+ " integer primary key autoincrement," +ID + " TEXT,"+ CONTENT + " TEXT,"
				+ CREATETIME + " TEXT," + COMPLETETIME + " TEXT," + UPDATETIME
				+ " TEXT," + ISCOMPLETED + " TEXT," + ISNOTIFY + " TEXT,"
				+ NOTIFYTIME + " TEXT," + PLANTYPE + " TEXT," + ISDELETED
				+ " TEXT)");

	}

	// 当数据库版本号发生变化时调用该方法
	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		if(arg1<=1&&!checkColumnExist1(db,TABLE_NAME,ID)) {
			db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD " + ID + " varchar(12) NULL");
		}
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


	/**
	 * 方法1：检查某表列是否存在:根据 cursor.getColumnIndex(String columnName) 的返回值判断，如果为-1表示表中无此字段
	 * @param db
	 * @param tableName 表名
	 * @param columnName 列名
	 * @return
	 */
	private boolean checkColumnExist1(SQLiteDatabase db, String tableName
			, String columnName) {
		boolean result = false ;
		Cursor cursor = null ;
		try{
			//查询一行
			cursor = db.rawQuery( "SELECT * FROM " + tableName + " LIMIT 0"
					, null );
			result = cursor != null && cursor.getColumnIndex(columnName) != -1 ;
		}catch (Exception e){
//			Log.e(TAG,"checkColumnExists1..." + e.getMessage()) ;
		}finally{
			if(null != cursor && !cursor.isClosed()){
				cursor.close() ;
			}
		}

		return result ;
	}
}