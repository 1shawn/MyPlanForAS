package com.andwho.myplan.model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by ys_1shawn on 2016/2/21.
 */
public class Comments extends BmobObject implements Serializable {
    public String content;//
//    public Posts posts;//
    public BmobRelation posts;
    public UserSettings author;
    public UserSettings likes;
    public String imgURL;//
    public int likesCount;
    public String isDeleted;//
    public UserSettings replyAuthor;

}