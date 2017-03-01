package com.github.slidingmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
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
public class SlidMenu extends ViewGroup {
    private int downX;
    private final int MAIN_VIEW   = 0;// 主界面
    private final int MENU_VIEW   = 1;// 左边菜单界面
    private       int currentView = MAIN_VIEW;// 记录当前界面，默认为主界面
    private Scroller scroller;// 用来模拟数据
    private int      touchSlop;

    public SlidMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlidMenu(Context context) {
        this(context, null);
    }

    @SuppressWarnings("deprecation")
    private void init() {

        scroller = new Scroller(getContext());
        touchSlop = ViewConfiguration.getTouchSlop();// 系统默认你进行了一个滑动操作的固定值
    }

    /**
     * widthMeasureSpec 屏幕的宽度heightMeasureSpec 屏幕的高度
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 测量主界面
        View mainView = getChildAt(1);// 主界面
        // 主界面宽高：屏幕的宽度屏幕的高度
        mainView.measure(widthMeasureSpec, heightMeasureSpec);
        // 测量左边菜单界面和主界面的宽高
        View menuView = getChildAt(0);// 左边菜单界面
        menuView.measure(menuView.getLayoutParams().width, heightMeasureSpec);
    }
    // viewgroup 的排版方法

    /**
     * int l 左边0 int t 上边0 int r 右边屏幕的宽度int b 下边屏幕的高度
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 排版主界面左边0， 高度0， 右边屏幕宽度，下边屏幕高度
        View mainView = getChildAt(1);
        mainView.layout(l, t, r, b);
        // 排版左边菜单界面左边-左边菜单界面的宽度， 高度0 ，右边0， 下边屏幕高度
        View menuView = getChildAt(0);
        menuView.layout(-menuView.getMeasuredWidth(), t, 0, b);
    }

    // 按下的点： downX = 100
    // 移动过后的点： moveX = 150
    // 间距diffX = -50 =100 -150 = downX - moveX;
    // scrollby(diffX,0)
    // downX = moveX = 150
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();
                // 计算间距
                int diffX = downX - moveX;
                int scrollX = getScrollX();// 获得当前界面的左上角的X 值
                // 屏幕将要移动到的X 值
                int dff = scrollX + diffX;
                if (dff < -getChildAt(0).getMeasuredWidth()) {// 设置左边界
                    scrollTo(-getChildAt(0).getMeasuredWidth(), 0);
                } else if (dff > 0) {// 设置右边界
                    scrollTo(0, 0);
                } else {// 如果不超过边界值，就要根据屏幕左上角的点的X 值，来计算位置
                    scrollBy(diffX, 0);
                }
                downX = moveX;
                break;
            case MotionEvent.ACTION_UP:
                int center = -getChildAt(0).getMeasuredWidth() / 2;
                if (getScrollX() > center) {
                    System.out.println("显示主界面");
                    currentView = MAIN_VIEW;
                } else {
                    System.out.println("显示左边菜单界面");
                    currentView = MENU_VIEW;
                }
                switchView();
                break;
            default:
                break;
        }
        return true;// 由我们自己来处理触摸事件
    }

    // 根据当前状态值来切换切面
    private void switchView() {
        int startX = getScrollX();
        int dx = 0;
        if (currentView == MAIN_VIEW) {
            // scrollTo(0, 0);
            dx = 0 - startX;
        } else {
            // scrollTo(-getChildAt(0).getMeasuredWidth(), 0);
            dx = -getChildAt(0).getMeasuredWidth() - startX;
        }
        int duration = Math.abs(dx) * 10;
        if (duration > 1000) {
            duration = 1000;
        }
        scroller.startScroll(startX, 0, dx, 0, duration);
        invalidate();
        // scrollTo(scroller.getCurrX(), 0);
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            int currX = scroller.getCurrX();
            scrollTo(currX, 0);
            invalidate();
        }
    }

    // 判断当前是否是菜单界面,true 就是菜单界面
    public boolean isMenuShow() {
        return currentView == MENU_VIEW;
    }

    // 隐藏菜单界面
    public void hideMenu() {
        currentView = MAIN_VIEW;
        switchView();
    }

    // 显示菜单界面
    public void showMenu() {
        currentView = MENU_VIEW;
        switchView();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int diff = moveX - downX;
                if (Math.abs(diff) > touchSlop) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}