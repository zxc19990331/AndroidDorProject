package com.stellaris.practice;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.stellaris.constants.DBKeys;
import com.stellaris.constants.MsgStatus;
import com.stellaris.functions.DBHandle;
import com.stellaris.model.User;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    String TAG = "新用户注册";
    final private int MIN_LENGTH = 7;
    @BindView(R.id.reg_logname)
    AutoCompleteTextView mLogname;

    @BindView(R.id.reg_password)
    EditText mPassword;

    @BindView(R.id.reg_password_again)
    EditText mPasswordAgain;

    @BindView(R.id.button)
    Button mButton;

    @BindView(R.id.reg_topbar)
    QMUITopBarLayout mTopbar;

    @BindView(R.id.reg_stu_id)
    EditText mStuId;

    @BindView(R.id.reg_sch)
    EditText mSch;

    private User curUser = new User();
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    private String[] items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initTopbar();
        initData();
    }

    private void initTopbar() {
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTopbar.setTitle(TAG);
    }

    private void initData(){
        //初始化可选学校列表
        items = getResources().getStringArray(R.array.school_list);
        //设置不可编辑
        mSch.setFocusable(false);
    }

    @OnClick({R.id.button,R.id.reg_sch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button: {
                String logname = mLogname.getText().toString();
                String password = mPassword.getText().toString();
                String password_again = mPasswordAgain.getText().toString();
                String stu_id = mStuId.getText().toString();
                //暂时先截取两位序号
                String sch_id = mSch.getText().toString().substring(0,1);
                //我知道这句判断跟下一句重复了，但是我还是要写
                if (logname.equals("") || password.equals("") || password_again.equals("")) {
                    Toast.makeText(RegisterActivity.this, "输入不可为空!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (logname.length() < MIN_LENGTH || password.length() < MIN_LENGTH) {
                    Toast.makeText(RegisterActivity.this, "用户名和密码长度至少7位!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(password_again)) {
                    Toast.makeText(RegisterActivity.this, "两次密码输入不一致!", Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HashMap<String,String> mp = DBHandle.getAllInfoByLogname(logname);
                        Message msg = new Message();
                        //若logname不存在
                        if(mp==null){
                            //mp2判断是否存在该学号
                            final HashMap<String,String >mp2 = DBHandle.getStuInfoByIdAndSch(stu_id,sch_id);
                            //若mp2不存在即不存在该学号
                            if(mp2==null){
                                msg.what=MsgStatus.REG_NO_STU_ID;
                            }
                            //存在该学号
                            else{
                                HashMap<String,String>mp3 = DBHandle.getUserInfoByStuIdAndSch(stu_id,sch_id);
                                //mp3表示是否已经有用户绑定了该学号
                                //没有用户绑定该学号
                                if(mp3==null){
                                    //这里是mp2，可恶，乱命名毁一生
                                    String info = String.format("学校：%s 学号：%s 姓名：%s 性别：%s 专业：%s 宿舍楼：%s 宿舍：%s",
                                            mp2.get(DBKeys.USR_COL_ID),mp2.get(DBKeys.USR_STU_ID),mp2.get(DBKeys.USR_NAME),mp2.get(DBKeys.USR_SEX),mp2.get(DBKeys.USR_MAJ),
                                            mp2.get(DBKeys.USR_DOR_BUI_ID),mp2.get(DBKeys.USR_DOR_ROOM_SHORT));
                                    //Dialog跟Toast类似都需要加入loop(这样会导致主UI线程锁住 一般放入handler但我觉得没必要)
                                    Looper.prepare();
                                    new QMUIDialog.MessageDialogBuilder(RegisterActivity.this)
                                            .setTitle("信息确认")
                                            .setMessage(info)
                                            .addAction("确定", new QMUIDialogAction.ActionListener() {
                                                @Override
                                                public void onClick(QMUIDialog dialog, int index) {
                                                    curUser.init(mp2);
                                                    curUser.setLogname(logname);
                                                    curUser.setPassword(password);
                                                    boolean res = DBHandle.insertUser(curUser);
                                                    if(res){
                                                        msg.what = MsgStatus.REG_SUCCESS;
                                                        //消息传递写的有点问题，直接事件跳转了
                                                        Toast.makeText(RegisterActivity.this, "注册绑定成功！", Toast.LENGTH_SHORT).show();
                                                        saveLoginInfo(logname,password);
                                                        //跳转并清空activity栈
                                                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(intent);
                                                    }else{
                                                        msg.what = MsgStatus.REG_FAIL;
                                                        Toast.makeText(RegisterActivity.this, "注册绑定失败！", Toast.LENGTH_SHORT).show();
                                                    }
                                                    dialog.dismiss();
                                                }
                                            })
                                            .addAction("取消", new QMUIDialogAction.ActionListener() {
                                                @Override
                                                public void onClick(QMUIDialog dialog, int index) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .create(mCurrentDialogStyle).show();
                                    Looper.loop();
                                }
                                //已经有用户绑定了该学号
                                else {
                                    msg.what = MsgStatus.REG_EXIST_STU_ID;
                                }
                            }
                        }
                        //logname已经存在
                        else {
                            msg.what = MsgStatus.USR_EXIST_LOGNAME;
                        }
                        handler.sendMessage(msg);
                    }
                }).start();
            }break;
            case R.id.reg_sch:{
                showSchoolDialog();
            }break;
        }
    }


    int checkedIndex = 0;

    private void showSchoolDialog() {
        //数据库暂时只有一个学校
        new QMUIDialog.CheckableDialogBuilder(RegisterActivity.this)
                .setCheckedIndex(checkedIndex)
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSch.setText(items[which]);
                        checkedIndex = which;
                        dialog.dismiss();
                    }
                })
                .create(mCurrentDialogStyle).show();
    }
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case MsgStatus.USR_EXIST_LOGNAME:{
                    Toast.makeText(RegisterActivity.this, "该用户名已存在！", Toast.LENGTH_SHORT).show();
                }break;
                case MsgStatus.REG_DIFF_INPUT:{
                    Toast.makeText(RegisterActivity.this, "两次密码输入不一致！", Toast.LENGTH_SHORT).show();
                    mPassword.setText("");
                    mPasswordAgain.setText("");
                }break;
                case MsgStatus.REG_EMPTY_INPUT:{
                    Toast.makeText(RegisterActivity.this, "输入不得为空！", Toast.LENGTH_SHORT).show();
                }break;
                case MsgStatus.REG_LEN_TOO_SHORT:{
                    Toast.makeText(RegisterActivity.this, "用户名和密码长度至少为7位！", Toast.LENGTH_SHORT).show();
                }break;
                case MsgStatus.REG_FAIL:{
                    Toast.makeText(RegisterActivity.this, "注册绑定失败！", Toast.LENGTH_SHORT).show();
                }break;
                case MsgStatus.REG_SUCCESS:{
                    Toast.makeText(RegisterActivity.this, "注册绑定成功！", Toast.LENGTH_SHORT).show();
                    saveLoginInfo(mLogname.getText().toString(),mPassword.getText().toString());
                    //跳转并清空activity栈
                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }break;
                case MsgStatus.REG_EXIST_STU_ID:{
                    Toast.makeText(RegisterActivity.this, "该学号已经被绑定！", Toast.LENGTH_SHORT).show();
                }break;
                case MsgStatus.REG_NO_STU_ID:{
                    Toast.makeText(RegisterActivity.this, "不存在该学号！", Toast.LENGTH_SHORT).show();
                }break;
            }
            return false;
        }
    });

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

}
