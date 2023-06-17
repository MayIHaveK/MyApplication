package com.mayihavek.myapplication.task;

import android.os.AsyncTask;

import com.mayihavek.myapplication.MainActivity;
import com.mayihavek.myapplication.R;
import com.mayihavek.myapplication.dao.UserDao;
import com.mayihavek.myapplication.utils.LogUtils;

import java.sql.SQLException;

public class LoginTask extends AsyncTask<String,Void,Boolean> {

    private boolean hasUserAccount;

    //用于数据处理的线程
    @Override
    protected Boolean doInBackground(String... params) {
        //获取传递过来的 参数 account , password
        String account = params[0];
        String password = params[1];
        try {
            hasUserAccount = UserDao.hasUserAccount(account);
            return UserDao.login(account, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    //渲染 UI 的线程
    @Override
    protected void onPostExecute(Boolean login) {
        MainActivity activity = MainActivity.mainActivity;
        if(!hasUserAccount){
            LogUtils.showFailureDialog("该用户不存在", activity);
        }else if(login){
            activity.setContentView(R.layout.activity_login_successful);
        }else {
            LogUtils.showFailureDialog("用户名或密码错误！",activity);
        }
    }

}
