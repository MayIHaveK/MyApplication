package com.mayihavek.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.mayihavek.base.BaseAppCompatActivity;
import com.mayihavek.myapplication.task.LoginTask;
import com.mayihavek.myapplication.utils.LogUtils;

import java.sql.SQLException;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends BaseAppCompatActivity {

    private EditText userAccount;
    private EditText userPassword;
    public Dialog dialog;

    public SharedPreferences preferences;  //利用SharedPreferences 进行数据存储
    private CheckBox remember;  //定义记住密码

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }


    public void init(){
        userAccount = (EditText) findViewById(R.id.editTextText);
        userPassword = (EditText) findViewById(R.id.editTextTextPassword);
        dialog = LogUtils.createLoadingDialog(MainActivity.this, "加载中");
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        remember = (CheckBox) findViewById(R.id.remember_password);

        //remember_password 作为 是否开启存储密码数据的 key
        boolean isRemember = preferences.getBoolean("remember_password",false);
        if (isRemember){
            //设置账号和密码
            userAccount.setText(preferences.getString("user_account",""));
            userPassword.setText(preferences.getString("user_password",""));
            //设置勾选
            remember.setChecked(true);
        }

    }


    public void onLogin(View view) throws SQLException {

        String account = userAccount.getText().toString();
        String password = userPassword.getText().toString();

        //首先在主线程上 判断是否填写参数
        if(account.equals("") || password.equals("")){
            LogUtils.closeDialog(dialog);
            return;
        }

        //登录的时候存入选择框的内容,设置是否记住密码
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean("remember_password",remember.isChecked());
        edit.apply();

        //开启一个加载图标
        dialog.show();
        LoginTask task = new LoginTask(this);
        task.execute(account,password);
    }


    public void onRegister(View view) {
        //监听按钮，如果点击，就跳转
        Intent intent = new Intent();
        //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
        intent.setClass(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }


}
