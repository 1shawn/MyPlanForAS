package com.andwho.myplan.model;

import java.io.Serializable;

/**
 * Created by zhouf on 16/4/17.
 * user+userSettings组合
 * 为了本地保存信息方便
 */
public class UserInfo implements Serializable {
    public String userName;
    public String tempPicUrl;
    public String headPicUrl;

    public String nickName;
    public String birthday;
    public String userObjectId;//userSettingId
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
    public   String objectId;//userId

}
