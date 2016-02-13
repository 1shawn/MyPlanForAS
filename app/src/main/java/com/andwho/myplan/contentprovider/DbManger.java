package com.andwho.myplan.contentprovider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.andwho.myplan.constants.PlanType;
import com.andwho.myplan.model.DatePlans;
import com.andwho.myplan.model.Plan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

/**
 * @author ouyyx
 */
@SuppressWarnings("ALL")
public class DbManger {
    //

    public static final boolean DEBUG = false;

    private static final String TAG = DbManger.class.getSimpleName();

    private static Context context;
    private static DbManger instance;
    private static ContentResolver mContentResolver;

    private DbManger() {
    }

    private DbManger(Context ctx) {
        context = ctx.getApplicationContext();
    }

    public synchronized static DbManger getInstance(Context ctx) {
        if (instance == null) {
            instance = new DbManger(ctx);
            mContentResolver = ctx.getContentResolver();
        }

        return instance;
    }

    public boolean addPlan(Plan plan) {

        ContentValues cv = new ContentValues();
        cv.put(MyPlanDBOpenHelper.CONTENT, plan.content);
        cv.put(MyPlanDBOpenHelper.CREATETIME, plan.createtime);
        cv.put(MyPlanDBOpenHelper.COMPLETETIME, plan.completetime);
        cv.put(MyPlanDBOpenHelper.UPDATETIME, plan.updatetime);
        cv.put(MyPlanDBOpenHelper.ISCOMPLETED, plan.iscompleted);
        cv.put(MyPlanDBOpenHelper.ISNOTIFY, plan.isnotify);
        cv.put(MyPlanDBOpenHelper.NOTIFYTIME, plan.notifytime);
        cv.put(MyPlanDBOpenHelper.PLANTYPE, plan.plantype);
        cv.put(MyPlanDBOpenHelper.ISDELETED, plan.isdeleted);

        Uri uri = mContentResolver.insert(
                Uri.parse(MyPlanDBOpenHelper.CONTENT_URI), cv);
        return uri == null ? false : true;
    }

    public void deletePlan(String id) {
        String where = MyPlanDBOpenHelper.PLANID + " =" + id;
        mContentResolver.delete(Uri.parse(MyPlanDBOpenHelper.CONTENT_URI),
                where, null);
    }

    // 通过日期查询“每日计划”
    public Cursor getEverydayPlanByDate(String createDate) {
        String where = MyPlanDBOpenHelper.PLANTYPE + " = 1  AND  "
                + MyPlanDBOpenHelper.CREATETIME + " = '" + createDate + "'"
                + "  ORDER BY " + MyPlanDBOpenHelper.UPDATETIME + " DESC";
        Cursor cursor = mContentResolver.query(
                Uri.parse(MyPlanDBOpenHelper.CONTENT_URI), null, where, null,
                null);

        return cursor;
    }

    // 查询“每日计划”的日期
    public Cursor getEverydayPlanDate() {
        String where = MyPlanDBOpenHelper.PLANTYPE + " = 1  GROUP BY  "
                + MyPlanDBOpenHelper.CREATETIME
                + "  ORDER BY " + MyPlanDBOpenHelper.CREATETIME + " DESC";
        String[] projection = new String[]{
                MyPlanDBOpenHelper.CREATETIME
        };
        Cursor cursor = mContentResolver.query(
                Uri.parse(MyPlanDBOpenHelper.CONTENT_URI), null, where, null,
                null);

        return cursor;
    }


    public ArrayList<Plan> queryPlans(String planType, String iscompleted) {
        ArrayList<Plan> listPlan = new ArrayList<Plan>();

//        String where = null;
//        if (!TextUtils.isEmpty(iscompleted)) {
//            where = MyPlanDBOpenHelper.PLANTYPE + " =" + planType + " and "
//                    + MyPlanDBOpenHelper.ISCOMPLETED + " = " + iscompleted;
//        } else {
//            where = MyPlanDBOpenHelper.PLANTYPE + " =" + planType;
//        }
//        Cursor cursor = mContentResolver.query(
//                Uri.parse(MyPlanDBOpenHelper.CONTENT_URI), null, where, null,
//                null);

        Cursor cursor = getPlanCursor(planType, iscompleted);
        while (cursor.moveToNext()) {
            Plan plan = new Plan();
            plan.planid = String.valueOf(cursor.getInt(cursor
                    .getColumnIndex(MyPlanDBOpenHelper.PLANID)));
            plan.content = cursor.getString(cursor
                    .getColumnIndex(MyPlanDBOpenHelper.CONTENT));
            plan.createtime = cursor.getString(cursor
                    .getColumnIndex(MyPlanDBOpenHelper.CREATETIME));
            plan.completetime = cursor.getString(cursor
                    .getColumnIndex(MyPlanDBOpenHelper.COMPLETETIME));
            plan.updatetime = cursor.getString(cursor
                    .getColumnIndex(MyPlanDBOpenHelper.UPDATETIME));
            plan.iscompleted = cursor.getString(cursor
                    .getColumnIndex(MyPlanDBOpenHelper.ISCOMPLETED));
            plan.isnotify = cursor.getString(cursor
                    .getColumnIndex(MyPlanDBOpenHelper.ISNOTIFY));
            plan.notifytime = cursor.getString(cursor
                    .getColumnIndex(MyPlanDBOpenHelper.NOTIFYTIME));
            plan.plantype = cursor.getString(cursor
                    .getColumnIndex(MyPlanDBOpenHelper.PLANTYPE));
            plan.isdeleted = cursor.getString(cursor
                    .getColumnIndex(MyPlanDBOpenHelper.ISDELETED));

            listPlan.add(plan);
        }

        cursor.close();

        return listPlan;
    }

