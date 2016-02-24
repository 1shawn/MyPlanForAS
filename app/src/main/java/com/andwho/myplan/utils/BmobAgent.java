package com.andwho.myplan.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.andwho.myplan.activity.BaseAct;
import com.andwho.myplan.contentprovider.DbManger;
import com.andwho.myplan.model.UserSettings;
import com.andwho.myplan.preference.MyPlanPreference;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.ResetPasswordByEmailListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2016/1/22.
 */
public class BmobAgent {
    //检查是否已经注册
    public static void checkUser(Context context,String username,FindListener<BmobUser> listener){
        BmobQuery<BmobUser> query = new BmobQuery<BmobUser>();
        query.addWhereEqualTo("username", username);
        query.findObjects(context, listener);
    }
    //注册
    public static void signIn(Context context,String username,String password,SaveListener listener){
        BmobUser bu = new BmobUser();
        bu.setUsername(username);
        bu.setPassword(password);
        bu.setEmail(username);
        //注意：不能用save方法进行注册
        bu.signUp(context, listener);
    }

    //忘记密码
    public static void forgetPsw(Context context,String email,ResetPasswordByEmailListener listener) {
        BmobUser bu2 = new BmobUser();
        bu2.resetPasswordByEmail(context, email, listener);
    }

                //登录

    public static void loginIn(Context context,String username,String password,SaveListener listener){
        BmobUser bu2 = new BmobUser();
        bu2.setUsername(username);
        bu2.setPassword(password);
        bu2.login(context, listener);
    }
    //退出登陆
    public static void loginOut(Context context){
        BmobUser.logOut(context);   //清除缓存用户对象
        MyPlanPreference.getInstance(context).setUsername("");
//        BmobUser currentUser = BmobUser.getCurrentUser(context); // 现在的currentUser是null了
    }
    public static void uploadPicFile(Context context,String picPath, UploadListener listener){
       BmobProFile.getInstance(context).upload(picPath, listener);
       /* BmobFile bmobFile = new BmobFile(new File(picPath));
        bmobFile.uploadblock(context,listener);*/
    }
    //是否在UserSetting先形成了
    public static void checkUserSettingInfo(Context context,String userObjectId,FindListener<UserSettings> listener){
        BmobQuery<UserSettings> query = new BmobQuery<UserSettings>();
        //查询
        query.addWhereEqualTo("userObjectId", userObjectId);
        //执行查询方法
        query.findObjects(context, listener);
    }

    public static void updateUserInfo(Context context, UserSettings userInfo,UpdateListener listener){

        userInfo.update(context, listener);
    }

    public static void saveUserInfo(Context context, UserSettings userInfo,SaveListener listener){
        userInfo.save(context, listener);
    }
    //用户设置信息
    public static void updateAllDate(final Context myselfContext,final UserSettings userInfo){
        updatePlanDate(myselfContext);
        String picUrl=MyPlanPreference.getInstance(myselfContext)
                .getTempPicUrl();
        final String userId = MyPlanPreference.getInstance(myselfContext).getUserId();

        if(!TextUtils.isEmpty(picUrl)) {
            // 拍照
            Uri uri = Uri.parse(picUrl);

            //下面方法将获取的uri转为String类型哦！
            String[] imgs1 = {MediaStore.Images.Media.DATA};//将图片URI转换成存储路径
            Cursor cursor = ((BaseAct) myselfContext).managedQuery(uri, imgs1, null, null, null);
            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String img_url = cursor.getString(index);

            MyPlanPreference.getInstance(myselfContext).setHeadPicUrl(
                    uri.toString());

            BmobAgent.uploadPicFile(myselfContext, img_url, new UploadListener() {
                @Override
                public void onSuccess(String fileName, String url, final BmobFile file) {
                    userInfo.avatarURL = file.getUrl();
                }

                @Override
                public void onProgress(int progress) {
                    // TODO Auto-generated method stub
//                        ToastUtil.showLongToast(myselfContext, "进度：" + progress);
                }

                @Override
                public void onError(int statuscode, String errormsg) {
                    // TODO Auto-generated method stub
                    ToastUtil.showLongToast(myselfContext, "文件上传失败：" + errormsg);
                }
            });

        }
        updateMineAndPlanData(myselfContext, userId, userInfo);
//        updatePlanDate(myselfContext);

    }
    private static void updateMineAndPlanData(final Context myselfContext,final String userId,final UserSettings userInfo){
        if(!TextUtils.isEmpty(userId)) {
            BmobAgent.checkUserSettingInfo(myselfContext, userId, new FindListener<UserSettings>() {
                @Override
                public void onSuccess(List<UserSettings> object) {
                    // TODO Auto-generated method stub
                    if (object != null && object.size() > 0) {
                        for (UserSettings userInfodate : object) {
//                        userInfodate.avatarURL = file.getUrl();
                            BmobAgent.updateUserInfo(myselfContext, userInfodate, new UpdateListener() {
                                @Override
                                public void onSuccess() {
//                                    ToastUtil.showLongToast(myselfContext, "更新成功");
                                }

                                @Override
                                public void onFailure(int i, String s) {
//                                    ToastUtil.showLongToast(myselfContext, "更新失败：" + s);
                                }
                            });
                            updatePlanDate(myselfContext);
                        }
                    } else {
                        userInfo.userObjectId = userId;
                        userInfo.createdTime = DateUtil.getCurDateYYYYMMDD();
                        userInfo.updatedTime = userInfo.createdTime;
                        BmobAgent.saveUserInfo(myselfContext, userInfo, new SaveListener() {
                            @Override
                            public void onSuccess() {
//                                ToastUtil.showLongToast(myselfContext, "更新成功");
                            }

                            @Override
                            public void onFailure(int i, String s) {
//                                ToastUtil.showLongToast(myselfContext, "更新失败：" + s);
                            }
                        });
                        updatePlanDate(myselfContext);

                    }
                }

                @Override
                public void onError(int code, String msg) {
                    // TODO Auto-generated method stub
                    ToastUtil.showLongToast(myselfContext, "查询失败：" + msg);
                }
            });
        }
    }

    //用户的相关计划信息
    private static void updatePlanDate(final Context context){
        try{
        ArrayList<BmobObject> listPlan = DbManger.getInstance(context)
                .queryPlans();
            new BmobObject().insertBatch(context, listPlan, new SaveListener() {
                @Override
                public void onSuccess() {
                    // TODO Auto-generated method stub
//                    toast("批量添加成功");
                    ToastUtil.showLongToast(context, "更新成功");
                }
                @Override
                public void onFailure(int code, String msg) {
                    // TODO Auto-generated method stub
//                    toast("批量添加失败:"+msg);
                }
            });
        }catch (Exception ex){
            String s=ex.getMessage();
        }
    }



}
