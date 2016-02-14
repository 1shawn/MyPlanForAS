package com.andwho.myplan;

import android.app.Application;

import com.andwho.myplan.constants.ConfigParam;

import cn.bmob.v3.Bmob;

/**
 * Created by ys_1shawn on 2016/2/14.
 */
public class MpApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(getApplicationContext(), ConfigParam.BOMB_APPLICATION_ID);
    }

}
