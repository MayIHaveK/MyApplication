package com.mayihavek.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.mayihavek.myapplication.task.LoginTask;
import com.mayihavek.myapplication.utils.LogUtils;

import java.sql.SQLException;


public class MainActivity extends AppCompatActivity {

    private EditText userAccount;
    private EditText userPassword;
    public Dialog dialog;


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
    }


    public void onLogin(View view) throws SQLException {

        String account = userAccount.getText().toString();
        String password = userPassword.getText().toString();

        //首先在主线程上 判断是否填写参数
        if(account.equals("") || password.equals("")){
            LogUtils.closeDialog(dialog);
            return;
        }

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
