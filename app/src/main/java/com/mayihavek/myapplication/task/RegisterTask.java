package com.mayihavek.myapplication.task;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.mayihavek.myapplication.RegisterActivity;
import com.mayihavek.myapplication.dao.UserDao;
import com.mayihavek.myapplication.entity.User;
import com.mayihavek.myapplication.utils.LogUtils;

import java.lang.ref.WeakReference;
import java.sql.SQLException;
import java.util.Objects;


public class RegisterTask extends AsyncTask<String, Void, Byte> {
    /**
     * 解决传入 registerActivity 作为成员变量的内存泄漏问题
     */
    private final WeakReference<RegisterActivity> registerActivity;

    private final Byte CodeCorrect = 1;
    private final Byte HasUserAccount = 2;
    private final Byte PasswordOneNotEqualsTwo = 3;
    private final Byte Finish = 4;

    public RegisterTask(RegisterActivity registerActivity) {
        this.registerActivity = new WeakReference<>(registerActivity);
    }

    @Override
    protected Byte doInBackground(String... params) {

        RegisterActivity activity = registerActivity.get();
        String realCode = activity.realCode;
        String inputCode = params[0];

        //判断验证码是否正确
        if (!inputCode.equals(realCode)) {
            return CodeCorrect;
        }

        String registerAccount = params[1];
        //获取是否已经存在当前这个用户名的用户
        try {
            boolean hasUserAccount = UserDao.hasUserAccount(registerAccount);
            if (hasUserAccount) {
                return HasUserAccount;
            }
            //如果不存在，进一步对比两次输入的密码是否一致
            String registerPasswordOne = params[2];
            String registerPasswordTwo = params[3];
            if (!registerPasswordOne.equals(registerPasswordTwo)) {
                return PasswordOneNotEqualsTwo;
            }

            //如果都满足添加新用户
            UserDao.addUser(new User(registerAccount, registerPasswordOne, registerAccount));
            return Finish;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void onPostExecute(Byte type) {
        RegisterActivity activity = registerActivity.get();

        //关闭加载动画
        LogUtils.closeDialog(activity.dialog);

        //刷新验证码
        activity.onCodeClick(activity.ShowCodeImage);

        if (Objects.equals(type, CodeCorrect)) {
            //弹出提示
            LogUtils.showFailureDialog("验证码错误！", activity);
            return;
        }

        //删除密码框和验证码框的内容
        activity.editTextRegisterPassword.setText("");
        activity.editTextRegisterPasswordTwo.setText("");
        activity.register_code.setText("");


        if (Objects.equals(type, HasUserAccount)) {
            //弹出提示
            LogUtils.showFailureDialog("该用户已存在！", activity);
            //删除账号框的内容
            activity.editTextRegister.setText("");
            return;
        }

        if (Objects.equals(type, PasswordOneNotEqualsTwo)) {
            LogUtils.showFailureDialog("两次密码输入不一致！", activity);
            return;
        }

        //跳转回去原先页面
        activity.finish();

    }

}
