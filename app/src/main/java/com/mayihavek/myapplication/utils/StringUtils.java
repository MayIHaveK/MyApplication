package com.mayihavek.myapplication.utils;

import android.text.TextUtils;

public class StringUtils {

    private static final String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,32}$";

    /**
     * 检测输入密码是否符合规范
     * 8~16位数字和字母组成
     * 不能是纯数字或纯字母
     */
    public static boolean isPasswordForm(String pwd) {
        if (TextUtils.isEmpty(pwd)) return false;
        return pwd.matches(regex);
    }

}
