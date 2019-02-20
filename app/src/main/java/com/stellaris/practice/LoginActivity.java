package com.stellaris.practice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Handler;
import android.os.Message;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

import com.stellaris.manager.UsrManager;
import com.stellaris.functions.DBHandle;
import com.stellaris.constants.MsgStatus;
import com.stellaris.constants.DBKeys;


public class LoginActivity extends AppCompatActivity {

    //控件id命名村规:activityname_viewtype_content
    @BindView(R.id.login_image_title)
    android.widget.ImageView imageTitle;

    @BindView(R.id.login_edit_logname)
    android.widget.AutoCompleteTextView editLogname;

    @BindView(R.id.login_edit_password)
    EditText editPassword;

    @BindView(R.id.login_text_register)
    TextView textRegister;

    @BindView(R.id.login_text_forget)
    TextView textForget;

    @BindView(R.id.login_button_login)
    android.widget.Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //读取本地sharedpreperence的用户名、密码
        SharedPreferences sharedPre=getSharedPreferences("user", MODE_PRIVATE);
        String username=sharedPre.getString("username", "");
        String password=sharedPre.getString("password", "");

        editLogname.setText(username);
        editPassword.setText(password);
    }

    //香蕉君在输入密码时会捂上眼睛
    @OnFocusChange(R.id.login_edit_password)
    public void onFocusChange(View v, boolean hasFocus){
        if(hasFocus)
            imageTitle.setImageDrawable(getResources().getDrawable(R.drawable.login_banana_1));
        else
            imageTitle.setImageDrawable(getResources().getDrawable(R.drawable.login_banana_0));
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            String str = "";
            switch(message.what){
                case MsgStatus.LOGIN_SUCCESS: {
                    //往SharedPreference中存入logname
                    String logname = editLogname.getText().toString().trim();
                    String password = editPassword.getText().toString().trim();
                    saveLoginInfo(logname, password);
                    //跳转到主页面
                    Intent intent = new Intent(LoginActivity.this, MainFrameActivity.class);
                    startActivity(intent);
                    finish();
                }
                    break;
                case MsgStatus.LOGIN_FAIL: break;
            }
            Toast.makeText(LoginActivity.this, (String)message.obj, Toast.LENGTH_SHORT).show();
            return false;
        }
    });

    //点击各按钮的事件
    @OnClick({R.id.login_text_register,R.id.login_text_forget,R.id.login_button_login})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.login_text_register:
                //注册
                break;
            case R.id.login_text_forget:
                //忘记密码
                break;
            case R.id.login_button_login:
                String logname = editLogname.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

                if(logname.equals("") || password.equals("") ) {
                    Toast.makeText(LoginActivity.this,"用户名和密码输入不能为空",Toast.LENGTH_SHORT).show();
                }
                else {
                    //Android不允许在主线程发出请求，故开启新线程
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HashMap<String, String> mp =
                                    DBHandle.getAllInfoByName(logname);

                            Message msg = new Message();
                            if(mp == null || !password.equals(mp.get(DBKeys.USR_PASWRD))) {
                                msg.what = MsgStatus.LOGIN_FAIL;
                                msg.obj = "用户名或密码输入错误";
                                //非UI线程不要试着去操作界面
                            }
                            else {
                                msg.what = MsgStatus.LOGIN_SUCCESS;
                                msg.obj = "登陆成功";
                                saveId(mp.get("id"));
                                saveUserInfo(mp);
                            }
                            handler.sendMessage(msg);
                        }
                    }).start();
                }
                break;
        }
    }


    public void saveLoginInfo(String username,String password){
        //获取SharedPreferences对象
        SharedPreferences sharedPre = getSharedPreferences("user", Context.MODE_PRIVATE);
        //获取Editor对象
        SharedPreferences.Editor editor=sharedPre.edit();
        //设置参数
        editor.putString("username", username);
        editor.putString("password", password);
        //提交
        editor.commit();
    }

    public void saveId(String id){
        //获取SharedPreferences对象
        SharedPreferences sharedPre = getSharedPreferences("user", Context.MODE_PRIVATE);
        //获取Editor对象
        SharedPreferences.Editor editor=sharedPre.edit();
        //设置参数
        editor.putString("id", id);
        //提交
        editor.commit();
    }

    public void saveUserInfo(HashMap<String,String> mp){
        UsrManager.setStudentId(mp.get(DBKeys.USR_ID));
        UsrManager.setLogname(mp.get(DBKeys.USR_LOG_NAME));
        UsrManager.setPassword(mp.get(DBKeys.USR_PASWRD));
        UsrManager.setMajor(mp.get(DBKeys.USR_MAJ));
        UsrManager.setCollegeId(mp.get(DBKeys.USR_COL_ID));
        UsrManager.setSex(mp.get(DBKeys.USR_SEX));
        UsrManager.setDorBuildingId(mp.get(DBKeys.USR_DOR_BUI_ID));
        UsrManager.setIdentity(mp.get(DBKeys.USR_IDENT));
        UsrManager.setDorRoomId(mp.get(DBKeys.USR_DOR_ROOM_ID));
        UsrManager.setDorRoomShortId(mp.get(DBKeys.USR_DOR_ROOM_SHORT));
        UsrManager.setDesc(mp.get(DBKeys.USR_DES));
        UsrManager.setName(mp.get(DBKeys.USR_NAME));
    }

}

