package com.github.slidingmenu.code62;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

	private SlidingMenu menu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		menu = (SlidingMenu) findViewById(R.id.sm);
	}

	public void clickTab(View view) {
		String text = ((TextView) view).getText().toString();
		Toast.makeText(this, "点击了 " + text, Toast.LENGTH_SHORT).show();

		menu.toggle();
	}

	public void clickBack(View view) {
		menu.toggle();
	}
}
