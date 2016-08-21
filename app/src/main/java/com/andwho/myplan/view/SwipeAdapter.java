
package com.andwho.myplan.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.andwho.myplan.R;
import com.andwho.myplan.model.Messages;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;


public class SwipeAdapter extends BaseAdapter {
    /**
     * 上下文对象
     */
    private Context mContext = null;

    /**
     * 
     */
    private int mRightWidth = 0;

    /**
     * 单击事件监听器
     */
    private IOnItemRightClickListener mListener = null;
    private List<Messages> mDatas;
    public interface IOnItemRightClickListener {
        void onRightClick(View v, int position);
    }

    /**
     */
    public SwipeAdapter(Context ctx, int rightWidth, IOnItemRightClickListener l) {
        mContext = ctx;
        mRightWidth = rightWidth;
        mListener = l;

    }

    public void setData(List<Messages> data){
        this.mDatas=data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas==null?0:mDatas.size();
    }

    @Override
    public Messages getItem(int position) {
        return mDatas==null?null:mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
       final ViewHolder item;
        if (convertView == null) {
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.msgitem, parent, false);
            convertView = LayoutInflater.from(mContext).inflate(R.layout.msgitem,null);
            item = new ViewHolder();
            item.item_left = (View)convertView.findViewById(R.id.item_left);
            item.item_right = (View)convertView.findViewById(R.id.item_right);
            item.item_title_txt = (TextView)convertView.findViewById(R.id.msg_title);
            item.item_content_txt = (TextView)convertView.findViewById(R.id.msg_content);
            item.item_attention =(View)convertView.findViewById(R.id.msg_count_area);
            convertView.setTag(item);
        } else {// 有直接获得ViewHolder
            item = (ViewHolder)convertView.getTag();
        }
        LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        item.item_left.setLayoutParams(lp1);
        LayoutParams lp2 = new LayoutParams(mRightWidth, LayoutParams.MATCH_PARENT);
        item.item_right.setLayoutParams(lp2);
        item.item_title_txt.setText(mDatas.get(position).title);
        item.item_content_txt.setText(mDatas.get(position).content);
        item.item_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRightClick(v, position);
                }
            }
        });
        BmobQuery<BmobUser> query = new BmobQuery<BmobUser>();
        query.addWhereRelatedTo("hasRead", new BmobPointer(mDatas.get(position)));
        query.findObjects(mContext, new FindListener<BmobUser>() {
            @Override
            public void onSuccess(final List<BmobUser> list) {
                // TODO Auto-generated method stub
                if (list != null && list.size() > 0) {
                    for (BmobUser user:list
                         ) {
                        if(user.getObjectId().equals(BmobUser.getCurrentUser(mContext).getObjectId())){
                            item.item_attention.setVisibility(View.GONE);
                        }
                    }

                } else {

                }
            }

            @Override
            public void onError(int arg0, String arg1) {
            }
        });
        return convertView;
    }

    private class ViewHolder {
        View item_left;

        View item_right;

        TextView item_title_txt;
        TextView item_content_txt;

        View item_attention;
    }
}
