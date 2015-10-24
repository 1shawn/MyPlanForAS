package com.andwho.myplan.view;



import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class MyViewPager extends ViewPager implements ScrollViewLocker {
	/**
	 * 是否可滑动的标志
	 */
	private boolean isCanScroll = true;

	public MyViewPager(Context context) {
		super(context);
	}

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setCanScroll(boolean isCanScroll) {
		this.isCanScroll = isCanScroll;
	}

	@Override
	public void scrollTo(int x, int y) {
		if (isCanScroll) {
			super.scrollTo(x, y);
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (isCanScroll)

			return super.onTouchEvent(ev);
		else
			return false;
	}

	// @Override
	// public boolean dispatchTouchEvent(MotionEvent ev) {
	// if (isCanScroll) {
	// return super.dispatchTouchEvent(ev);
	// } else {
	// return false;
	// }
	// }

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (isCanScroll) {
			return super.onInterceptTouchEvent(ev);
		} else {
			return false;
		}
	}
	
}