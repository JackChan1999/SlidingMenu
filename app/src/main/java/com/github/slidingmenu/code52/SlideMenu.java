package com.github.slidingmenu.code52;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;
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
	private View menuView,mainView;
	private int menuWidth = 0;
	private Scroller scroller;

	public SlideMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SlideMenu(Context context) {
		super(context);
		init();
	}
	
	private void init(){
		scroller = new Scroller(getContext());
	}
	
	/**
	 * 当1级的子view全部加载完调用，可以用初始化子view的引用
	 * 注意，这里无法获取子view的宽高
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		menuView = getChildAt(0);
		mainView = getChildAt(1);
		menuWidth = menuView.getLayoutParams().width;
	}
	
	/**
	 * widthMeasureSpec和heightMeasureSpec是系统测量SlideMenu时传入的参数，
	 * 这2个参数测量出的宽高能让SlideMenu充满窗体，其实是正好等于屏幕宽高
	 */
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		
//		int measureSpec = MeasureSpec.makeMeasureSpec(menuWidth, MeasureSpec.EXACTLY);
//		
//		//测量所有子view的宽高
//		//通过getLayoutParams方法可以获取到布局文件中指定宽高
//		menuView.measure(measureSpec, heightMeasureSpec);
//		//直接使用SlideMenu的测量参数，因为它的宽高都是充满父窗体
//		mainView.measure(widthMeasureSpec, heightMeasureSpec);
//		
//	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) ev.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			int deltaX = (int) ( ev.getX()- downX);
			
			if(Math.abs(deltaX)>8){
				return true;
			}
			break;
		}
		return super.onInterceptTouchEvent(ev);
//		return super.onInterceptTouchEvent(ev);
	}

	/**
	 * l: 当前子view的左边在父view的坐标系中的x坐标
	 * t: 当前子view的顶边在父view的坐标系中的y坐标
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
//		Log.e("MAIN", "L: "+l+"   t: "+t  +"  r: "+r  + "   b: "+b);
		menuView.layout(-menuWidth, 0, 0, menuView.getMeasuredHeight());
		mainView.layout(0, 0, r, b);
	}
	
	private int downX;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) event.getX();
			int deltaX =  moveX- downX;
			
			int newScrollX = getScrollX() - deltaX;
			
			if(newScrollX < -menuWidth) newScrollX = -menuWidth;
			if(newScrollX > 0) newScrollX = 0;
			
			Log.e("Main", "scrollX: "+getScrollX());
			scrollTo(newScrollX, 0);
			downX = moveX;
			break;
		case MotionEvent.ACTION_UP:
			//1.使用自定义动画
//			ScrollAnimation scrollAnimation;
//			if(getScrollX()>-menuWidth/2){
//				//关闭菜单
////				scrollTo(0, 0);
//				scrollAnimation = new ScrollAnimation(this, 0);
//			}else {
//				//打开菜单
////				scrollTo(-menuWidth, 0);
//				scrollAnimation = new ScrollAnimation(this, -menuWidth);
//			}
//			startAnimation(scrollAnimation);
			//2.使用Scroller
			if(getScrollX()>-menuWidth/2){
//				//关闭菜单
				closeMenu();
			}else {
				//打开菜单
				openMenu();
			}
			break;
		}
		return true;
	}
	
	private void closeMenu(){
		scroller.startScroll(getScrollX(), 0, 0-getScrollX(), 0, 400);
		invalidate();
	}
	
	private void openMenu(){
		scroller.startScroll(getScrollX(), 0, -menuWidth-getScrollX(), 0, 400);
		invalidate();
	}
	
	/**
	 * Scroller不主动去调用这个方法
	 * 而invalidate()可以调这个方法
	 * invalidate->draw->computeScroll
	 */
	@Override
	public void computeScroll() {
		super.computeScroll();
		if(scroller.computeScrollOffset()){//返回true,表示动画没结束
			scrollTo(scroller.getCurrX(), 0);
			invalidate();
		}
	}

	/**
	 * 切换菜单的开和关
	 */
	public void switchMenu() {
		if(getScrollX()==0){
			//需要打开
			openMenu();
		}else {
			//需要关闭
			closeMenu();
		}
	}

}
