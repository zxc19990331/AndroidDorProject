package com.stellaris.practice;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.stellaris.constants.DBKeys;
import com.stellaris.constants.MsgStatus;
import com.stellaris.functions.DBHandle;
import com.stellaris.manager.UsrManager;
import com.stellaris.model.Posting;

import com.stellaris.functions.ShortUUID;

import butterknife.BindView;
import butterknife.ButterKnife;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class AddPostActivity extends AppCompatActivity {

    @BindView(R.id.add_post_edit)
    EditText mPostEdit;

    @BindView(R.id.add_post_topbar)
    QMUITopBarLayout mPostTopbar;

    //该activity的意图
    Intent intent;

    private String TAG = "说两句";
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        ButterKnife.bind(this);
        initTopBar();
        getDraft();
    }

    private void initTopBar() {
        mPostTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessagePositiveDialog();
            }
        });
        mPostTopbar.addRightTextButton("发表",R.layout.button_empty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //理论上获得的应该是服务器的当前时间，但服务器并没有架设后端，所以只能先获取本地时间
                if(mPostEdit.getText().toString().isEmpty()){
                    Toast.makeText(AddPostActivity.this,"发送内容不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                String str = formatter.format(curDate);
                Posting post = new Posting();
                post.setUserID(UsrManager.getId());
                post.setUserName(UsrManager.getName());
                post.setContent(mPostEdit.getText().toString());
                post.setSchoolId(UsrManager.getCollegeId());
                post.setDorbuildingId(UsrManager.getDorBuildingId());
                post.setDate(str);
                post.setComments("");
                //生成短uuid
                post.setId(ShortUUID.generateShortUuid());
                //完善个头，不完善了

                post.setType(UsrManager.getIdentity().equals(DBKeys.USR_IDENT_AYI)?DBKeys.POST_TYPE_AYI:DBKeys.POST_TYPE_STU);

                sendPosting(post);


            }
        });
        mPostTopbar.setTitle(TAG);
    }

    @Override
    public void onBackPressed() {
        showMessagePositiveDialog();
    }


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case MsgStatus.POST_SUCCESS:{
                    Toast.makeText(AddPostActivity.this,"发送成功！",Toast.LENGTH_SHORT).show();
                    //清空草稿区
                    saveDraft(false);
                    finish();
                }break;
                case MsgStatus.POST_FAIL:{
                    Toast.makeText(AddPostActivity.this,"发送失败！",Toast.LENGTH_SHORT).show();
                }break;
            }
            return false;
        }
    });

    private void sendPosting(Posting post){
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean res = DBHandle.sendPosting(post);
                Message msg = new Message();
                if(res){
                    msg.what = MsgStatus.POST_SUCCESS;
                }
                else {
                    msg.what = MsgStatus.POST_FAIL;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    //返回时文本区若有内容则跳出dialog
    private void showMessagePositiveDialog() {
        if(!mPostEdit.getText().toString().isEmpty()) {
            new QMUIDialog.MessageDialogBuilder(this)
                    .setTitle("编辑区有未提交的内容")
                    .setMessage("是否保存草稿？")
                    .addAction("保存", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();
                            saveDraft(true);
                            Toast.makeText(AddPostActivity.this, "草稿已保存", Toast.LENGTH_SHORT).show();
                            AddPostActivity.super.onBackPressed();
                        }
                    })
                    .addAction("不保存", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            saveDraft(false);
                            dialog.dismiss();
                            AddPostActivity.super.onBackPressed();
                        }
                    })
                    .create(mCurrentDialogStyle).show();
        }
        else{
            AddPostActivity.super.onBackPressed();
        }
    }

    //保存草稿至preference
    public void saveDraft(boolean hasContent){
        //获取SharedPreferences对象
        SharedPreferences sharedPre = getSharedPreferences("user", Context.MODE_PRIVATE);
        //获取Editor对象
        SharedPreferences.Editor editor=sharedPre.edit();
        //设置参数
        editor.putString("post_draft", hasContent?mPostEdit.getText().toString():"");
        //提交
        editor.commit();
    }

    //获得preference里的草稿
    public void getDraft(){
        SharedPreferences sharedPre=getSharedPreferences("user", MODE_PRIVATE);
        String draft = sharedPre.getString("post_draft", "");
        mPostEdit.setText(draft);
    }
}
