package com.github.slidingmenu.viewdraghelper;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.slidingmenu.R;
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
public class MainActivity extends Activity {
	private ListView list,listview;
	private ImageView head;
	private SlideMenu2 slideMenu2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_drag);
		slideMenu2 = (SlideMenu2) findViewById(R.id.slideMenu);
		head = (ImageView) findViewById(R.id.head);
		list = (ListView) findViewById(android.R.id.list);
		listview = (ListView) findViewById(R.id.listview);
		
		head.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(slideMenu2.mStatus== SlideMenu2.Status.Close){
					slideMenu2.open();
				}
			}
		});

		list.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				Data.strings) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = (TextView) View.inflate(MainActivity.this,
						android.R.layout.simple_list_item_1, null);
				TextView textView = (TextView) view
						.findViewById(android.R.id.text1);
				textView.setTextColor(Color.parseColor("#ffffff"));
				textView.setTextSize(24);
				textView.setText(Data.strings[position]);
				return textView;
			}
		});
		listview.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				Data.strings) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = (TextView) View.inflate(MainActivity.this,
						android.R.layout.simple_list_item_1, null);
				TextView textView = (TextView) view
						.findViewById(android.R.id.text1);
				textView.setTextColor(Color.parseColor("#000000"));
				textView.setTextSize(24);
				textView.setPadding(30, 0, 0, 0);
				textView.setText(""+position);
				return textView;
			}
		});
		
		slideMenu2.setOnDragStatusChangeListener(new SlideMenu2.OnDragStatusChangeListener() {
			@Override
			public void onOpen() {
				list.smoothScrollByOffset(1);
			}
			@Override
			public void onClose() {
//				ViewPropertyAnimator.animate(head).rotationBy(360).setDuration(1000).setInterpolator(new OvershootInterpolator());
				
				ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(head, "translationX", 15);
				objectAnimator.setInterpolator(new CycleInterpolator(2));
				objectAnimator.setDuration(500);
				objectAnimator.start();
			}
			@Override
			public void onDragging(float dragProgress) {
//				Log.e("MainActivity", "dragProgress: "+dragProgress);
				head.setAlpha(1-dragProgress);
			}
		});
	}

}
