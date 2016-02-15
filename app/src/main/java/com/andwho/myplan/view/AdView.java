package com.andwho.myplan.view;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andwho.myplan.R;
import com.andwho.myplan.model.Banner;
import com.andwho.myplan.utils.DensityUtils;

import java.util.List;


/***
 * @author ouyyx 广告栏
 */
public class AdView extends LinearLayout {

    private static final String TAG = AdView.class.getSimpleName();

    private Context myselfContext;

    private ImageView iv_ad;
    private FrameLayout ad_layout;
    private SlowGalleryForAd ad_gallery;
    private TextView tv_title;
    private LinearLayout ll_ad_indicator;

    public AdView(Context context) {
        this(context, null);
    }

    public AdView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.ad_view, this, true);

        myselfContext = context;

        findViews();
    }

    private void findViews() {
        iv_ad = (ImageView) findViewById(R.id.iv_ad);
        ad_layout = (FrameLayout) findViewById(R.id.ad_layout);
        ad_gallery = (SlowGalleryForAd) findViewById(R.id.ad_gallery);
        tv_title = (TextView) findViewById(R.id.tv_title);
        ll_ad_indicator = (LinearLayout) findViewById(R.id.ll_ad_indicator);
    }

    private List<Banner> adInfos;

    public void showDefaultImg() {
        iv_ad.setVisibility(View.VISIBLE);
        ad_layout.setVisibility(View.GONE);
        tv_title.setVisibility(View.GONE);
        ll_ad_indicator.setVisibility(View.GONE);
    }

    public void init(List<Banner> listInfos) {
        //
        // String[] images = {
        // "http://img2.imgtn.bdimg.com/it/u=3093785514,1341050958&fm=21&gp=0.jpg",
        // "http://img2.3lian.com/2014/f2/37/d/40.jpg",
        // "http://d.3987.com/sqmy_131219/001.jpg",
        // "http://img2.3lian.com/2014/f2/37/d/39.jpg",
        // "http://www.8kmm.com/UploadFiles/2012/8/201208140920132659.jpg",
        // "http://f.hiphotos.baidu.com/image/h%3D200/sign=1478eb74d5a20cf45990f9df460b4b0c/d058ccbf6c81800a5422e5fdb43533fa838b4779.jpg",
        // "http://f.hiphotos.baidu.com/image/pic/item/09fa513d269759ee50f1971ab6fb43166c22dfba.jpg"
        // };
        //
        // List<String> listInfos = new ArrayList<String>();
        // listInfos.add(images[0]);
        // listInfos.add(images[1]);
        // listInfos.add(images[2]);
        // listInfos.add(images[3]);
        // listInfos.add(images[4]);
        // listInfos.add(images[5]);

        adInfos = listInfos;
        // adInfos.addAll(listInfos);

        if (adInfos != null && adInfos.size() > 0) {
            // 展示的广告
            iv_ad.setVisibility(View.GONE);
            ad_layout.setVisibility(View.VISIBLE);
            // 页码显示
            int showSize = adInfos.size();
            if (showSize > 1) {
                ll_ad_indicator.setVisibility(View.VISIBLE);
                mMaxFlipAd = showSize;
                createPageIndicatorAd();
            } else {
                ll_ad_indicator.removeAllViews();
                ll_ad_indicator.setVisibility(View.INVISIBLE);
            }

            tv_title.setVisibility(View.VISIBLE);
            updateTitle(0);

            ad_gallery.setAdapter(new AdAdapter(myselfContext, adInfos));
            ad_gallery.setOnItemClickListener(onItemClickListener);
            ad_gallery.setOnItemSelectedListener(onAdItemSelectedListener);
            ad_gallery.setOnGalleryTouchListener(OnTouchListener);
            this.startAdAutoSwitch();
        } else {
            showDefaultImg();
        }

    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                long arg3) {
            // TODO Auto-generated method stub
            Banner detail = adInfos.get(position);
            if (detail != null && !TextUtils.isEmpty(detail.detailURL)) {
                // IntentHelper.showCommonWebViewAct(myselfContext, detail);
            }

        }
    };

    private AdapterView.OnItemSelectedListener onAdItemSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            // TODO Auto-generated method stub
            mAdSelectedPosition = arg2;
            updateTitle(mAdSelectedPosition);
            updatePageIndicatorAd();
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

    };

    private void updateTitle(int position) {
        tv_title.setText(adInfos.get(position).title);
    }

    private int mMaxFlipAd = 0;

    private void createPageIndicatorAd() {
        ll_ad_indicator.removeAllViews();
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.leftMargin = DensityUtils.dip2px(10);
        llp.rightMargin = DensityUtils.dip2px(10);
        for (int i = 0; i < this.mMaxFlipAd; i++) {
            ImageView img = new ImageView(myselfContext);
            img.setLayoutParams(llp);
            ll_ad_indicator.addView(img, llp);
        }

        updatePageIndicatorAd();
    }

    private int mAdSelectedPosition = 0;

    private void updatePageIndicatorAd() {

        if (mAdSelectedPosition < 0 || mAdSelectedPosition > mMaxFlipAd - 1) {
            ll_ad_indicator.setVisibility(View.INVISIBLE);
            return;
        }
        ll_ad_indicator.setVisibility(View.VISIBLE);

        for (int i = 0; i < ll_ad_indicator.getChildCount(); i++) {
            ((ImageView) ll_ad_indicator.getChildAt(i))
                    .setBackgroundResource(R.drawable.indicator2);
        }

        if (ll_ad_indicator.getChildCount() > 0) {
            ((ImageView) ll_ad_indicator.getChildAt(mAdSelectedPosition))
                    .setBackgroundResource(R.drawable.indicator_selected2);
        }

    }

    public class AdAdapter extends BaseAdapter {

        private Context mContext;
        private List<Banner> infoList = null;
        private Gallery.LayoutParams glLayoutParams = null;

        public AdAdapter(Context context, List<Banner> list) {
            list.toArray();
            this.mContext = context;
            this.infoList = list;
            glLayoutParams = new Gallery.LayoutParams(
                    android.widget.Gallery.LayoutParams.FILL_PARENT,
                    DensityUtils.dip2px(190));
        }

        @Override
        public int getCount() {
            // TODO Auto-generated
            // method stub
            return infoList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated
            // method stub
            return infoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated
            // method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            RemoteImageView imageView = (RemoteImageView) convertView;
            if (imageView == null) {
                imageView = new RemoteImageView(mContext);
            }

            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(glLayoutParams);

            imageView.setDefaultImage(R.drawable.def_activity_bar);

            Banner detail = (Banner) infoList.get(position);
            if (detail != null && !TextUtils.isEmpty(detail.imgURL)) {
                // Log.d(TAG, "@@...sp...getView..url : " + detail.imgURL);
                imageView.setImageUrl(detail.imgURL);
            } else {
                imageView.setImageResource(R.drawable.def_activity_bar);
            }

            return imageView;
        }
    }

    private static final int MSG_GALLERY_AUTO_SWITCH = 2000;
    private static final int AUTO_SWITCH_INTERVAL = 6000;

    Handler galleryHandler = new Handler() {
        @SuppressWarnings("deprecation")
        @Override
        public void handleMessage(Message msg) {
            int count = ad_gallery.getCount();
            if (count <= 1) {
                return;
            }
            int selectedPosition = ad_gallery.getSelectedItemPosition();
            if (selectedPosition >= count - 1) {
                selectedPosition = 0;
                ad_gallery.setSelection(selectedPosition);
            } else {
                selectedPosition++;
            }

            updateTitle(selectedPosition);

            // 自动向右滚动滚动
            ad_gallery.onScroll(null, null, 100, 0);
            ad_gallery.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);

            startAdAutoSwitch();
        }
    };

    public void startAdAutoSwitch() {
        if (adInfos != null && adInfos.size() > 0) {

            galleryHandler.removeMessages(MSG_GALLERY_AUTO_SWITCH);
            galleryHandler.sendEmptyMessageDelayed(MSG_GALLERY_AUTO_SWITCH,
                    AUTO_SWITCH_INTERVAL);
        }
    }

    public void stopAdAutoSwitch() {
        if (adInfos != null && adInfos.size() > 0) {
            galleryHandler.removeMessages(MSG_GALLERY_AUTO_SWITCH);
        }
    }

    public static enum AdvertsType {

        TYPE1(1, "超级笔记广告"), TYPE2(2, "工作台广告");

        public int index;
        public String desc;

        private AdvertsType(int index, String desc) {
            this.index = index;
            this.desc = desc;
        }
    }

    private boolean isInMove = false;
    private SlowGalleryForAd.OnGalleryTouchListener OnTouchListener = new SlowGalleryForAd.OnGalleryTouchListener() {

        @Override
        public void onTouchInGallery(boolean isIn) {
            // TODO Auto-generated method stub
            // Log.d(TAG, "@@...scs...isIn = " + isIn);
            // 有时候onTouchUp捕获不到，所以在这里做了一个补充 (防止只捕获了onTouchDownOrMove，造成不能滑动主页)
            // 如果最后触及的点在gallery之外则重置
            if (!isIn) {
                startAdAutoSwitch();
                sendBroadcastCanSroll(true);
                isInMove = false;
            }
        }

        @Override
        public void onTouchDownOrMove() {
            // TODO Auto-generated method stub
            // isInMove 防止重复调用
            if (!isInMove) {
                // 这个时候停止自动切换图片，并且禁止主页的可滑动
                // Log.d(TAG, "@@...scs...onTouchDown");
                stopAdAutoSwitch();
                sendBroadcastCanSroll(false);
                isInMove = true;
            }

        }

        @Override
        public void onTouchUp() {
            // TODO Auto-generated method stub
            // 这个时候恢复自动切换图片，并且恢复主页的可滑动
            // Log.d(TAG, "@@...scs...onTouchUp");
            startAdAutoSwitch();
            sendBroadcastCanSroll(true);
            isInMove = false;
        }

        private void sendBroadcastCanSroll(boolean can) {
//            Intent intent = new Intent(IntentKeys.INTENT_KEY_ONTOUCH_IN_GALLERY);
//            intent.putExtra("CanScroll", can);
//            myselfContext.sendBroadcast(intent);
        }

    };
}