    public ArrayList<Plan> queryPlans(String planType) {
        return queryPlans(planType, null);
    }

    @SuppressWarnings("WhileLoopReplaceableByForEach")
    public ArrayList<DatePlans> getEverydayPlanData() {
        ArrayList<DatePlans> datePlansList = new ArrayList<DatePlans>();
        // date
        HashSet<String> dateSet = new HashSet<String>();
        ArrayList<Plan> list = queryPlans(PlanType.EVERYDAY_PLAN);
        for (Plan plan : list) {
            dateSet.add(plan.createtime);
        }
        //
        Iterator<String> i = dateSet.iterator();
        //noinspection WhileLoopReplaceableByForEach,WhileLoopReplaceableByForEach
        while (i.hasNext()) {
            String date = i.next();
            ArrayList<Plan> tempList = new ArrayList<Plan>();
            for (Plan plan : list) {
                if (date.equals(plan.createtime)) {
                    tempList.add(plan);
                }
            }
            datePlansList.add(new DatePlans(date, tempList));
        }

        Collections.sort(datePlansList);

        return datePlansList;
    }


    public Cursor getPlanCursor(String planType, String iscompleted) {
        String where = null;
        if (!TextUtils.isEmpty(iscompleted)) {
            where = MyPlanDBOpenHelper.PLANTYPE + " =" + planType + " and "
                    + MyPlanDBOpenHelper.ISCOMPLETED + " = " + iscompleted + "  ORDER BY " + MyPlanDBOpenHelper.UPDATETIME + " DESC";
        } else {
            where = MyPlanDBOpenHelper.PLANTYPE + " =" + planType + "  ORDER BY " + MyPlanDBOpenHelper.UPDATETIME + " DESC";
        }
        Cursor cursor = mContentResolver.query(
                Uri.parse(MyPlanDBOpenHelper.CONTENT_URI), null, where, null,
                null);

        return cursor;
    }


    public void updatePlan(Plan plan) {

        ContentValues cv = new ContentValues();
        cv.put(MyPlanDBOpenHelper.CONTENT, plan.content);
        cv.put(MyPlanDBOpenHelper.CREATETIME, plan.createtime);
        cv.put(MyPlanDBOpenHelper.COMPLETETIME, plan.completetime);
        cv.put(MyPlanDBOpenHelper.UPDATETIME, plan.updatetime);
        cv.put(MyPlanDBOpenHelper.ISCOMPLETED, plan.iscompleted);
        cv.put(MyPlanDBOpenHelper.ISNOTIFY, plan.isnotify);
        cv.put(MyPlanDBOpenHelper.NOTIFYTIME, plan.notifytime);
        cv.put(MyPlanDBOpenHelper.PLANTYPE, plan.plantype);
        cv.put(MyPlanDBOpenHelper.ISDELETED, plan.isdeleted);

        String where = MyPlanDBOpenHelper.PLANID + " =" + plan.planid;
        mContentResolver.update(Uri.parse(MyPlanDBOpenHelper.CONTENT_URI), cv,
                where, null);
    }


    public Plan getPlanFromCursor(Cursor cursor) {
        Plan plan = new Plan();
        plan.planid = String.valueOf(cursor.getInt(cursor
                .getColumnIndex(MyPlanDBOpenHelper.PLANID)));
        plan.content = cursor.getString(cursor
                .getColumnIndex(MyPlanDBOpenHelper.CONTENT));
        plan.createtime = cursor.getString(cursor
                .getColumnIndex(MyPlanDBOpenHelper.CREATETIME));
        plan.completetime = cursor.getString(cursor
                .getColumnIndex(MyPlanDBOpenHelper.COMPLETETIME));
        plan.updatetime = cursor.getString(cursor
                .getColumnIndex(MyPlanDBOpenHelper.UPDATETIME));
        plan.iscompleted = cursor.getString(cursor
                .getColumnIndex(MyPlanDBOpenHelper.ISCOMPLETED));
        plan.isnotify = cursor.getString(cursor
                .getColumnIndex(MyPlanDBOpenHelper.ISNOTIFY));
        plan.notifytime = cursor.getString(cursor
                .getColumnIndex(MyPlanDBOpenHelper.NOTIFYTIME));
        plan.plantype = cursor.getString(cursor
                .getColumnIndex(MyPlanDBOpenHelper.PLANTYPE));
        plan.isdeleted = cursor.getString(cursor
                .getColumnIndex(MyPlanDBOpenHelper.ISDELETED));


        return plan;
    }

}
