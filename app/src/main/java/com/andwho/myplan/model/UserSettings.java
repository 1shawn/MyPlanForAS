package com.andwho.myplan.model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by ys_1shawn on 2016/2/16.
 */
public class UserSettings  extends BmobObject implements Serializable{

    public String userObjectId;
    public String nickName;
    public String birthday;
    public String gender;
    public String liespan;
    public String avatarURL;
    public String centerTopURL;
    public String isAutoSync;
    public String createdTime;
    public String updatedTime;
    public String level;
}
