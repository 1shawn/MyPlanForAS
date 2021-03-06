package com.andwho.myplan.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.andwho.myplan.R;
import com.andwho.myplan.model.Comments;
import com.andwho.myplan.model.Plan;
import com.andwho.myplan.model.Posts;
import com.andwho.myplan.model.UserSettings;
import com.andwho.myplan.preference.MyPlanPreference;
import com.andwho.myplan.utils.DateUtil;
import com.andwho.myplan.utils.Log;
import com.andwho.myplan.utils.ToastUtil;
import com.andwho.myplan.view.ImListenerLayout;
import com.andwho.myplan.view.MyListView;
import com.andwho.myplan.view.ObservableScrollView;
import com.andwho.myplan.view.RemoteImageView;
import com.andwho.myplan.view.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by ys_1shawn on 2016/2/21.
 */
public class CommunityDetailAct extends BaseAct implements View.OnClickListener {

    private static final String TAG = CommunityDetailAct.class.getSimpleName();

    private Activity myselfContext;

    private LinearLayout ll_leftIcon;
    private TextView tv_leftIcon;
    private TextView tv_title;
    private ImageView iv_rightIcon;

    private RoundedImageView iv_headicon;
    private RemoteImageView iv_post_img1, iv_post_img2;
    private TextView tv_name, tv_time, tv_content;

    private ObservableScrollView sv;

