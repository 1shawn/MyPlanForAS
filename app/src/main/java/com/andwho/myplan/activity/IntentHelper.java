package com.andwho.myplan.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.andwho.myplan.R;
import com.andwho.myplan.model.Banner;
import com.andwho.myplan.model.Plan;
import com.andwho.myplan.model.Posts;
import com.andwho.myplan.onekeyshare.OnekeyShare;
import com.andwho.myplan.view.slideLayout.IntentUtils;

import cn.sharesdk.framework.ShareSDK;

public class IntentHelper {

    public static final void showPlanEdit(Activity act, String planType) {
        showPlanEdit(act, planType, null);
    }

    public static final void showPlanEdit(Activity act, String planType,
                                          Plan plan) {
        Intent intent = new Intent(act, EditPlanAct.class);
        intent.putExtra("planType", planType);
        intent.putExtra("plan", plan);
        IntentUtils.getInstance().startActivity(act, intent);
    }

    public static final void showCommonWebView(Context ctx,
                                               Banner banner) {
        Intent intent = new Intent(ctx, CommonWebViewAct.class);
        intent.putExtra("Banner", banner);
        ctx.startActivity(intent);
    }


    public static final void showPersonalSetting(Activity act) {
        Intent intent = new Intent(act, PersonalSettingAct.class);
        IntentUtils.getInstance().startActivity(act, intent);
    }

    public static final void showModifyInfo(Activity act, String type) {
        Intent intent = new Intent(act, ModifyInfoAct.class);
        intent.putExtra("type", type);
        IntentUtils.getInstance().startActivity(act, intent);
    }

    public static final void showMore(Activity act) {
        Intent intent = new Intent(act, MoreAct.class);
        IntentUtils.getInstance().startActivity(act, intent);
    }

    public static final void showProblems(Activity act) {
        Intent intent = new Intent(act, ProblemsAct.class);
        act.startActivity(intent);
    }

    public static final void showEditPost(Activity act) {
        Intent intent = new Intent(act, EditPostAct.class);
        IntentUtils.getInstance().startActivity(act, intent);
    }

    public static final void showCommunityDetail(Activity act, Posts post) {
        Intent intent = new Intent(act, CommunityDetailAct.class);
        intent.putExtra("Posts", post);
        IntentUtils.getInstance().startActivity(act, intent);
    }

    public static final void showImageGallery(Activity act, Posts post) {
        Intent intent = new Intent(act, ImageGalleryAct.class);
        intent.putExtra("Posts", post);
        act.startActivity(intent);
    }

    public static final void showLogin(Activity act) {
        Intent intent = new Intent(act, LoginAct.class);
        act.startActivity(intent);
    }

    public static final void showMain(Activity act) {
        Intent intent = new Intent(act, IndexAct.class);
        act.startActivity(intent);
    }

    public static final void showAboutUs(Activity act) {
        Intent intent = new Intent(act, AboutUsAct.class);
        IntentUtils.getInstance().startActivity(act, intent);
    }

    public static final void showSign(Activity act) {
        Intent intent = new Intent(act, SignAct.class);
        act.startActivity(intent);
    }

    public static final void showFindPsw(Activity act) {
        Intent intent = new Intent(act, FindPswAct.class);
        act.startActivity(intent);
    }

    public static final void share(Activity act) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
        intent.putExtra(Intent.EXTRA_TEXT, "我有计划分享");

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        act.startActivity(Intent.createChooser(intent, "我有计划"));
    }
    public static final void showShare(Context context) {
        ShareSDK.initSDK(context);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(context.getResources().getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我有计划");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite(context.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(context);
    }

    public static final void showMsgCenter(Activity act) {
        Intent intent = new Intent(act, MsgCenterAct.class);
        IntentUtils.getInstance().startActivity(act, intent);
    }

    public static final void showMsgDetail(Activity act, String title,String content,String time,String objId) {
        Intent intent = new Intent(act, MsgDetailAct.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.putExtra("msgTime", time);
        intent.putExtra("objId", objId);
        IntentUtils.getInstance().startActivity(act, intent);
    }
}
