package com.andwho.myplan.view.myexpandablelistview;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
//import com.sp.manager.utils.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

/**
 */

/**
 * 可固定标题的ExpandableListView
 * 
 */
public class PinnedExpandableListView extends ExpandableListView {

	/**
	 * 该ListView的Adapter必须实现该接口
	 * 
	 */
	public interface PinnedExpandableListViewAdapter {

		/**
		 * 固定标题状态：不可见
		 */
		public static final int PINNED_HEADER_GONE = 0;
		/**
		 * 固定标题状态：可见
		 */
		public static final int PINNED_HEADER_VISIBLE = 1;
		/**
		 * 固定标题状态：正在往上推
		 */
		public static final int PINNED_HEADER_PUSHED_UP = 2;

		public int getPinnedHeaderState(int groupPosition, int childPosition);

		public void configurePinnedHeader(View header, int groupPosition,
										  int childPosition, int alpha);

	}

	public static interface PinnedOnSectionChangeListener {

		void onChange(int position);
	}

	private int currentSection;
	private PinnedOnSectionChangeListener mChangeListener = new PinnedOnSectionChangeListener() {

		@Override
		public void onChange(int position) {

			if (currentSection != position) {

				currentSection = position;

				if (changeListener != null) {
					changeListener.onChange(position);
				}
			}
		}
	};

	private PinnedOnSectionChangeListener changeListener;

	public void setOnSectionChangeListener(
			PinnedOnSectionChangeListener listener) {

		this.changeListener = listener;
	}

//	public void setOnChildClickListener(
//			OnChildClickListener onChildClickListener) {
//		this.setOnChildClickListener(onChildClickListener);
//	}

	private OnScrollListener mOnScrollListener;

	public void setMyOnScrollListener(OnScrollListener onScrollListener) {
		mOnScrollListener = onScrollListener;
	}

	private static final int MAX_ALPHA = 255;

	private PinnedExpandableListViewAdapter mAdapter;
	private View mHeaderView;
	private boolean mHeaderVisible;
	private int mHeaderViewWidth;
	private int mHeaderViewHeight;

	private OnClickListener mPinnedHeaderClickLisenter;

	public void setOnPinnedHeaderClickLisenter(OnClickListener listener) {
		mPinnedHeaderClickLisenter = listener;
	}

	public PinnedExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setPinnedHeaderView(int resource) {
		View h = LayoutInflater.from(getContext()).inflate(resource, null,
				false);
		setPinnedHeaderView(h);
	}

	public void setPinnedHeaderView(View view) {
		mHeaderView = view;
		mHeaderView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));

		// Disable vertical fading when the pinned header is present
		// TODO change ListView to allow separate measures for top and bottom
		// fading edge;
		// in this particular case we would like to disable the top, but not the
		// bottom edge.
		if (mHeaderView != null) {
			setFadingEdgeLength(0);
		}
		requestLayout();
	}

	@Override
	public void setAdapter(ExpandableListAdapter adapter) {
		super.setAdapter(adapter);
		mAdapter = (PinnedExpandableListViewAdapter) adapter;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mHeaderView != null) {
			measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
			mHeaderViewWidth = mHeaderView.getMeasuredWidth();
			mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		}
	}

	private int mOldState = -1;

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		final long flatPostion = getExpandableListPosition(getFirstVisiblePosition());
		final int groupPos = ExpandableListView
				.getPackedPositionGroup(flatPostion);
		final int childPos = ExpandableListView
				.getPackedPositionChild(flatPostion);
		if (mAdapter == null) {
			// Log.e("", "@@...sp...onLayout  mAdapter == null");
			return;
		}
		int state = mAdapter.getPinnedHeaderState(groupPos, childPos);
		// 只有在状态改变时才layout，这点相当重要，不然可能导致视图不断的刷新
		if (mHeaderView != null && mAdapter != null && state != mOldState) {
			mOldState = state;
			mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
		}

		configureHeaderView(groupPos, childPos);
	}

	public void configureHeaderView(int groupPosition, int childPosition) {
		if (mHeaderView == null || mAdapter == null) {
			return;
		}

		final int state = mAdapter.getPinnedHeaderState(groupPosition,
				childPosition);
		switch (state) {
		case PinnedExpandableListViewAdapter.PINNED_HEADER_GONE: {
			mHeaderVisible = false;
			break;
		}

		case PinnedExpandableListViewAdapter.PINNED_HEADER_VISIBLE: {
			mAdapter.configurePinnedHeader(mHeaderView, groupPosition,
					childPosition, MAX_ALPHA);
			if (mHeaderView.getTop() != 0) {
				mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
			}

			mHeaderVisible = true;
			break;
		}

		case PinnedExpandableListViewAdapter.PINNED_HEADER_PUSHED_UP: {
			final View firstView = getChildAt(0);
			if (firstView == null) {
				break;
			}
			int bottom = firstView.getBottom();
			int headerHeight = mHeaderView.getHeight();
			int y;
			int alpha;
			if (bottom < headerHeight) {
				y = bottom - headerHeight;
				alpha = MAX_ALPHA * (headerHeight + y) / headerHeight;
			} else {
				y = 0;
				alpha = MAX_ALPHA;
			}
			mAdapter.configurePinnedHeader(mHeaderView, groupPosition,
					childPosition, alpha);
			if (mHeaderView.getTop() != y) {
				mHeaderView.layout(0, y, mHeaderViewWidth, mHeaderViewHeight
						+ y);
			}
			mHeaderVisible = true;
			break;
		}

		default:
			break;
		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		// 由于HeaderView并没有添加到ExpandableListView的子控件中，所以要draw他
		if (mHeaderVisible) {
			drawChild(canvas, mHeaderView, getDrawingTime());
		}
	}

	private float mDownX;
	private float mDownY;

	private static final float FINGER_WIDTH = 20;

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mHeaderVisible) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mDownX = ev.getX();
				mDownY = ev.getY();
				if (mDownX <= mHeaderViewWidth && mDownY <= mHeaderViewHeight) {
					return true;
				}
				break;
			case MotionEvent.ACTION_UP:
				float x = ev.getX();
				float y = ev.getY();

				float offsetX = Math.abs(x - mDownX);
				float offsetY = Math.abs(y - mDownY);
				// 如果在固定标题内点击了，那么触发事件
				if (x <= mHeaderViewWidth && y <= mHeaderViewHeight
						&& offsetX <= FINGER_WIDTH && offsetY <= FINGER_WIDTH) {

					if (mPinnedHeaderClickLisenter != null) {
						mPinnedHeaderClickLisenter.onClick(mHeaderView);
					}

					return true;
				}

				break;
			default:
				break;
			}
		}
		return super.onTouchEvent(ev);
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mOnScrollListener != null) {
			mOnScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		if (mOnScrollListener != null) {
			mOnScrollListener.onScroll(view, firstVisibleItem,
					visibleItemCount, totalItemCount);
		}

		final long flatPos = getExpandableListPosition(firstVisibleItem);
		int groupPosition = ExpandableListView.getPackedPositionGroup(flatPos);
		int childPosition = ExpandableListView.getPackedPositionChild(flatPos);

		mChangeListener.onChange(groupPosition);

		configureHeaderView(groupPosition, childPosition);
	}

}
