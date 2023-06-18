package com.mayihavek.myapplication;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;

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

import java.util.ArrayList;
import java.util.List;


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

    @Override
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_successful);

        initViews();
        initEvents();
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        // 侧滑视图
        mDrawerLayout = findViewById(R.id.drawerLayout);
        mNavigationView = findViewById(R.id.nav_view);


        // 初始化消息数据
        messageList = new ArrayList<>();
        messageList.add(new Message("小明", "你好，今天有空吗？"));
        messageList.add(new Message("小红", "嗨，有什么事？"));
        messageList.add(new Message("小明", "我们可以一起出去玩吗？"));
        messageList.add(new Message("小红", "好的，什么时候？"));
        messageList.add(new Message("小明", "下周六怎么样？"));
        messageList.add(new Message("小明", "下周六怎么样？"));
        messageList.add(new Message("小明", "下周六怎么样？"));
        messageList.add(new Message("小明", "下周六怎么样？"));

        // 初始化RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);


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
                    LogUtils.showFailureDialog("个人信息", LoginSuccessful.this);
                } else if (id == R.id.menu_pwd) {
                    LogUtils.showFailureDialog("修改密码", LoginSuccessful.this);
                } else if (id == R.id.menu_setting) {
                    LogUtils.showFailureDialog("设置", LoginSuccessful.this);
                } else if (id == R.id.menu_about) {
                    LogUtils.showFailureDialog("关于", LoginSuccessful.this);
                } else if (id == R.id.menu_exit) {
                    LogUtils.showFailureDialog("退出", LoginSuccessful.this);
                }

                // 关闭侧滑菜单
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }


}