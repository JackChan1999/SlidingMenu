package com.github.slidingmenu.viewdraghelper;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v4.widget.ViewDragHelper.Callback;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
/**
 * ============================================================
 * Copyright：Google有限公司版权所有 (c) 2017
 * Author：   AllenIverson
 * Email：    815712739@qq.com
 * GitHub：   https://github.com/JackChen1999
 * 博客：     http://blog.csdn.net/axi295309066
 * 微博：     AndroidDeveloper
 * <p>
 * Project_Name：SlidingMenu
 * Package_Name：com.github.slidingmenu
 * Version：1.0
 * time：2017/3/1 13:06
 * des ：SlidingMenu 侧边栏 侧滑菜单
 * gitVersion：2.12.0.windows.1
 * updateAuthor：$Author$
 * updateDate：$Date$
 * updateDes：${TODO}
 * ============================================================
 */
public class SlideMenu extends FrameLayout{
	private String TAG = SlideMenu.class.getSimpleName();
	public SlideMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private ViewDragHelper viewDragHelper;
	private void init(){
		viewDragHelper = ViewDragHelper.create(this, callback);
	}
	
	private View menuView,mainView;
	private int menuWidth;
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if(getChildCount()<2){
			throw new IllegalStateException("Your layout must has 2 children or more!");
		}
		menuView = getChildAt(0);
		mainView = getChildAt(1);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		menuWidth = menuView.getMeasuredWidth();
		menuView.layout(-1*menuWidth, t, l, b);
		mainView.layout(l, t, r, b);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return viewDragHelper.shouldInterceptTouchEvent(ev);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		viewDragHelper.processTouchEvent(event);
		return true;
	}
	
	private Callback callback = new Callback() {
		@Override
		public boolean tryCaptureView(View child, int pointerId) {
			return child==menuView || child==mainView;
		}
		@Override
		public void onViewDragStateChanged(int state) {
			super.onViewDragStateChanged(state);
		}
		@Override
		public void onViewPositionChanged(View changedView, int left, int top,
				int dx, int dy) {
			super.onViewPositionChanged(changedView, left, top, dx, dy);
//			Log.e(TAG, "onViewPositionChanged   dx: "+dx);
			if(changedView==mainView){
				menuView.layout(menuView.getLeft()+dx, 0, menuView.getRight()+dx, menuView.getBottom());
			}else {
				mainView.layout(mainView.getLeft()+dx, 0, mainView.getRight()+dx, mainView.getBottom());
			}
			
//			float percent = mainView.getLeft()/(float)menuWidth;
//			Log.e(TAG, "percent: "+percent);
//			ViewHelper.setScaleX(menuView, 0.5f+0.5f*percent);
//			ViewHelper.setScaleY(menuView, 0.5f+0.5f*percent);
		}
		@Override
		public void onViewCaptured(View capturedChild, int activePointerId) {
			super.onViewCaptured(capturedChild, activePointerId);
		}
		@Override
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			super.onViewReleased(releasedChild, xvel, yvel);
			Log.e(TAG, "onViewReleased ："+(releasedChild==mainView));
			if(mainView.getLeft()>menuWidth/2){
				viewDragHelper.smoothSlideViewTo(mainView, menuWidth, 0);
				ViewCompat.postInvalidateOnAnimation(SlideMenu.this);
			}else {
				viewDragHelper.smoothSlideViewTo(mainView, 0, 0);
				ViewCompat.postInvalidateOnAnimation(SlideMenu.this);
			}
		}
		@Override
		public int getViewHorizontalDragRange(View child) {
			return menuWidth;
		}
		@Override
		public int clampViewPositionHorizontal(View child, int left, int dx) {
			if(child==mainView){
				if(left<0)return 0;
				if(left>menuWidth) return menuWidth;
			}
			if(child==menuView){
				if(left<-1*menuWidth)return -1*menuWidth;
				if(left>0) return 0;
			}
			return left;
		}
	};
	
	public void computeScroll() {
		if(viewDragHelper.continueSettling(true)){
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}
}
