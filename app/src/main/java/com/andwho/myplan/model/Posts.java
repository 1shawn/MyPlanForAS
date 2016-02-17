package com.andwho.myplan.model;

import java.io.Serializable;
import java.util.ArrayList;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by ys_1shawn on 2016/2/16.
 */
public class Posts extends BmobObject implements Serializable {
    public String content;//
    public ArrayList<String> imgURLArray;//
    public String isDeleted;//
    public String isTop;
    public String isHighlight;

    public UserSettingss author;
    public UserSettingss likes;

    public int readTimes;
    public int likesCount;
    public int commentsCount;

    public BmobDate updatedTime;

}
