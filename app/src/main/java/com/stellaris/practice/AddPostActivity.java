package com.stellaris.practice;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddPostActivity extends AppCompatActivity {

    @BindView(R.id.add_post_edit)
    EditText mPostEdit;

    @BindView(R.id.add_post_topbar)
    QMUITopBarLayout mPostTopbar;

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
                Log.d(TAG,"发表");
            }
        });
        mPostTopbar.setTitle(TAG);
    }

    @Override
    public void onBackPressed() {
        showMessagePositiveDialog();
    }


    private void showMessagePositiveDialog() {
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

    public void getDraft(){
        SharedPreferences sharedPre=getSharedPreferences("user", MODE_PRIVATE);
        String draft = sharedPre.getString("post_draft", "");
        mPostEdit.setText(draft);
    }
}
