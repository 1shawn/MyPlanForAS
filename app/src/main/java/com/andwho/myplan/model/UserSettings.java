package com.andwho.myplan.model;

import cn.bmob.v3.BmobUser;

/**
 * Created by ys_1shawn on 2016/2/16.
 */
public class UserSettings extends BmobUser {

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

    public String getUserObjectId() {
        return userObjectId;
    }

    public void setUserObjectId(String userObjectId) {
        this.userObjectId = userObjectId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
