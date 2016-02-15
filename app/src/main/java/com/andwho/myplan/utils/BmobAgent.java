package com.andwho.myplan.utils;

import android.content.Context;

import com.andwho.myplan.preference.MyPlanPreference;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.ResetPasswordByEmailListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2016/1/22.
 */
public class BmobAgent {
    //检查是否已经注册
    public static void checkUser(Context context,String username,FindListener<BmobUser> listener){
        BmobQuery<BmobUser> query = new BmobQuery<BmobUser>();
        query.addWhereEqualTo("username", username);
        query.findObjects(context,listener);
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
    public static void uploadFile(Context context,String filePath,UploadListener listener){
        BmobProFile.getInstance(context).upload(filePath,listener);
      /*  BTPFileResponse response = BmobProFile.getInstance(context).upload(filePath, new UploadListener() {

            @Override
            public void onSuccess(String fileName,String url,BmobFile file) {
                Log.i("bmob","文件上传成功："+fileName+",可访问的文件地址："+file.getUrl());
                // TODO Auto-generated method stub
                // fileName ：文件名（带后缀），这个文件名是唯一的，开发者需要记录下该文件名，方便后续下载或者进行缩略图的处理
                // url        ：文件地址
                // file        :BmobFile文件类型，`V3.4.1版本`开始提供，用于兼容新旧文件服务。
                注：若上传的是图片，url地址并不能直接在浏览器查看（会出现404错误），需要经过`URL签名`得到真正的可访问的URL地址,当然，`V3.4.1`的版本可直接从'file.getUrl()'中获得可访问的文件地址。
            }

            @Override
            public void onProgress(int progress) {
                // TODO Auto-generated method stub
                Log.i("bmob","onProgress :"+progress);
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                // TODO Auto-generated method stub
                Log.i("bmob","文件上传失败："+errormsg);
            }
        });*/
    }
}
