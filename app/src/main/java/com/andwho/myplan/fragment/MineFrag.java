package com.andwho.myplan.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.andwho.myplan.R;
import com.andwho.myplan.activity.IntentHelper;
import com.andwho.myplan.constants.CompleteStatus;
import com.andwho.myplan.constants.PlanType;
import com.andwho.myplan.contentprovider.DbManger;
import com.andwho.myplan.model.Plan;
import com.andwho.myplan.preference.MyPlanPreference;
import com.andwho.myplan.utils.DateUtil;
import com.andwho.myplan.utils.MyPlanUtil;
import com.andwho.myplan.utils.StringUtil;
import com.andwho.myplan.view.RoundedImageView;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * @author ouyyx 我的
 */
public class MineFrag extends BaseFrag implements OnClickListener {

    private static final String TAG = MineFrag.class.getSimpleName();

    private Activity myselfContext;

    private TextView tv_title;

    private ImageView iv_ss;

    private ScrollView ll_root;
    private RoundedImageView iv_headicon;
    private TextView tv_name, tv_if, tv_left1, tv_left2;

    private TextView tv_total_everyday, tv_finish_everyday, tv_rate_everyday;
    private TextView tv_total_longterm, tv_finish_longterm, tv_rate_longterm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myselfContext = this.getActivity();

        if (null == fragmentView) {
            fragmentView = findViews(inflater, container);
            setListener();
            init();
        }

