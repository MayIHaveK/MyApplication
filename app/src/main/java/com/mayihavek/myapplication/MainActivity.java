package com.mayihavek.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.mayihavek.myapplication.dao.UserDao;
import com.mayihavek.myapplication.task.LoginTask;
import com.mayihavek.myapplication.utils.LogUtils;

import java.sql.SQLException;


public class MainActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    private static EditText userAccount;
    @SuppressLint("StaticFieldLeak")
    private static EditText userPassword;
    private static String account;
    private static String password;
    @SuppressLint("StaticFieldLeak")
    public static MainActivity mainActivity;


    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        userAccount = (EditText) findViewById(R.id.editTextText);
        userPassword = (EditText) findViewById(R.id.editTextTextPassword);
        mainActivity = MainActivity.this;

        progressDialog = new ProgressDialog(this, R.style.progressDialog);
        //设置不可点击外边取消动画
        progressDialog.setCanceledOnTouchOutside(false);

    }

    public void onLogin(View view) throws SQLException {

        account = userAccount.getText().toString();
        password = userPassword.getText().toString();

        //首先在主线程上 判断是否填写参数
        if(account.equals("") || password.equals("")){
            //LogUtils.closeDialog(dialog);
            return;
        }


        LoginTask task = new LoginTask();
        task.execute(account,password);

        //显示加载页面
        //Dialog dialog = LogUtils.createLoadingDialog(MainActivity.this, "加载中");

/*        new Thread(() -> {
            try {
                boolean hasUserAccount = UserDao.hasUserAccount(account);

                runOnUiThread(() -> {

                    if(account.equals("") || password.equals("")){
                        LogUtils.closeDialog(dialog);
                        return;
                    }
                    if (!hasUserAccount) {
                        LogUtils.showFailureDialog("该用户不存在！", mainActivity);
                        LogUtils.closeDialog(dialog);
                        return;
                    }
                    new Thread(() -> {
                        try {
                            boolean login = UserDao.login(account, password);
                            runOnUiThread(() -> {
                                if (login) {
                                    setContentView(R.layout.activity_login_successful);
                                    LogUtils.closeDialog(dialog);
                                    return;
                                }
                                LogUtils.closeDialog(dialog);
                                LogUtils.showFailureDialog("用户名或密码错误！", mainActivity);
                            });

                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }).start();

                });
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).start();*/

    }


    public void onRegister(View view) {
        //监听按钮，如果点击，就跳转
        Intent intent = new Intent();
        //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
        intent.setClass(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }


}
