package com.andwho.myplan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andwho.myplan.R;
import com.andwho.myplan.model.Comments;
import com.andwho.myplan.utils.DateUtil;
import com.andwho.myplan.view.RoundedImageView;

import java.util.List;

/**
 * Created by ys_1shawn on 2016/2/21.
 */
public class EditPostAct extends BaseAct implements View.OnClickListener {

    private static final String TAG = CommunityDetailAct.class.getSimpleName();

    private Activity myselfContext;

    private LinearLayout ll_leftIcon;
    private TextView tv_leftIcon;
    private TextView tv_title;
    private ImageView iv_rightIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_post_act);

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
        iv_rightIcon.setImageResource(R.drawable.icon_save);
    }

    private void findViews() {

    }

    private void setListener() {


    }


    private void init() {

    }


    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        int id = view.getId();
        switch (id) {
            case R.id.ll_leftIcon:
//                hideSoftKeyboard();
                finish();
                break;
            case R.id.iv_rightIcon:
                break;


            default:
                break;
        }
    }

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

//
//    private InputMethodManager imm;
//
//    private void showInputMethod() {
//        imm = (InputMethodManager) myselfContext
//                .getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(et_comment, InputMethodManager.RESULT_SHOWN);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
//                InputMethodManager.HIDE_IMPLICIT_ONLY);
//
//    }
//
//    public void hideSoftKeyboard() {
//        if (imm != null && imm.isActive()) {
//            imm.hideSoftInputFromWindow(et_comment.getWindowToken(), 0);
//        }
//    }


}
