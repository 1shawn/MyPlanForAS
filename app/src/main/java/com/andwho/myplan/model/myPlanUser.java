package com.andwho.myplan.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by zhouf on 2016/2/14.
 */
public class MyPlanUser extends  BmobObject {
    private String userObjectId;
    private String nickName;
    private String birthday;
    private String gender;
    private String liespan;
    private String avatarURL;
    private String centerTopURL;
    private String isAutoSync;
    private String createdTime;
    private String updatedTime;
    private String level;
//    private String createdAt;
//    private String updatedAt;
//    private String ACL;

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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLiespan() {
        return liespan;
    }

    public void setLiespan(String liespan) {
        this.liespan = liespan;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public String getCenterTopURL() {
        return centerTopURL;
    }

    public void setCenterTopURL(String centerTopURL) {
        this.centerTopURL = centerTopURL;
    }

    public String getIsAutoSync() {
        return isAutoSync;
    }

    public void setIsAutoSync(String isAutoSync) {
        this.isAutoSync = isAutoSync;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
/*
    @Override
    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

   @Override
    public String getACL() {
        return ACL;
    }

    public void setACL(String ACL) {
        this.ACL = ACL;
    }*/
}
