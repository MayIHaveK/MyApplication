package com.mayihavek.myapplication;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.mayihavek.myapplication.base.BaseAppCompatActivity;
import com.mayihavek.myapplication.entity.Message;
import com.mayihavek.myapplication.utils.LogUtils;
import com.mayihavek.myapplication.utils.MessageAdapter;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class LoginSuccessful extends BaseAppCompatActivity {

    /**
     * 导航栏左侧的侧边栏的父容器
     */
    private DrawerLayout mDrawerLayout;
    //导航视图
    private NavigationView mNavigationView;

    /**
     * 信息显示
     */
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    public static WeakReference<LoginSuccessful> loginSuccessful;


    @Override
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_successful);

        initViews();
        initEvents();
        loginSuccessful = new WeakReference<>(this);

        CreateActivity.title = "";
        CreateActivity.t_id = 0;
        CreateActivity.text = "";
        CreateActivity.isEdit = false;

    }

    /**
     * 初始化视图
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void initViews() {
        // 侧滑视图
        mDrawerLayout = findViewById(R.id.drawerLayout);
        mNavigationView = findViewById(R.id.nav_view);


        // 初始化消息数据
        messageList = new ArrayList<>();

        //读取数据
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);



        String all_text = preferences.getString("all_text", "");
        String[] split = all_text.split("\\$");
        for(String str : split){
            String[] strings = str.split("§");
            if(strings.length >= 3 && !"".equalsIgnoreCase(strings[1]) && !"".equalsIgnoreCase(strings[2])) {
                messageList.add(new Message(strings[1], strings[2],Integer.parseInt(strings[0])));
            }
        }

        // 初始化RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);


        //获取一个背景按钮
        ImageView icon = new ImageView(this); // Create an icon
        icon.setImageDrawable(getResources().getDrawable(R.drawable.button_menu));

        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .build();


        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        ImageView itemIcon = new ImageView(this);
        itemIcon.setImageDrawable( getResources().getDrawable(R.drawable.button_add_2) );
        SubActionButton button1 = itemBuilder.setContentView(itemIcon).build();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateActivity.title = "";
                CreateActivity.t_id = 0;
                CreateActivity.text = "";
                CreateActivity.isEdit = false;
                Intent intent = new Intent();
                intent.setClass(LoginSuccessful.this, CreateActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button1)
                .attachTo(actionButton)
                .build();

    }


    private float x1;
    private boolean isOpen;

    @SuppressLint("RtlHardcoded")
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // 继承了Activity的onTouchEvent方法，直接监听点击事件
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // 当手指按下的时候
            x1 = event.getX();
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            // 当手指移动的时候
            float x2 = event.getX();
            // 右滑动
            if (!isOpen && x2 - x1 > 50) {
                // 显示侧滑栏
                mDrawerLayout.openDrawer(Gravity.LEFT);
                isOpen = true;
            } else if (x1 - x2 > 50) {
                // 左滑
                if (isOpen) {
                    // 关闭侧滑栏
                    mDrawerLayout.closeDrawers();
                    isOpen = false;
                }
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            // 手指抬起时重置状态
            x1 = 0;
            isOpen = false;
        }

        // 将事件传递给父类进行后续处理
        return super.dispatchTouchEvent(event);
    }




    /**
     * 侧滑栏点击事件
     */
    private void initEvents () {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_info) {
                    new SweetAlertDialog(LoginSuccessful.this)
                            .setTitleText("英语学习")
                            .setContentText("一个简单的笔记本")
                            .show();
                } else if (id == R.id.menu_setting) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginSuccessful.this);
                    SharedPreferences.Editor edit = preferences.edit();
                    edit.remove("all_text");
                    edit.apply();
                    //刷新内容
                    Intent intent = new Intent();
                    //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
                    intent.setClass(LoginSuccessful.this, LoginSuccessful.class);
                    startActivity(intent);

                } else if (id == R.id.menu_about) {
                    new SweetAlertDialog(LoginSuccessful.this)
                            .setTitleText("关于")
                            .setContentText("无锡南洋欢迎你")
                            .show();
                } else if (id == R.id.menu_exit) {
                    finish();
                }else if(id == R.id.menu_thank){
                    new SweetAlertDialog(LoginSuccessful.this)
                            .setTitleText("感谢使用")
                            .setContentText("祝您生活愉快！")
                            .show();
                }

                // 关闭侧滑菜单
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }


}