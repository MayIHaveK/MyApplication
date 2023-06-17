package com.mayihavek.myapplication.task;

import android.os.AsyncTask;

import com.mayihavek.myapplication.MainActivity;
import com.mayihavek.myapplication.R;
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
        MainActivity activity = mainActivity.get();
        LogUtils.closeDialog(activity.dialog);
        if(!hasUserAccount){
            LogUtils.showFailureDialog("该用户不存在", activity);
        }else if(login){
            activity.setContentView(R.layout.activity_login_successful);
        }else {
            LogUtils.showFailureDialog("用户名或密码错误！",activity);
        }
    }

}
