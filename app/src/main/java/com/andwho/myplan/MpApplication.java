package com.andwho.myplan;

import android.app.Application;
import android.content.Context;

import com.andwho.myplan.constants.ConfigParam;
import com.andwho.myplan.utils.ImageUtils;

import cn.bmob.v3.Bmob;

/**
 * Created by ys_1shawn on 2016/2/14.
 */
public class MpApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Context mContext = getApplicationContext();

        Bmob.initialize(mContext, ConfigParam.BOMB_APPLICATION_ID);
        ImageUtils.initImageLoader(mContext);
    }


}
