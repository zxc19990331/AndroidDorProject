package com.stellaris.practice;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.stellaris.constants.MsgStatus;
import com.stellaris.functions.DBHandle;
import com.stellaris.manager.UsrManager;
import com.stellaris.model.Event;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddEventActivity extends AppCompatActivity {

    private String TAG = "添加事件";
    private String[] mCommonTitleList;
    private String[] mDors;
    private String[] mTypes;
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;

    @BindView(R.id.evene_title)
    AutoCompleteTextView mEditTitle;

    @BindView(R.id.event_note)
    AutoCompleteTextView mEditNote;

    @BindView(R.id.event_type)
    AutoCompleteTextView mEditType;

    @BindView(R.id.bt_common_use)
    QMUIRoundButton mBtCommonUse;

    @BindView(R.id.event_dors)
    AutoCompleteTextView mEditDors;

    @BindView(R.id.event_topbar)
    QMUITopBarLayout mTopbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        ButterKnife.bind(this);
        initTopbar();
        initData();
        getChooseDors();
    }

    private void initTopbar() {
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTopbar.addRightTextButton("完成",R.layout.button_empty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEvent();
            }
        });
        mTopbar.setTitle(TAG);
    }

    private void initData() {
        mCommonTitleList = getResources().getStringArray(R.array.event_title_list);
        mTypes = getResources().getStringArray(R.array.event_type_list);
        mEditType.setFocusable(false);
        mEditDors.setFocusable(false);
    }

    private void sendEvent() {
        //构造event
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String strDate = formatter.format(curDate);
        String title = mEditTitle.getText().toString();
        String type = mEditType.getText().toString();
        String dors = mEditDors.getText().toString();
        String bui = UsrManager.getDorBuildingId();
        String note = mEditNote.getText().toString();
        Event event = new Event();
        event.setBuiId(bui);
        event.setDate(strDate);
        //ID应该在服务端生成，即相当于该程序的DBHandle内
        event.setType(type);
        event.setUserId(UsrManager.getId());
        event.setTitle(title);
        event.setRoomId(dors);
        event.setSchoolId(UsrManager.getCollegeId());
        event.setBuiId(UsrManager.getDorBuildingId());
        event.setNote(note);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                boolean res = DBHandle.sendEvent(event);
                msg.what = MsgStatus.EVENT_SEND_SUCCESS;
                handler.sendMessage(msg);
            }
        }).start();

    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case MsgStatus.EVENT_SEND_SUCCESS:
                    Toast.makeText(AddEventActivity.this,"事件发布成功!",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case MsgStatus.EVENT_SEND_FAIL:
                    Toast.makeText(AddEventActivity.this,"事件发布失败!",Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

    private void getChooseDors(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> dorlist = DBHandle.getDorIdsByBuiIdAndSchoolId(UsrManager.getDorBuildingId(), UsrManager.getCollegeId());
                //list转数组
                mDors = dorlist.toArray(new String[dorlist.size()]);
            }
        }).start();
    }

    private int mTypeChooseIndex = 0;
    private int mTitleChooseIndex = 0;
    private List<String> chooseDor = new ArrayList<>();
    private int[] chooseDorIndex = {};

    @OnClick({R.id.event_type, R.id.bt_common_use, R.id.event_dors})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.event_type:
                showChooseType();
                break;
            case R.id.bt_common_use:
                showChooseTitle();
                break;
            case R.id.event_dors:
                showChooseDors();
                break;
        }
    }

    private void showChooseType(){
        new QMUIDialog.CheckableDialogBuilder(AddEventActivity.this)
                .setCheckedIndex(mTypeChooseIndex)
                .addItems(mTypes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mEditType.setText(mTypes[which]);
                        mTypeChooseIndex = which;
                        dialog.dismiss();
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    private void showChooseTitle(){
        new QMUIDialog.CheckableDialogBuilder(AddEventActivity.this)
                .setCheckedIndex(mTitleChooseIndex)
                .addItems(mCommonTitleList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mEditTitle.setText(mCommonTitleList[which]);
                        mTitleChooseIndex = which;
                        dialog.dismiss();
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    private void showChooseDors(){
        final QMUIDialog.MultiCheckableDialogBuilder builder = new QMUIDialog.MultiCheckableDialogBuilder(AddEventActivity.this)
                .setCheckedItems(chooseDorIndex)
                .addItems(mDors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.addAction("取消", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                dialog.dismiss();
            }
        });
        builder.addAction("确定", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                chooseDor.clear();
                for (int i = 0; i < builder.getCheckedItemIndexes().length; i++) {
                    chooseDor.add(mDors[builder.getCheckedItemIndexes()[i]]);
                }
                //List转json
                JSONArray jsonArray = new JSONArray(chooseDor);
                mEditDors.setText(jsonArray.toString());
                //纪录已选择index
                chooseDorIndex = builder.getCheckedItemIndexes();
                dialog.dismiss();
            }
        });
        builder.create(mCurrentDialogStyle).show();
    }

}