        return fragmentView;
    }

    private View fragmentView;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != fragmentView) {
            ((ViewGroup) fragmentView.getParent()).removeView(fragmentView);
        }
    }

    private View findViews(LayoutInflater inflater, ViewGroup container) {

        View view = inflater.inflate(R.layout.mine_frag, container, false);
        initHeader(view);
        iv_ss = (ImageView) view.findViewById(R.id.iv_ss);
        ll_root = (ScrollView) view.findViewById(R.id.ll_root);
        // top
        iv_headicon = (RoundedImageView) view.findViewById(R.id.iv_headicon);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_if = (TextView) view.findViewById(R.id.tv_if);
        tv_left1 = (TextView) view.findViewById(R.id.tv_left1);
        tv_left2 = (TextView) view.findViewById(R.id.tv_left2);
        // everyday
        tv_total_everyday = (TextView) view
                .findViewById(R.id.tv_total_everyday);
        tv_finish_everyday = (TextView) view
                .findViewById(R.id.tv_finish_everyday);
        tv_rate_everyday = (TextView) view.findViewById(R.id.tv_rate_everyday);
        // longterm
        tv_total_longterm = (TextView) view
                .findViewById(R.id.tv_total_longterm);
        tv_finish_longterm = (TextView) view
                .findViewById(R.id.tv_finish_longterm);
        tv_rate_longterm = (TextView) view.findViewById(R.id.tv_rate_longterm);

        return view;
    }

    private void initHeader(View view) {
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText("我的");
    }

    private void setListener() {
        iv_headicon.setOnClickListener(this);
    }

    private void init() {

    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        initContent();
    }

    private void initContent() {
        new LoadImageAsyncTask().execute();

       /* String nickname = MyPlanPreference.getInstance(myselfContext)
                .getNickname();
        tv_name.setText(nickname);*/
        String userName= MyPlanPreference.getInstance(myselfContext).getUsername();
        if(!TextUtils.isEmpty(userName)){
            // 允许用户使用应用
            String nickname=MyPlanPreference.getInstance(myselfContext).getNickname();
            if(!TextUtils.isEmpty(nickname)){
                tv_name.setText(nickname);
            }else{
                tv_name.setText(StringUtil.starStrFormatChange(userName));
            }

        }

        String lifespan = MyPlanPreference.getInstance(myselfContext)
                .getLifeSpan();
        tv_if.setText(Html.fromHtml(getString(R.string.mine_if_text, lifespan)));
        tv_left1.setText(Html.fromHtml(getString(R.string.mine_left1_text,
                DateUtil.getLeftDays(myselfContext))));
        //
        ArrayList<Plan> everydayPlans = DbManger.getInstance(myselfContext)
                .queryPlans(PlanType.EVERYDAY_PLAN);
        int everydayPlansSize = everydayPlans.size();
        tv_total_everyday.setText(String.valueOf(everydayPlansSize));
        ArrayList<Plan> longtermPlans = DbManger.getInstance(myselfContext)
                .queryPlans(PlanType.LONGTERM_PLAN);
        int longtermPlansSize = longtermPlans.size();
        tv_total_longterm.setText(String.valueOf(longtermPlansSize));
        //
        ArrayList<Plan> everydayFinishPlans = DbManger.getInstance(
                myselfContext).queryPlans(PlanType.EVERYDAY_PLAN, CompleteStatus.IS_COMPLETED);
        int everydayPlansFinishSize = everydayFinishPlans.size();
        tv_finish_everyday.setText(String.valueOf(everydayPlansFinishSize));
        //
        ArrayList<Plan> longtermFinishPlans = DbManger.getInstance(
                myselfContext).queryPlans(PlanType.LONGTERM_PLAN, CompleteStatus.IS_COMPLETED);
        int longtermPlansFinishSize = longtermFinishPlans.size();
        tv_finish_longterm.setText(String.valueOf(longtermPlansFinishSize));
        //
        // Log.e(TAG, "@@...myplan...everydayPlansFinishSize = " +
        // everydayPlansFinishSize);
        // Log.e(TAG, "@@...myplan...everydayPlansSize = " + everydayPlansSize);
        // Log.e(TAG, "@@...myplan...longtermPlansFinishSize = "
        // + longtermPlansFinishSize);
        // Log.e(TAG, "@@...myplan...longtermPlansSize = " + longtermPlansSize);
        if (everydayPlansFinishSize == 0 || everydayPlansSize == 0) {
            tv_rate_everyday.setText("0%");
        } else {
            tv_rate_everyday.setText(MyPlanUtil.getFinishRate(
                    String.valueOf(everydayPlansFinishSize),
                    String.valueOf(everydayPlansSize))
                    + "%");
        }

        if (longtermPlansFinishSize == 0 || longtermPlansSize == 0) {
            tv_rate_longterm.setText("0%");
        } else {
            tv_rate_longterm.setText(MyPlanUtil.getFinishRate(
                    String.valueOf(longtermPlansFinishSize),
                    String.valueOf(longtermPlansSize))
                    + "%");
        }
    }

    private class LoadImageAsyncTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                String picUrl = MyPlanPreference.getInstance(myselfContext)
                        .getHeadPicUrl();
                if (TextUtils.isEmpty(picUrl)) {
                    return null;
                }

                Uri uri = Uri.parse(picUrl);
                ContentResolver contentProvider = myselfContext
                        .getContentResolver();
                Bitmap bmp = BitmapFactory.decodeStream(contentProvider
                        .openInputStream(uri));
                return Bitmap.createScaledBitmap(bmp, 200, 200, true);

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                return null;
            }

        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result == null) {
                iv_headicon.setImageResource(R.drawable.default_headicon);
            } else {
                iv_headicon.setImageBitmap(result);
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

    }

    ;

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        int id = view.getId();
        switch (id) {
            case R.id.iv_headicon:
                IntentHelper.showMore(myselfContext);
                break;

            default:
                break;
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        startAutoRefresh();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        stopAutoRefresh();
    }

    private static final int MSG_TODAY_LEFT_SECONDS = 1;
    private static final int AUTO_REFRESH_INTERVAL = 1000;

    @SuppressLint("HandlerLeak")
    private Handler refreshHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            tv_left2.setText(Html.fromHtml(getString(R.string.mine_left2_text,
                    DateUtil.getTodayLeftSeconds())));
            startAutoRefresh();
        }
    };

    public void startAutoRefresh() {
        refreshHandler.removeMessages(MSG_TODAY_LEFT_SECONDS);
        refreshHandler.sendEmptyMessageDelayed(MSG_TODAY_LEFT_SECONDS,
                AUTO_REFRESH_INTERVAL);
    }

    public void stopAutoRefresh() {
        refreshHandler.removeMessages(MSG_TODAY_LEFT_SECONDS);
    }
}