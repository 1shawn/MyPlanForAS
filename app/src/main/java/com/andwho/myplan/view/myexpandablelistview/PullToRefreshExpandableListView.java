/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.andwho.myplan.view.myexpandablelistview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import com.andwho.myplan.view.myexpandablelistview.PinnedExpandableListView.PinnedOnSectionChangeListener;
import com.andwho.myplan.view.myexpandablelistview.adapter.EmptyViewMethodAccessor;
import com.andwho.myplan.view.myexpandablelistview.adapter.OverscrollHelper;

public class PullToRefreshExpandableListView extends
		PullToRefreshAdapterViewBase<ExpandableListView> {

	public PullToRefreshExpandableListView(Context context) {
		super(context);
	}

	public PullToRefreshExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullToRefreshExpandableListView(Context context, Mode mode) {
		super(context, mode);
	}

	public PullToRefreshExpandableListView(Context context, Mode mode,
			AnimationStyle style) {
		super(context, mode, style);
	}

	@Override
	public final Orientation getPullToRefreshScrollDirection() {
		return Orientation.VERTICAL;
	}

	private PinnedExpandableListView mExpandableListView;

	@Override
	protected PinnedExpandableListView createRefreshableView(Context context,
			AttributeSet attrs) {
		final PinnedExpandableListView lv;
		if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
			lv = new InternalExpandableListViewSDK9(context, attrs);
		} else {
			lv = new InternalExpandableListView(context, attrs);
		}

		// Set it to this so it can be used in ListActivity/ListFragment
		lv.setId(android.R.id.list);
		mExpandableListView = lv;

		return lv;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		super.onScrollStateChanged(view, scrollState);
		if (mExpandableListView != null) {
			mExpandableListView.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		if (mExpandableListView != null) {
			mExpandableListView.onScroll(view, firstVisibleItem,
					visibleItemCount, totalItemCount);
		}
	}

	public void setSelectionFromTop(int position, int y) {
		if (mExpandableListView != null) {
			mExpandableListView.setSelectionFromTop(position, y);
		}
	}

	public boolean isGroupExpanded(int groupPosition) {
		if (mExpandableListView != null) {
			return mExpandableListView.isGroupExpanded(groupPosition);
		} else {
			return false;
		}
	}

	public void setAdapter(ExpandableListAdapter adapter) {
		if (mExpandableListView != null) {
			mExpandableListView.setAdapter(adapter);
		}
	}

	@SuppressLint("NewApi")
	public void expandGroup(int groupPos) {
		if (mExpandableListView != null) {
			mExpandableListView.expandGroup(groupPos, false);
		}
	}

	@SuppressLint("NewApi")
	public void expandGroup(int groupPos, boolean animate) {
		if (mExpandableListView != null) {
			mExpandableListView.expandGroup(groupPos, animate);
		}
	}

	public void setPinnedHeaderViewId(int resource) {
		if (mExpandableListView != null) {
			mExpandableListView.setPinnedHeaderView(resource);
		}
	}

	public void setPinnedHeaderView(View view) {
		if (mExpandableListView != null) {
			mExpandableListView.setPinnedHeaderView(view);// .setHeaderView(view);

		}
	}

	public void smoothScrollToPosition(int position) {
		if (mExpandableListView != null) {
			mExpandableListView.smoothScrollToPosition(position);
		}
	}

	public void setOnPinnedHeaderClickLisenter(OnClickListener listener) {
		if (mExpandableListView != null) {
			mExpandableListView.setOnPinnedHeaderClickLisenter(listener);
		}
	}

	// public void setOnChildClickListener(
	// OnChildClickListener onChildClickListener) {
	// if (mExpandableListView != null) {
	// mExpandableListView.setOnChildClickListener(onChildClickListener);
	// }
	// }

	public int getFirstVisiblePosition() {
		if (mExpandableListView != null) {
			return mExpandableListView.getFirstVisiblePosition();
		} else {
			return 0;
		}
	}

	public long getExpandableListPosition() {
		if (mExpandableListView != null) {
			return mExpandableListView
					.getExpandableListPosition(getFirstVisiblePosition());
		} else {
			return 0;
		}
	}

	public void collapseGroup(int groupPosition) {
		if (mExpandableListView != null) {
			mExpandableListView.collapseGroup(groupPosition);
		}
	}

	public void setOnSectionChangeListener(
			PinnedOnSectionChangeListener listener) {
		if (mExpandableListView != null) {
			mExpandableListView.setOnSectionChangeListener(listener);
		}
	}

	public void setMyOnScrollListener(OnScrollListener onScrollListener) {
		if (mExpandableListView != null) {
			mExpandableListView.setMyOnScrollListener(onScrollListener);
		}
	}

	public void setOnChildClickListener(
			OnChildClickListener onChildClickListener) {
		if (mExpandableListView != null) {
			mExpandableListView.setOnChildClickListener(onChildClickListener);
		}
	}

	public PinnedExpandableListView getTrueExpandableListView() {
		return mExpandableListView;
	}

	class InternalExpandableListView extends PinnedExpandableListView implements
			EmptyViewMethodAccessor {

		public InternalExpandableListView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		public void setEmptyView(View emptyView) {
			PullToRefreshExpandableListView.this.setEmptyView(emptyView);
		}

		@Override
		public void setEmptyViewInternal(View emptyView) {
			super.setEmptyView(emptyView);
		}
	}

	final class InternalExpandableListViewSDK9 extends
			InternalExpandableListView {

		public InternalExpandableListViewSDK9(Context context,
				AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
				int scrollY, int scrollRangeX, int scrollRangeY,
				int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

			final boolean returnValue = super.overScrollBy(deltaX, deltaY,
					scrollX, scrollY, scrollRangeX, scrollRangeY,
					maxOverScrollX, maxOverScrollY, isTouchEvent);

			// Does all of the hard work...
			OverscrollHelper.overScrollBy(PullToRefreshExpandableListView.this,
					deltaX, scrollX, deltaY, scrollY, isTouchEvent);

			return returnValue;
		}
	}
}
