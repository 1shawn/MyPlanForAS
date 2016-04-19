package com.andwho.myplan.model;

<<<<<<< .merge_file_a09936
/**
 * Created by zhouf on 2016/4/18.
 */
public class UserInfo {
    public String userName;
    public String userId;
=======
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

>>>>>>> .merge_file_a09968
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
<<<<<<< .merge_file_a09936
=======
    public   String objectId;

>>>>>>> .merge_file_a09968
}
