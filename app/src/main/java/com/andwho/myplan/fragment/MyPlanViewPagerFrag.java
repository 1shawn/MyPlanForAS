package com.andwho.myplan.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andwho.myplan.R;
import com.andwho.myplan.view.MyViewPager;

public abstract class MyPlanViewPagerFrag extends BaseFrag {

	private static final String TAG = MyPlanViewPagerFrag.class.getSimpleName();

	// 子页面数组
	private Fragment[] fragments;
	private MyViewPager tabPager;

	public void initViewPager(View view) {

		tabPager = (MyViewPager) LayoutInflater.from(getActivity()).inflate(
				R.layout.view_pager_content, null);
		fragments = getFragments();
		ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
		tabPager.setAdapter(adapter);
		tabPager.setOnPageChangeListener(pageChangeListener);

		getContainer(view).addView(tabPager);

		setTabSelected(0);
		setPageSelected(0);
	}

	// 取得装载MyViewPager的容器ViewGroup
	public abstract ViewGroup getContainer(View view);

	public void setPageSelected(int position) {
		tabPager.setCurrentItem(position, true);
	}

	// 当PageSelected状态改变时候的回调
	public abstract void setTabSelected(int position);

	// 取得MyViewPager中的Fragment
	public abstract Fragment[] getFragments();

	private class ViewPagerAdapter extends FragmentPagerAdapter {
		private FragmentManager fm;

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
			this.fm = fm;

		}

		@Override
		public void setPrimaryItem(View container, int position, Object object) {
			// TODO Auto-generated method stub
			super.setPrimaryItem(container, position, object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			Fragment fragment = (Fragment) super.instantiateItem(container,

			position);

			String fragmentTag = fragment.getTag();
			if (fragments[position].getTag() == null
					|| !fragments[position].getTag().equals(fragmentTag)) {

				FragmentTransaction ft = fm.beginTransaction();

				ft.remove(fragment);

				fragment = fragments[position % fragments.length];

				ft.add(container.getId(), fragment, fragmentTag);

				ft.attach(fragment);

				ft.commit();
			}

			return fragment;

		}

		@Override
		public Fragment getItem(int arg0) {
			return fragments[arg0];

		}

		@Override
		public int getCount() {
			return fragments.length;
		}

	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageScrollStateChanged(int position) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			setTabSelected(position);
		}

	};
}
