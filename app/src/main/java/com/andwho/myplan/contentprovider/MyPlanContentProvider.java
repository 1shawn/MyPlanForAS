package com.andwho.myplan.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * @author ouyyx 提醒ContentProvider
 * */
public class MyPlanContentProvider extends ContentProvider {
	private MyPlanDBOpenHelper dbOpenHelper;
	private UriMatcher uriMatcher;
	private static final int PLANS = 0;//
	private static final int PLAN_ID = 1;

	@Override
	public boolean onCreate() {
		initUriMatcher();
		dbOpenHelper = new MyPlanDBOpenHelper(getContext());
		return true;
	}

	private void initUriMatcher() {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(MyPlanDBOpenHelper.AUTHORITY,
				MyPlanDBOpenHelper.TABLE_NAME, PLANS);
		uriMatcher.addURI(MyPlanDBOpenHelper.AUTHORITY,
				MyPlanDBOpenHelper.TABLE_NAME + "/#", PLAN_ID);
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		switch (uriMatcher.match(uri)) {
		case PLANS:

			long rowid = db.insert(MyPlanDBOpenHelper.TABLE_NAME,
					MyPlanDBOpenHelper.CONTENT + ","
							+ MyPlanDBOpenHelper.CREATETIME + ","
							+ MyPlanDBOpenHelper.COMPLETETIME + ","
							+ MyPlanDBOpenHelper.UPDATETIME + ","
							+ MyPlanDBOpenHelper.ISCOMPLETED + ","
							+ MyPlanDBOpenHelper.ISNOTIFY + ","
							+ MyPlanDBOpenHelper.NOTIFYTIME + ","
							+ MyPlanDBOpenHelper.PLANTYPE + ","
							+ MyPlanDBOpenHelper.ISDELETED, values);

			return ContentUris.withAppendedId(uri, rowid);
		default:
			throw new IllegalArgumentException("unknown uri" + uri.toString());
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		int updataNum = 0;
		switch (uriMatcher.match(uri)) {

		case PLANS:
			updataNum = db.update(MyPlanDBOpenHelper.TABLE_NAME, values,
					selection, selectionArgs);
			break;

		case PLAN_ID:
			long id = ContentUris.parseId(uri);
			String where = MyPlanDBOpenHelper.PLANID + "=" + id;
			if (selection != null && !"".equals(selection.trim())) {
				where = selection + " and " + where;
			}
			updataNum = db.update(MyPlanDBOpenHelper.TABLE_NAME, values, where,
					selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("unknown uri" + uri.toString());
		}
		return updataNum;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		int deletedNum = 0;
		switch (uriMatcher.match(uri)) {

		case PLANS:
			deletedNum = db.delete(MyPlanDBOpenHelper.TABLE_NAME, selection,
					selectionArgs);
			break;

		case PLAN_ID:
			long id = ContentUris.parseId(uri);
			String where = MyPlanDBOpenHelper.PLANID + "=" + id;
			if (selection != null && !"".equals(selection.trim())) {
				where = selection + " and " + where;
			}
			deletedNum = db.delete(MyPlanDBOpenHelper.TABLE_NAME, where,
					selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("unknown uri" + uri.toString());
		}
		return deletedNum;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		Cursor cursor;
		switch (uriMatcher.match(uri)) {

		case PLANS:
			cursor = db.query(MyPlanDBOpenHelper.TABLE_NAME, projection,
					selection, selectionArgs, null, null, sortOrder);
			break;

		case PLAN_ID:

			long id = ContentUris.parseId(uri);
			String where = MyPlanDBOpenHelper.PLANID + "=" + id;

			if (selection != null && !"".equals(selection.trim())) {
				where = selection + " and " + where;
			}
			cursor = db.query(MyPlanDBOpenHelper.TABLE_NAME, projection, where,
					selectionArgs, null, null, sortOrder);
			break;
		default:
			throw new IllegalArgumentException("unknown uri" + uri.toString());
		}
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case PLANS:
			return "vnd.android.cursor.dir/myplan";
		case PLAN_ID:
			return "vnd.android.cursor.item/myplan";
		default:
			throw new IllegalArgumentException("unknown uri" + uri.toString());
		}
	}

}