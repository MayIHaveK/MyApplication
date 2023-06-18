package com.mayihavek.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import com.mayihavek.myapplication.base.BaseAppCompatActivity;
import com.mayihavek.myapplication.task.RegisterTask;
import com.mayihavek.myapplication.utils.Code;
import com.mayihavek.myapplication.utils.LogUtils;
import com.mayihavek.myapplication.utils.StringUtils;

public class RegisterActivity extends BaseAppCompatActivity {

    public EditText editTextRegister;
    public EditText editTextRegisterPassword;
    public EditText editTextRegisterPasswordTwo;
    public String realCode;
    public ImageView ShowCodeImage;
    public EditText register_code;
    public Dialog dialog;
    private static final int InputType_PSW_Hide = EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD;// 129
    private static final int InputType_PSW_Visible = EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;//145


    @Override
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //载入默认数据
        init();
        //注册事件
        registerListener();
    }

    public void onBack(View view) {
        //跳转回去原先页面,返回上一级就相当于结束当前页面
        finish();
    }

    public void onCodeClick(View view) {
        //重新生成验证码
        ShowCodeImage = (ImageView) view;
        ShowCodeImage.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();
    }

    public void onRegister(View view) {

        String registerAccount = editTextRegister.getText().toString();
        String registerPasswordOne = editTextRegisterPassword.getText().toString();
        String registerPasswordTwo = editTextRegisterPasswordTwo.getText().toString();
        String inputCode = register_code.getText().toString();

        //判断用户密码等等不为空
        if (registerAccount.equals("") || registerPasswordOne.equals("") || registerPasswordTwo.equals("")) {
            return;
        }

        //自定义密码规则,8~16位数字和字母组成
        if(!StringUtils.isPasswordForm(registerPasswordOne)){
            LogUtils.showFailureDialog("密码要求同时包含数字和字母且在8~32位",this);
            return;
        }


        //生成加载动画
        dialog.show();

        //注册任务
        RegisterTask registerTask = new RegisterTask(this);
        registerTask.execute(inputCode, registerAccount, registerPasswordOne, registerPasswordTwo);
    }

    public void init() {
        editTextRegister = (EditText) findViewById(R.id.editTextRegisterAccount);
        editTextRegisterPassword = (EditText) findViewById(R.id.editTextRegisterPasswordOne);
        editTextRegisterPasswordTwo = (EditText) findViewById(R.id.editTextRegisterPasswordTWO);
        register_code = (EditText) findViewById(R.id.et_iv_register_code);

        //将验证码用图片的形式显示出来
        ShowCodeImage = (ImageView) findViewById(R.id.iv_register_showCode);
        ShowCodeImage.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();

        //创建进度条
        dialog = LogUtils.createLoadingDialog(RegisterActivity.this, "加载中");
    }

    public void registerListener(){
        // 添加监听
        SwitchCompat mSwitch = (SwitchCompat) findViewById(R.id.switch2);
        mSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            boolean setVisible = editTextRegisterPassword.getInputType() == InputType_PSW_Visible;
            int type = setVisible ? InputType_PSW_Hide : InputType_PSW_Visible;

            editTextRegisterPassword.setInputType(type);
            editTextRegisterPasswordTwo.setInputType(type);
            editTextRegisterPassword.setInputType(type);
            editTextRegisterPasswordTwo.setInputType(type);
        });
    }

}