package com.github.slidingmenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ============================================================
 * Copyright：${TODO}有限公司版权所有 (c) 2017
 * Author：   陈冠杰
 * Email：    815712739@qq.com
 * GitHub：   https://github.com/JackChen1999
 * 博客：     http://blog.csdn.net/axi295309066
 * 微博：     AndroidDeveloper
 * <p>
 * Project_Name：SlidingMenu
 * Package_Name：com.github.slidingmenu
 * Version：1.0
 * time：2016/3/1 12:02
 * des ：侧滑菜单
 * gitVersion：2.12.0.windows.1
 * updateAuthor：$Author$
 * updateDate：$Date$
 * updateDes：${TODO}
 * ============================================================
 */
public class MainActivity2 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main2);
        ImageButton main_back = (ImageButton) findViewById(R.id.main_back);
        final SlidMenu slidmenu = (SlidMenu) findViewById(R.id.slidmenu);
        main_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean currentView = slidmenu.isMenuShow();
                if(currentView){//当前菜单界面显示，就隐藏
                    slidmenu.hideMenu();
                }else{
                    slidmenu.showMenu();
                }
            }
        });
    }
    public void clickTab(View v){
        TextView tv = (TextView) v;
        Toast.makeText(getApplicationContext(), tv.getText(), Toast.LENGTH_SHORT).show();
    }
}