package com.andwho.myplan.utils;

import android.content.Context;

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
    public static void checkUser(Context context,String username,String password,FindListener<BmobUser> listener){
        BmobQuery<BmobUser> query = new BmobQuery<BmobUser>();
        query.addWhereEqualTo(username, password);
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
//        BmobUser currentUser = BmobUser.getCurrentUser(context); // 现在的currentUser是null了
    }
}
