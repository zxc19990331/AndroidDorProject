package com.stellaris.practice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.stellaris.constants.DBKeys;
import com.stellaris.manager.UsrManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventShowActivity extends AppCompatActivity {

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopbar;

    @BindView(R.id.RecycleView)
    RecyclerView mRecycleView;

    private String TAG = "宿舍公告板";
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_show);
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
        if(UsrManager.getIdentity().equals(DBKeys.USR_IDENT_AYI)){
            mTopbar.addRightTextButton("管理",R.layout.button_empty).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    showBottomSheet();
                }
            });
        }
    }

    private void showBottomSheet() {
        new QMUIBottomSheet.BottomListSheetBuilder(EventShowActivity.this)
                .addItem("添加事件")
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        switch (position) {
                            case 0:
                                Intent intent = new Intent(EventShowActivity.this, AddEventActivity.class);
                                startActivity(intent);
                                break;
                        }
                        dialog.dismiss();
                    }
                })
                .build().show();
    }
    private void initData() {

    }

}
