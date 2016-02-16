package com.andwho.myplan.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.andwho.myplan.R;
import com.andwho.myplan.model.Banner;
import com.andwho.myplan.model.Posts;
import com.andwho.myplan.utils.Log;
import com.andwho.myplan.view.AdView;
import com.andwho.myplan.view.RemoteImageView;
import com.andwho.myplan.view.myexpandablelistview.PullToRefreshBase;
import com.andwho.myplan.view.myexpandablelistview.PullToRefreshBase.Mode;
import com.andwho.myplan.view.myexpandablelistview.PullToRefreshBase.OnRefreshListener;
import com.andwho.myplan.view.myexpandablelistview.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * @author ouyyx 小区(顶部广告，分享列表)
 */
public class CommunityFrag extends BaseFrag implements OnClickListener {

    private static final String TAG = CommunityFrag.class.getSimpleName();

    private Activity myselfContext;
    private TextView tv_title;
    private ImageView iv_rightIcon;

    private TextView tv_nocontent;

    private AdView ad;
    private PullToRefreshListView listview;
    private ListAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myselfContext = this.getActivity();
        View view = findViews(inflater, container);
        setListener();
        init();

        return view;
    }

    private View findViews(LayoutInflater inflater, ViewGroup container) {

        View view = inflater.inflate(R.layout.community_frag, container,
                false);
        initHeader(view);

        ad = (AdView) view.findViewById(R.id.ad);
        tv_nocontent = (TextView) view.findViewById(R.id.tv_nocontent);
        listview = (PullToRefreshListView) view.findViewById(R.id.listview);
        return view;
    }

    private void setListener() {

        listview.setMode(Mode.BOTH);
        listview.setOnRefreshListener(mOnRefreshListener);
    }

    private void initHeader(View view) {
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText("小区");
        iv_rightIcon = (ImageView) view.findViewById(R.id.iv_rightIcon);
        iv_rightIcon.setVisibility(View.VISIBLE);
        iv_rightIcon.setImageResource(R.drawable.icon_add);
        iv_rightIcon.setOnClickListener(this);
    }

    private OnRefreshListener<ListView> mOnRefreshListener = new OnRefreshListener<ListView>() {

        @Override
        public void onRefresh(PullToRefreshBase<ListView> refreshView) {
            // TODO Auto-generated method sub
            listview.onRefreshComplete();
        }

    };

    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            // TODO Auto-generated method stub
