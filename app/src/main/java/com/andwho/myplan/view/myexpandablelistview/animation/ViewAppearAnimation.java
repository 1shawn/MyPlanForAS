package com.andwho.myplan.view.myexpandablelistview.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;


public class ViewAppearAnimation {
	public static void showViewWithAnimation(View view) {
		view.setVisibility(View.VISIBLE);
		ScaleAnimation anim = new ScaleAnimation(0f, 1f, 0f, 1f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		anim.setDuration(400);
		anim.setFillAfter(true);
		anim.setAnimationListener(new ShowViewAnimationListener(view));
		view.startAnimation(anim);
	}

	private static class ShowViewAnimationListener implements
			Animation.AnimationListener {
		private View view;

		public ShowViewAnimationListener(View view) {
			this.view = view;
		}

		@Override
		public void onAnimationStart(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {

			
			view.clearAnimation();
		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

	}

}
