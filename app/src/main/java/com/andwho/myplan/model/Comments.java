package com.andwho.myplan.model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by ys_1shawn on 2016/2/21.
 */
public class Comments extends BmobObject implements Serializable {
    public String content;//
    public Posts posts;
    public UserSettingss author;
    public UserSettingss likes;
    public String imgURL;//
    public int likesCount;
    public String isDeleted;//
    public UserSettingss replyAuthor;

}