//            final Plan plan = listAdapter.getItem(arg2 - 1);
//            IntentHelper.showPlanEdit(myselfContext, "0", plan);
        }

    };

    private void init() {
        addHeader();

        initPostList();
    }

    private void initPostList() {

        Log.e(TAG, "@@...smpp...initPostList " );

        BmobQuery<Posts> query = new BmobQuery<Posts>();
        query.findObjects(myselfContext, new FindListener<Posts>() {
            @Override
            public void onSuccess(final List<Posts> list) {
                // TODO Auto-generated method stub

                Log.e(TAG, "@@...smpp...Posts size = " + list);

                for (Posts banner : list) {
                    Log.e(TAG, "@@...smpp..-----------------> ");
                    Log.e(TAG, "@@...smpp...  content = "
                            + banner.content);
                    Log.e(TAG, "@@...smpp...  nickName = "
                            + banner.author.nickName);
                    Log.e(TAG, "@@...smpp...  likesCount = "
                            + banner.likesCount);


                }

                if (list != null && list.size() > 0) {
                    tv_nocontent.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);

                    listAdapter = new ListAdapter(myselfContext, list);
                    listview.setAdapter(listAdapter);
                    listview.setOnItemClickListener(mOnItemClickListener);
                } else {
                    listview.setVisibility(View.GONE);
                    tv_nocontent.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onError(int arg0, String arg1) {
                // TODO Auto-generated method stub

                Log.e(TAG, "@@...smpp...报错了onError = " + arg1);

            }
        });

    }


    private void addHeader() {
//        LayoutInflater vi = (LayoutInflater) myselfContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        ad = (AdView) vi.inflate(R.layout.ad_header, null, false);
        ad.showDefaultImg();

        BmobQuery<Banner> query = new BmobQuery<Banner>();
        query.findObjects(myselfContext, new FindListener<Banner>() {
            @Override
            public void onSuccess(final List<Banner> listBanner) {
                // TODO Auto-generated method stub

                Log.e(TAG, "@@...smpp...CheckUpdate size = " + listBanner);

                for (Banner banner : listBanner) {
                    Log.e(TAG, "@@...smpp..-----------------> ");
                    Log.e(TAG, "@@...smpp...banner title = "
                            + banner.title);
                    Log.e(TAG, "@@...smpp...banner imgURL = "
                            + banner.imgURL);
                    Log.e(TAG, "@@...smpp...banner  detailURL = "
                            + banner.detailURL);
                    Log.e(TAG, "@@...smpp...banner  isDeleted = "
                            + banner.isDeleted);

                }
                myselfContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ad.init(listBanner);
                    }
                });

                //listview.addHeaderView(ad , null, false);
            }

            @Override
            public void onError(int arg0, String arg1) {
                // TODO Auto-generated method stub

            }
        });


    }

    private class ListAdapter extends BaseAdapter {

        public List<Posts> data;

        private Activity mActivity;
        private LayoutInflater inflater;
        private String activityType;

        public ListAdapter(Activity mActivity, List<Posts> data) {
            this.mActivity = mActivity;
            this.data = data;
            inflater = mActivity.getLayoutInflater();
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Posts getItem(int arg0) {
            return data.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = (LinearLayout) inflater.inflate(
                        R.layout.post_list_item, null);

                holder.iv_headicon = (RemoteImageView) convertView
                        .findViewById(R.id.iv_headicon);
                holder.iv_post_img1 = (RemoteImageView) convertView
                        .findViewById(R.id.iv_post_img1);
                holder.iv_post_img2 = (RemoteImageView) convertView
                        .findViewById(R.id.iv_post_img2);

                holder.tv_name = (TextView) convertView
                        .findViewById(R.id.tv_name);
                holder.tv_time = (TextView) convertView
                        .findViewById(R.id.tv_time);
                holder.tv_content = (TextView) convertView
                        .findViewById(R.id.tv_content);

                holder.ll_read = (LinearLayout) convertView
                        .findViewById(R.id.ll_read);
                holder.ll_comments = (LinearLayout) convertView
                        .findViewById(R.id.ll_comments);
                holder.ll_likes = (LinearLayout) convertView
                        .findViewById(R.id.ll_likes);

                holder.iv_read = (ImageView) convertView
                        .findViewById(R.id.iv_read);
                holder.iv_comments = (ImageView) convertView
                        .findViewById(R.id.iv_comments);
                holder.iv_likes = (ImageView) convertView
                        .findViewById(R.id.iv_likes);

                holder.tv_read_times = (TextView) convertView
                        .findViewById(R.id.tv_read_times);
                holder.tv_comments_count = (TextView) convertView
                        .findViewById(R.id.tv_comments_count);
                holder.tv_likes_count = (TextView) convertView
                        .findViewById(R.id.tv_likes_count);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Posts post = data.get(position);
            holder.iv_headicon.setDefaultImage(R.drawable.default_headicon);
            if (post.author != null) {
                holder.iv_headicon.setImageUrl(post.author.avatarURL);
                holder.tv_name.setText(post.author.nickName);
            }

            //holder.tv_time.setText(post.createdAt);
            holder.tv_content.setText(post.content);

            holder.iv_post_img1.setVisibility(View.GONE);
            holder.iv_post_img2.setVisibility(View.GONE);
            holder.iv_post_img1.setDefaultImage(R.drawable.def_activity_bar);
            holder.iv_post_img1.setDefaultImage(R.drawable.def_activity_bar);
            ArrayList<String> imgs = post.imgURLArray;
            if (imgs != null && imgs.size() > 0) {
                if (imgs.size() == 1) {
                    holder.iv_post_img1.setVisibility(View.VISIBLE);
                    holder.iv_post_img1.setImageUrl(imgs.get(0));
                } else if (imgs.size() == 2) {
                    holder.iv_post_img1.setVisibility(View.VISIBLE);
                    holder.iv_post_img2.setVisibility(View.VISIBLE);
                    holder.iv_post_img1.setImageUrl(imgs.get(0));
                    holder.iv_post_img2.setImageUrl(imgs.get(1));
                }
            }

            holder.tv_read_times.setText(String.valueOf(post.readTimes));
            holder.tv_comments_count.setText(String.valueOf(post.commentsCount));
            holder.tv_likes_count.setText(String.valueOf(post.likesCount));

            return convertView;
        }

        class ViewHolder {
            RemoteImageView iv_headicon, iv_post_img1, iv_post_img2;
            TextView tv_name, tv_time, tv_content;

            LinearLayout ll_read, ll_comments, ll_likes;
            ImageView iv_read, iv_comments, iv_likes;
            TextView tv_read_times, tv_comments_count, tv_likes_count;
        }

    }


    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        int id = view.getId();
        switch (id) {
            case R.id.iv_rightIcon:
//                IntentHelper.showPlanEdit(myselfContext, curPlanType);
                break;
            default:
                break;
        }
    }


    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // StatService.onResume(this);
        ad.startAdAutoSwitch();
    }

    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // StatService.onPause(this);
        ad.stopAdAutoSwitch();
    }
}