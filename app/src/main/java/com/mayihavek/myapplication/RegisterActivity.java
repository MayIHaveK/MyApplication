package com.mayihavek.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import com.mayihavek.myapplication.dao.UserDao;
import com.mayihavek.myapplication.entity.User;
import com.mayihavek.myapplication.utils.Code;
import com.mayihavek.myapplication.utils.LogUtils;

import java.sql.SQLException;

public class RegisterActivity extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    private static EditText editTextRegister;
    @SuppressLint("StaticFieldLeak")
    private static EditText editTextRegisterPassword;
    @SuppressLint("StaticFieldLeak")
    private static EditText editTextRegisterPasswordTwo;
    @SuppressLint("StaticFieldLeak")
    private static EditText register_code;
    private static String registerAccount;
    private static String registerPasswordOne;
    private static String registerPasswordTwo;
    private static String inputCode;

    private Switch mSwitch;
    private static final int InputType_PSW_Hide = EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD;// 129
    private static final int InputType_PSW_Visible = EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;//145

    private String realCode;
    private ImageView mIvloginactivityShowcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextRegister = (EditText) findViewById(R.id.editTextRegisterAccount);
        editTextRegisterPassword = (EditText) findViewById(R.id.editTextRegisterPasswordOne);
        editTextRegisterPasswordTwo = (EditText) findViewById(R.id.editTextRegisterPasswordTWO);

        register_code = (EditText) findViewById(R.id.et_iv_register_code);

        //将验证码用图片的形式显示出来
        mIvloginactivityShowcode = (ImageView) findViewById(R.id.iv_register_showCode);
        mIvloginactivityShowcode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();


        mSwitch = (Switch) findViewById(R.id.switch2);
        // 添加监听
        mSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            boolean setVisible = editTextRegisterPassword.getInputType() == InputType_PSW_Visible;
            int type = setVisible ? InputType_PSW_Hide : InputType_PSW_Visible;

            editTextRegisterPassword.setInputType(type);
            editTextRegisterPasswordTwo.setInputType(type);
            editTextRegisterPassword.setInputType(type);
            editTextRegisterPasswordTwo.setInputType(type);
        });

    }

    public void onBack(View view) {
        //跳转回去原先页面,返回上一级就相当于结束当前页面
        finish();
    }

    public void onCodeClick(View view){
        //重新生成验证码
        mIvloginactivityShowcode = (ImageView) view;
        mIvloginactivityShowcode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();
    }

    public void onRegister(View view) {

        registerAccount = editTextRegister.getText().toString();
        registerPasswordOne = editTextRegisterPassword.getText().toString();
        registerPasswordTwo = editTextRegisterPasswordTwo.getText().toString();
        inputCode = register_code.getText().toString();

        new Thread(() -> {
            try {

                //先判断验证码是否正确
                if(!realCode.equals(inputCode)){
                    runOnUiThread(() -> LogUtils .showFailureDialog("验证码错误！", RegisterActivity.this));
                    onCodeClick(view);
                    return;
                }

                //判断用户密码等等不为空
                if(registerAccount.equals("") || registerPasswordOne.equals("") || registerPasswordTwo.equals("")){
                    return;
                }

                boolean hasUserAccount = UserDao.hasUserAccount(registerAccount);
                if (hasUserAccount) {
                    runOnUiThread(() -> LogUtils .showFailureDialog("该用户已存在！", RegisterActivity.this));
                    return;
                }

                //如果不存在 进一步 对比 两次密码输入的是否正确
                if (!registerPasswordOne.equals(registerPasswordTwo)) {
                    runOnUiThread(() -> {
                        LogUtils.showFailureDialog("两次密码输入不一致！", RegisterActivity.this);
                        //删除文本框的内容
                        editTextRegister.setText("");
                        editTextRegisterPassword.setText("");
                        editTextRegisterPasswordTwo.setText("");
                    });
                    return;
                }

                //如果都满足添加新用户
                UserDao.addUser(new User(registerAccount, registerPasswordOne, registerAccount));

                //跳转回去原先页面
                finish();


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).start();


    }

}