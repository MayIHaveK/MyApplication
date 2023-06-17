package com.mayihavek.myapplication.task;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.mayihavek.myapplication.LoginSuccessful;
import com.mayihavek.myapplication.MainActivity;
import com.mayihavek.myapplication.RegisterActivity;
import com.mayihavek.myapplication.dao.UserDao;
import com.mayihavek.myapplication.utils.LogUtils;

import java.lang.ref.WeakReference;
import java.sql.SQLException;

/**
 *  用于处理登录任务
 */
public class LoginTask extends AsyncTask<String,Void,Boolean> {

    private boolean hasUserAccount;
    /**
     * 静态内部类+弱引用 来解决传入 mainActivity 存为变量后的内存泄漏问题
     */
    private final WeakReference<MainActivity> mainActivity;

    public LoginTask(MainActivity mainActivity){
        this.mainActivity = new WeakReference<>(mainActivity);
    }

    //用于数据处理的线程
    @SuppressLint("CommitPrefEdits")
    @Override
    protected Boolean doInBackground(String... params) {
        //获取传递过来的 参数 account , password
        String account = params[0];
        String password = params[1];
        try {
            hasUserAccount = UserDao.hasUserAccount(account);

            boolean login = UserDao.login(account, password);
            if(login) {
                MainActivity activity = mainActivity.get();
                //读取和写入数据
                SharedPreferences preferences = activity.preferences;
                if(preferences.getBoolean("remember_password",false)) {
                    SharedPreferences.Editor edit = preferences.edit();
                    edit.putString("user_account", account);
                    edit.putString("user_password", password);
                    edit.apply();
                }
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }


    //渲染 UI 的线程
    @Override
    protected void onPostExecute(Boolean login) {
        MainActivity activity = mainActivity.get();
        LogUtils.closeDialog(activity.dialog);
        if(!hasUserAccount){
            LogUtils.showFailureDialog("该用户不存在", activity);
            return;
        }
        if(login){
            Intent intent = new Intent();
            intent.setClass(activity, LoginSuccessful.class);
            activity.startActivity(intent);
            return;
        }
        LogUtils.showFailureDialog("用户名或密码错误！",activity);
    }

}
