package com.stellaris.practice;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;
import com.stellaris.adapter.EventAdapter;
import com.stellaris.constants.DBKeys;
import com.stellaris.constants.MsgStatus;
import com.stellaris.functions.DBHandle;
import com.stellaris.manager.UsrManager;
import com.stellaris.model.Event;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventShowActivity extends AppCompatActivity {

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopbar;

    @BindView(R.id.RecycleView)
    RecyclerView mRecycleView;

    @BindView(R.id.pull_to_refresh)
    QMUIPullRefreshLayout mPullRefreshLayout;

    private String TAG = "宿舍事件";
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    List<Event> mEventShow;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_show);
        ButterKnife.bind(this);
        bundle = getIntent().getExtras();
        initTopbar();
        initView();
        initRefresh();
        setEvents();
    }

    private void initRefresh() {
        //设置下拉刷新
        mPullRefreshLayout.setOnPullListener(new QMUIPullRefreshLayout.OnPullListener() {
            @Override
            public void onMoveTarget(int offset) {

            }

            @Override
            public void onMoveRefreshView(int offset) {

            }

            @Override
            public void onRefresh() {
                mPullRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setEvents();
                        mPullRefreshLayout.finishRefresh();
                    }
                }, 1000);
            }
        });

    }

    private void initTopbar() {
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //如果是阿姨就添加管理按钮
        if (UsrManager.getIdentity().equals(DBKeys.USR_IDENT_AYI)) {
            mTopbar.addRightTextButton("管理", R.layout.button_empty).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showBottomSheet();
                }
            });
        }
        mTopbar.setTitle(TAG);
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
                                startActivityForResult(intent,MsgStatus.INTENT_SEND);
                                break;
                        }
                        dialog.dismiss();
                    }
                })
                .build().show();
    }

    private void setEvents() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String type = bundle.getString("type","");
                if(type.equals("all")) {
                    mEventShow = DBHandle.getEventsByBuiAndSch(UsrManager.getDorBuildingId(), UsrManager.getCollegeId(), 20);
                }else{
                    mEventShow = DBHandle.getEventsByBuiAndSchAndRoom(UsrManager.getDorBuildingId(), UsrManager.getCollegeId(),UsrManager.getDorRoomShortId(),20);
                }
                Message msg = new Message();
                msg.what = MsgStatus.EVENT_GOT;
                msg.obj = mEventShow;
                handler.sendMessage(msg);
            }
        }).start();
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MsgStatus.EVENT_GOT: {
                    EventAdapter adapter = new EventAdapter(EventShowActivity.this, mEventShow);
                    mRecycleView.setAdapter(adapter);
                }break;
                default:
                    break;
            }
            return false;
        }
    });

    private void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(EventShowActivity.this);
        linearLayoutManager.setAutoMeasureEnabled(true);
        mRecycleView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==MsgStatus.INTENT_SEND&&resultCode==MsgStatus.INTENT_NEW_CONTENT){
            setEvents();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
