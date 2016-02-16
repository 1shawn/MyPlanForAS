package com.andwho.myplan.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andwho.myplan.R;

/**
 * Created by ys_1shawn on 2016/2/16.
 */
public class MyProgressBar extends LinearLayout
{
    public ProgressBar mProgressBar;
    private TextView mTextView;

    public MyProgressBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        LayoutInflater.from(context).inflate(
                R.layout.progress_bar_layout, this, true);
        mTextView = (TextView) findViewById(R.id.tv_progressBar);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    public void showProgressBar(int stringId)
    {

        mTextView.setText(stringId);
        mProgressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void setVisibility(int visibility)
    {
        // TODO Auto-generated method stub
        super.setVisibility(visibility);
        mProgressBar.setVisibility(visibility);
        mTextView.setVisibility(visibility);
    }

    public void disProgressBar(String text)
    {
        mTextView.setText(text);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    public void setProgressText(String text)
    {
        mTextView.setText(text);
    }

    public void setProgressText(int stringId)
    {
        mTextView.setText(stringId);
    }

}
