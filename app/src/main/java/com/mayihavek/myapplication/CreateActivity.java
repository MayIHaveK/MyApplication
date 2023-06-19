package com.mayihavek.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.RequiresApi;

import com.mayihavek.myapplication.base.BaseAppCompatActivity;
import com.mayihavek.myapplication.utils.LogUtils;

import java.lang.ref.WeakReference;

public class CreateActivity extends BaseAppCompatActivity {

    public SharedPreferences preferences;
    public EditText titleEditText;
    public EditText correctEditText;


    public static boolean isEdit = false;
    public static String title = "";
    public static String text = "";
    public static int t_id;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        init();
    }

    public void init(){
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        titleEditText = ((EditText) findViewById(R.id.note_title));
        correctEditText = ((EditText) findViewById(R.id.note_text));
        titleEditText.setText(title);
        correctEditText.setText(text);
    }

    public void onDelete(View view){
        if(!isEdit){
            LogUtils.showFailureDialog("当前无需删除",CreateActivity.this);
            return;
        }
        //获取id,sender,message
        //获取内容
        SharedPreferences.Editor edit = preferences.edit();
        String all_text = preferences.getString("all_text", "");
        all_text = all_text.replaceAll(t_id + "\\§" + title + "\\§" + text + "\\$","");
        edit.putString("all_text",all_text);
        edit.apply();
        //跳转回去成功页面
        Intent intent = new Intent();
        intent.setClass(CreateActivity.this, LoginSuccessful.class);
        startActivity(intent);
    }

    public void onSave(View view){

        Editable text = titleEditText.getText();
        Editable textText = correctEditText.getText();
        if(textText.toString().equalsIgnoreCase("") || textText.toString().equalsIgnoreCase(""))
        {
            LogUtils.showFailureDialog("请不要输入空内容",CreateActivity.this);
            return;
        }

        if(text.toString().contains("§") || textText.toString().contains("§")){
            LogUtils.showFailureDialog("不可包含特殊字符",CreateActivity.this);
            return;
        }

        if(text.toString().contains("$") || textText.toString().contains("$")){
            LogUtils.showFailureDialog("不可包含特殊字符",CreateActivity.this);
            return;
        }

        //保存数据
        SharedPreferences.Editor edit = preferences.edit();

        String all_text = preferences.getString("all_text", "");
        int total_id = preferences.getInt("total_id",0);

        if(isEdit){
            all_text = all_text.replaceAll(t_id + "\\§" + title + "\\§" + CreateActivity.text + "\\$",t_id + "\\§" + text + "\\§" + textText + "\\$");
        }else{
            all_text += total_id + "§" + text + "§" + textText + "$";
        }


        edit.putString("all_text",all_text);
        edit.putInt("total_id",total_id + 1);
        edit.apply();
        //跳转回去成功页面
        Intent intent = new Intent();
        intent.setClass(CreateActivity.this, LoginSuccessful.class);
        startActivity(intent);
    }


}
