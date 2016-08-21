package com.andwho.myplan.model;

import java.io.Serializable;
import java.util.ArrayList;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by zhouf on 2016/5/10.
 */
public class Messages  extends BmobObject implements Serializable {
    public String title;
    public String content;
    public String detailURL;
    public ArrayList<String> imgURLArray;
    public BmobRelation hasRead;
    public int readTimes;
    public String isDeleted;
    public String canShare;

}
