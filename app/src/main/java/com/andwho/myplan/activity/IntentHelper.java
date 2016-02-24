package com.andwho.myplan.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.andwho.myplan.view.slideLayout.IntentUtils;
import com.andwho.myplan.model.Banner;
import com.andwho.myplan.model.Plan;
import com.andwho.myplan.model.Posts;

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
        IntentUtils.getInstance().startActivity(act, intent);
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
}
