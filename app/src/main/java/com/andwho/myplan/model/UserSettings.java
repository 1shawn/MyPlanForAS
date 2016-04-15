package com.andwho.myplan.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by ys_1shawn on 2016/2/16.
 */
public class UserSettings extends BmobObject {//只能继承BmobObject，否则无法查询数据

    public String nickName;
    public String birthday;
    public String userObjectId;
    public String gender;
    public String lifespan;
    public String liespan;
    public String avatarURL;
    public String centerTopURL;
    public String isAutoSync;
    public String createdTime;
    public String updatedTime;
    public String syncTime;//
    public String level;
}