    private LinearLayout ll_bottom;
    private LinearLayout ll_like, ll_comment;
    private TextView tv_like;
    private EditText et_comment;
    private ImageView iv_like;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_detail_act);

        myselfContext = this;

        initHeader();
        findViews();
        setListener();
        init();
    }

    private void initHeader() {
        ll_leftIcon = (LinearLayout) this.findViewById(R.id.ll_leftIcon);
        tv_leftIcon = (TextView) this.findViewById(R.id.tv_leftIcon);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        iv_rightIcon = (ImageView) this.findViewById(R.id.iv_rightIcon);

        ll_leftIcon.setOnClickListener(this);
        iv_rightIcon.setOnClickListener(this);

        tv_leftIcon.setText("小区");

        tv_title.setText("");
        ll_leftIcon.setVisibility(View.VISIBLE);
        iv_rightIcon.setVisibility(View.VISIBLE);
        iv_rightIcon.setImageResource(R.drawable.icon_more);
    }

    private void findViews() {
        ll_root = (ImListenerLayout) findViewById(R.id.ll_root);

        iv_headicon = (RoundedImageView)
                findViewById(R.id.iv_headicon);
        iv_post_img1 = (RemoteImageView)
                findViewById(R.id.iv_post_img1);
        iv_post_img2 = (RemoteImageView)
                findViewById(R.id.iv_post_img2);

        tv_name = (TextView)
                findViewById(R.id.tv_name);
        tv_time = (TextView)
                findViewById(R.id.tv_time);
        tv_content = (TextView)
                findViewById(R.id.tv_content);

        sv = (ObservableScrollView)
                findViewById(R.id.sv);

        tv_nocontent = (TextView)
                findViewById(R.id.tv_nocontent);
        listview = (MyListView)
                findViewById(R.id.listview);

        ll_bottom = (LinearLayout)
                findViewById(R.id.ll_bottom);
        ll_comment = (LinearLayout)
                findViewById(R.id.ll_comment);
        ll_like = (LinearLayout)
                findViewById(R.id.ll_like);
        tv_like = (TextView)
                findViewById(R.id.tv_like);
        et_comment = (EditText)
                findViewById(R.id.et_comment);
        iv_like=(ImageView)findViewById(R.id.iv_like);
    }

    private void setListener() {
        ll_root.setOnSoftKeyboardListener(imListener);
        ll_like.setOnClickListener(this);
        ll_comment.setOnClickListener(this);
        sv.setScrollViewListener(onScrollChangeListener);
        iv_post_img1.setOnClickListener(this);
        iv_post_img2.setOnClickListener(this);
        et_comment.setOnKeyListener(onKey);

    }
    Boolean isCommunity=true;

    View.OnKeyListener onKey=new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            // TODO Auto-generated method stub
            if(keyCode == KeyEvent.KEYCODE_ENTER&&event.getAction()==KeyEvent.ACTION_DOWN){//解决回车会有两次调用
                //这里写发送信息的方法
                String msg=et_comment.getText().toString();
                String userSettingId=MyPlanPreference.getInstance(myselfContext).getUserSettingId();
                if(TextUtils.isEmpty(msg)||TextUtils.isEmpty(userSettingId)){
                    isCommunity=true;
                    hideSoftKeyboard();
                    if(TextUtils.isEmpty(userSettingId)){
                        IntentHelper.showLogin(myselfContext);
                    }
                    return true;
                }
                showProgressDialog(null, true, false);


                UserSettings author = new UserSettings();
                author.setObjectId(userSettingId);

                /*Posts posts = new Posts();
                posts.setObjectId(post.getObjectId());*/

                Comments comments=new Comments();
                comments.author = author;
                if(!isCommunity&&v.getTag()!=null) {
                    comments.replyAuthor=((Comments)v.getTag()).author;
                }

                //将当前用户添加到Post表中的likes字段值中，表明当前用户喜欢该帖子
                BmobRelation relation = new BmobRelation();
                //将当前用户添加到多对多关联中
                relation.add(post);
                //多对多关联指向`post`的`likes`字段

                comments.posts=relation;
                comments.content=msg;
                comments.isDeleted="0";
                community(comments);
                isCommunity=true;
                hideSoftKeyboard();
                return true;
            }
            return false;
        }
    };
    private void community(final Comments comment){

        comment.save(myselfContext, new SaveListener() {
            @Override
            public void onSuccess() {
//                ToastUtil.showLongToast(myselfContext, "评论已提交！");
//                dismissProgressDialog();
                BmobRelation relation = new BmobRelation();
                //将当前用户添加到多对多关联中
                relation.add(comment);
                post.comments=relation;
                post.commentsCount++;
                post.update(myselfContext, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        ToastUtil.showLongToast(myselfContext, "评论已提交！");
                        requestListData(false);//不用刷新点赞的数据
                        dismissProgressDialog();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        ToastUtil.showLongToast(myselfContext, "评论提交失败！");
                        dismissProgressDialog();
                    }
                });

            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtil.showLongToast(myselfContext,"评论提交失败！");
                dismissProgressDialog();

            }
        });
    }
    private boolean isEditModel = false;
    private Plan plan;
    private Posts post;

    private void init() {

        post = (Posts) myselfContext.getIntent().getSerializableExtra("Posts");
        Log.e(TAG, "@@...mp...Posts likesCount = " + post.likesCount);
        Log.e(TAG, "@@...mp...Posts nickName = " + post.author.nickName);
        Log.e(TAG, "@@...mp...Posts content = " + post.content);


        iv_headicon.setDefaultImage(R.drawable.default_headicon);
        if (post.author != null) {
            iv_headicon.setImageUrl(post.author.avatarURL);
            if (!TextUtils.isEmpty(post.author.nickName)) {
//                    tv_name.setText(post.author.nickName + "   " + position);
                tv_name.setText(post.author.nickName);
            } else {
                tv_name.setText("昵称");
            }
        }
        String createDate = post.getCreatedAt().toString();
//            tv_time.setText(DateUtil.getPostFormatDate(cDate) +  "  实际: " + cDate);
        tv_time.setText(DateUtil.getPostFormatDate(createDate));
        tv_content.setText(post.content);


        iv_post_img1.setDefaultImage(R.drawable.def_activity_bar);
        iv_post_img2.setDefaultImage(R.drawable.def_activity_bar);
        ArrayList<String> imgs = post.imgURLArray;
        if (imgs != null && imgs.size() > 0) {
            if (imgs.size() == 1) {
                iv_post_img1.setVisibility(View.VISIBLE);
                iv_post_img2.setVisibility(View.GONE);
                iv_post_img1.setImageUrl(imgs.get(0));
            } else if (imgs.size() == 2) {
                iv_post_img1.setVisibility(View.VISIBLE);
                iv_post_img2.setVisibility(View.VISIBLE);
                iv_post_img1.setImageUrl(imgs.get(0));
                iv_post_img2.setImageUrl(imgs.get(1));
            } else {
                iv_post_img1.setVisibility(View.GONE);
                iv_post_img2.setVisibility(View.GONE);
            }
        } else {
            iv_post_img1.setVisibility(View.GONE);
            iv_post_img2.setVisibility(View.GONE);
        }

        tv_like.setText(String.valueOf(post.likesCount));
        requestListData(true);
    }



    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        int id = view.getId();
        switch (id) {
            case R.id.ll_leftIcon:
                hideSoftKeyboard();
                finish();
                break;
            case R.id.iv_rightIcon:
                initPopuptWindow();
                break;
            case R.id.iv_post_img1:
            case R.id.iv_post_img2:
                IntentHelper.showImageGallery(myselfContext, post);
                break;
            case R.id.ll_like:
                likePost();
                break;
            case R.id.ll_comment:
                et_comment.setHint("评论一下");
                showInputEdit(true);
                showInputMethod();
                break;

            default:
                break;
        }
    }

    private void likePost(){
        if(post==null)
            return;
        try{
            showProgressDialog(null, true, false);

            //将当前用户添加到多对多关联中
            BmobRelation relation = new BmobRelation();
            if(iv_like.getTag()==null||!(Boolean)(iv_like.getTag())) {
                post.likesCount = ++post.likesCount;
                relation.add(BmobUser.getCurrentUser(CommunityDetailAct.this));
                post.likes = relation;
            } else{
                post.likesCount = --post.likesCount;
                relation.remove(BmobUser.getCurrentUser(CommunityDetailAct.this));
                post.likes = relation;
            }
            post.update(myselfContext, new UpdateListener() {
                @Override
                public void onSuccess() {
                    dismissProgressDialog();
                    if(iv_like.getTag()==null||!(Boolean)(iv_like.getTag())) {
                        iv_like.setImageResource(R.drawable.icon_praised);
                        iv_like.setTag(true);
                    }else{
                        iv_like.setImageResource(R.drawable.icon_praise);
                        iv_like.setTag(false);
                    }
                    tv_like.setText(String.valueOf(post.likesCount));
                }

                @Override
                public void onFailure(int i, String s) {
                    ToastUtil.showLongToast(myselfContext,s);
                    dismissProgressDialog();
                }
            });
        }catch (Exception ex){

        }
    }
    private TextView tv_nocontent;
    private MyListView listview;
    private CommentListAdapter listAdapter;

    private void requestListData(Boolean getLiks) {
        showProgressDialog(null,true,false);
        Posts posts = new Posts();
        posts.setObjectId(post.getObjectId());

        BmobQuery<Comments> query = new BmobQuery<Comments>();
        query.include("author,replyAuthor");
        query.addWhereEqualTo("isDeleted", "0");
//        query.order("-isTop");
        query.addWhereRelatedTo("comments", new BmobPointer(post));
        query.order("-createdAt");// 降序排列
//        query.setLimit(10);
        query.findObjects(myselfContext, new FindListener<Comments>() {
            @Override
            public void onSuccess(final List<Comments> list) {
                // TODO Auto-generated method stub
                dismissProgressDialog();
                if (list != null && list.size() > 0) {

                    tv_nocontent.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);

                    listAdapter = new CommentListAdapter(myselfContext, list);
                    listview.setAdapter(listAdapter);
                    listview.setOnItemClickListener(onItemClickListener);
                } else {
                    listview.setVisibility(View.GONE);
                    tv_nocontent.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(int arg0, String arg1) {
                dismissProgressDialog();
                // TODO Auto-generated method stub
                ToastUtil.showLongToast(myselfContext, arg1);
            }
        });
        if(getLiks) {
            BmobQuery<BmobUser> query2 = new BmobQuery<BmobUser>();
            query2.addWhereRelatedTo("likes", new BmobPointer(post));
            query2.findObjects(myselfContext, new FindListener<BmobUser>() {
                @Override
                public void onSuccess(List<BmobUser> list) {
                    dismissProgressDialog();
                    if (list != null && list.size() > 0) {
                        for (BmobUser item : list) {
                            if (BmobUser.getCurrentUser(CommunityDetailAct.this).getObjectId().equals(item.getObjectId())) {
                                iv_like.setImageResource(R.drawable.icon_praised);
                                iv_like.setTag(true);
                            }

                        }
                    }
                }

                @Override
                public void onError(int i, String s) {
                    dismissProgressDialog();
                }
            });
        }
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Comments comments = listAdapter.getItem(position);
            et_comment.setHint("回复 " + comments.author.nickName + " :");
            et_comment.setTag(comments);
            isCommunity=false;
            showInputEdit(true);
            showInputMethod();

        }
    };

    private class CommentListAdapter extends BaseAdapter {

        public List<Comments> data;

        private Activity mActivity;
        private LayoutInflater inflater;
        private String activityType;

        public CommentListAdapter(Activity mActivity, List<Comments> data) {
            this.mActivity = mActivity;
            this.data = data;
            inflater = mActivity.getLayoutInflater();
        }

        public void addList(List<Comments> addedData) {
            if (data != null && data.size() > 0) {
                this.data.addAll(addedData);
                notifyDataSetChanged();
            }
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Comments getItem(int arg0) {
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
                        R.layout.comment_list_item, null);

                holder.iv_headicon = (RoundedImageView) convertView
                        .findViewById(R.id.iv_headicon);

                holder.tv_name = (TextView) convertView
                        .findViewById(R.id.tv_name);
                holder.tv_time = (TextView) convertView
                        .findViewById(R.id.tv_time);
                holder.tv_reply = (TextView) convertView
                        .findViewById(R.id.tv_reply);
                holder.tv_content = (TextView) convertView
                        .findViewById(R.id.tv_content);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Comments post = data.get(position);
            holder.iv_headicon.setDefaultImage(R.drawable.default_headicon);
            if (post.author != null) {
                holder.iv_headicon.setImageUrl(post.author.avatarURL);
                if (!TextUtils.isEmpty(post.author.nickName)) {
                    holder.tv_name.setText(post.author.nickName);
                } else {
                    holder.tv_name.setText("昵称");
                }
            }
            String createDate = post.getCreatedAt().toString();
            holder.tv_time.setText(DateUtil.getPostFormatDate(createDate));

            if (post.replyAuthor != null) {
                holder.tv_reply.setVisibility(View.VISIBLE);
                holder.tv_reply.setText("回复 " + post.replyAuthor.nickName + ":");
            } else {
                holder.tv_reply.setVisibility(View.GONE);
            }

            holder.tv_content.setText(post.content);

            return convertView;
        }

        class ViewHolder {
            RoundedImageView iv_headicon;
            TextView tv_name, tv_time, tv_reply, tv_content;
        }

    }


    private InputMethodManager imm;

    private void showInputMethod() {
        imm = (InputMethodManager) myselfContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et_comment, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);

    }

    public void hideSoftKeyboard() {
        if (imm != null && imm.isActive()) {
            imm.hideSoftInputFromWindow(et_comment.getWindowToken(), 0);
        }
    }

    private ImListenerLayout ll_root;

    private ImListenerLayout.OnSoftKeyboardListener imListener = new ImListenerLayout.OnSoftKeyboardListener() {

        @Override
        public void onShown() {
            showInputEdit(true);
        }

        @Override
        public void onHidden() {
            showInputEdit(false);
        }
    };

    private void showInputEdit(boolean show) {
        if (show) {
            ll_bottom.setVisibility(View.GONE);
            et_comment.setVisibility(View.VISIBLE);
            et_comment.requestFocus();
        } else {
            ll_bottom.setVisibility(View.VISIBLE);
            et_comment.setVisibility(View.GONE);
        }
    }

    private ObservableScrollView.ScrollViewListener onScrollChangeListener = new ObservableScrollView.ScrollViewListener() {
        @Override
        public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
            if (et_comment.getVisibility() == View.VISIBLE) {
                showInputEdit(false);
                hideSoftKeyboard();
            }
        }

    };


    protected void initPopuptWindow() {
        // TODO Auto-generated method stub

        View popupWindow_view = ((LayoutInflater) myselfContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.community_detail_popupwindow, null);
        final PopupWindow popupWindow = new PopupWindow(popupWindow_view,
                ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setFocusable(true);
        popupWindow_view.setFocusableInTouchMode(true);// 不然按KEYCODE_BACK不生效
        popupWindow_view.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK && popupWindow != null
                        && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });

        popupWindow_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                return false;
            }
        });

        LinearLayout ll_refresh = (LinearLayout) popupWindow_view.findViewById(R.id.ll_refresh);
        LinearLayout ll_share = (LinearLayout) popupWindow_view.findViewById(R.id.ll_share);
        LinearLayout ll_report = (LinearLayout) popupWindow_view.findViewById(R.id.ll_report);
        popupWindow.showAsDropDown(findViewById(R.id.rl_titleBar), 0, 0);

    }
}
