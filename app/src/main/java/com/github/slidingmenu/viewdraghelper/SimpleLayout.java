package com.github.slidingmenu.viewdraghelper;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v4.widget.ViewDragHelper.Callback;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class SimpleLayout extends LinearLayout {
	private String tag = SimpleLayout.class.getSimpleName();
	public SimpleLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private View mChild;
	private ViewDragHelper viewDragHelper;
	private int maxVerticalDragRange;
	private int maxHorizontalDragRange;

	private void init() {
		viewDragHelper = ViewDragHelper.create(this, 1, callback);
	}

	private Callback callback = new Callback() {

		/**
		 * TOUCH_DOWN:
		 */
		@Override
		public boolean tryCaptureView(View child, int pointerId) {
			return mChild == child;
		}
		/**
		 * TOUCH_DOWN:
		 */
		@Override
		public void onViewCaptured(View capturedChild, int activePointerId) {
			super.onViewCaptured(capturedChild, activePointerId);
			Log.e(tag, "拖动childView");
		}
		/**
		 * TOUCH_DOWN, TOUCH_UP, TOUCH_MOVE
		 */
		@Override
		public void onViewDragStateChanged(int state) {
			super.onViewDragStateChanged(state);
			Log.e(tag, "拖动state： "+state);
		}

		@Override
		public void onViewPositionChanged(View changedView, int left, int top,
				int dx, int dy) {
			super.onViewPositionChanged(changedView, left, top, dx, dy);
			Log.e(tag, "onViewPositionChanged  left： "+left + " top: "+top  +" dx: "+dx  +" dy:"+dy);
		}

		/**
		 * TOUCH_UP; 对移动速度的封装
		 */
		@Override
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			super.onViewReleased(releasedChild, xvel, yvel);
			Log.e(tag, "onViewReleased:  xvel:"+xvel+ "   yvel:"+yvel);
			
			if(releasedChild.getTop()>maxVerticalDragRange/2){
//				viewDragHelper.settleCapturedViewAt(0, maxVerticalDragRange);
				//直接调用Scroller.startScroll();
				viewDragHelper.smoothSlideViewTo(releasedChild, 0, maxVerticalDragRange);
				invalidate();
				
			}else {
				viewDragHelper.smoothSlideViewTo(releasedChild, 0, 0);
				ViewCompat.postInvalidateOnAnimation(SimpleLayout.this);
			}
		}

		/**
		 * 不是用来控制拖动范围的
		 */
		@Override
		public int getViewVerticalDragRange(View child) {
			Log.e(tag, "getViewVerticalDragRange");
			return 0;
//			return getMeasuredHeight()-mChild.getMeasuredHeight();
		}

		/**
		 * TOUCH_MOVE
		 */
		@Override
		public int clampViewPositionVertical(View child, int top, int dy) {
			int dragLimit = top;
			if(top<0)dragLimit = 0;
			if(top>=maxVerticalDragRange)dragLimit = maxVerticalDragRange;
			Log.e(tag, "clampViewPositionVertical  top: "+top  +" dy:"+dy +" topLimit:"+maxVerticalDragRange);
//			return dragLimit;
			return 0;
		}
		@Override
		public int getViewHorizontalDragRange(View child) {
			return 0;
		}
		/**
		 * left:总共拖动了多少
		 * dx:这次拖动了多少
		 */
		@Override
		public int clampViewPositionHorizontal(View child, int left, int dx) {
			int dragLimit = left;
			if(left<0)dragLimit = 0;
			if(left>maxHorizontalDragRange)dragLimit = maxHorizontalDragRange;
			return dragLimit;
		}
	};
	
	public void computeScroll() {
		scrollBy(0, 0);
		if(viewDragHelper.continueSettling(false)){//
			invalidate();
//			ViewCompat.postInvalidateOnAnimation(this);
		}
		
//		if(scroller.computeScrollOffset()){
//			scrollTo(scroller.getCurrX(), scroller.getCurrY());
//			invalidate();
//		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		Log.e(tag, "onLayout");
		mChild = getChildAt(0);
		maxVerticalDragRange = getMeasuredHeight()-mChild.getMeasuredHeight();
		maxHorizontalDragRange = getMeasuredWidth() - mChild.getMeasuredWidth();
		// 放到最中心
		View childView = getChildAt(0);
//		int childL = getMeasuredWidth() / 2 - childView.getMeasuredWidth() / 2;
//		int childT = getMeasuredHeight() / 2 - childView.getMeasuredHeight()
//				/ 2;
//		int childR = getMeasuredWidth() / 2 + childView.getMeasuredWidth() / 2;
//		int childB = getMeasuredHeight() / 2 + childView.getMeasuredHeight()
//				/ 2;
//		childView.layout(childL, childT, childR, childB);
		childView.layout(l, t, childView.getMeasuredWidth(), childView.getMeasuredHeight());
	}

	/**
	 * 在onMeasure之前执行，不可在这里初始化view的高
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}

	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return viewDragHelper.shouldInterceptTouchEvent(ev);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		viewDragHelper.processTouchEvent(event);
		return true;
	}
}
