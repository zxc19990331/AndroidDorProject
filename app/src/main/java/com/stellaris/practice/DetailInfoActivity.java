package com.stellaris.practice;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.stellaris.constants.DBKeys;
import com.stellaris.constants.MsgStatus;
import com.stellaris.functions.DBHandle;
import com.stellaris.manager.UsrManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailInfoActivity extends AppCompatActivity {

    private String TAG = "详细信息";
    private HashMap<String, String> mDetailInfo;

    @BindView(R.id.detail_info_list)
    QMUIGroupListView mGroupListView;

    @BindView(R.id.detail_info_topbar)
    QMUITopBarLayout mTopBar;

    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);
        ButterKnife.bind(this);
        initTopBar();
        initList();
        getDetailInfo();

    }


    private void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTopBar.setTitle(TAG);
    }

    private List<String> itemTags  = new ArrayList<>();

    private void initList() {
        QMUICommonListItemView item_name = mGroupListView.createItemView("姓名");
        QMUICommonListItemView item_school = mGroupListView.createItemView("学校");
        QMUICommonListItemView item_dor_building = mGroupListView.createItemView("宿舍楼");
        QMUICommonListItemView item_dor_room = mGroupListView.createItemView("宿舍");
        QMUICommonListItemView item_major = mGroupListView.createItemView("专业");
        QMUICommonListItemView item_stu_id = mGroupListView.createItemView("学号");
        QMUICommonListItemView item_sex = mGroupListView.createItemView("性别");
        QMUICommonListItemView item_descr = mGroupListView.createItemView("个性签名");

        item_name.setTag(DBKeys.USR_NAME);
        item_school.setTag(DBKeys.USR_COL_ID);
        item_dor_building.setTag(DBKeys.USR_DOR_BUI_ID);
        item_dor_room.setTag(DBKeys.USR_DOR_ROOM_SHORT);
        item_major.setTag(DBKeys.USR_MAJ);
        item_stu_id.setTag(DBKeys.USR_STU_ID);
        item_sex.setTag(DBKeys.USR_SEX);
        item_descr.setTag(DBKeys.USR_DESR);

        itemTags.add(DBKeys.USR_NAME);
        itemTags.add(DBKeys.USR_COL_ID);
        itemTags.add(DBKeys.USR_DOR_BUI_ID);
        itemTags.add(DBKeys.USR_DOR_ROOM_SHORT);
        itemTags.add(DBKeys.USR_MAJ);
        itemTags.add(DBKeys.USR_STU_ID);
        itemTags.add(DBKeys.USR_SEX);
        itemTags.add(DBKeys.USR_DESR);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch ((String)v.getTag()) {
                    case DBKeys.USR_DESR:{
                        showEditTextDialog();
                    }break;
                }
            }
        };


        int size = QMUIDisplayHelper.dp2px(DetailInfoActivity.this, 20);
        QMUIGroupListView.newSection(DetailInfoActivity.this)
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(item_name, onClickListener)
                .addItemView(item_sex, onClickListener)
                .addItemView(item_school, onClickListener)
                .addItemView(item_dor_building, onClickListener)
                .addItemView(item_dor_room, onClickListener)
                .addItemView(item_major, onClickListener)
                .addItemView(item_stu_id, onClickListener)
                .addItemView(item_descr, onClickListener)
                .addTo(mGroupListView);


    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case MsgStatus.DETAIL_GOT: {
                    mDetailInfo = (HashMap<String, String>) message.obj;

                    for (int i = 0; i < itemTags.size(); i++) {
                        String key = itemTags.get(i);
                        QMUICommonListItemView item = (QMUICommonListItemView) mGroupListView.findViewWithTag(key);
                        item.setDetailText(mDetailInfo.get(key));
                    }
                }break;
                case MsgStatus.INFO_DESCR_CHANGE:{
                    Toast.makeText(DetailInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    getDetailInfo();
                }break;
            }

            return false;
        }
    });

    private void getDetailInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> mp = DBHandle.getDetailById(getId());
                Message msg = new Message();
                msg.obj = mp;
                msg.what = MsgStatus.DETAIL_GOT;
                handler.sendMessage(msg);
            }
        }).start();

    }

    private String getId() {
        SharedPreferences sharedPre = getSharedPreferences("user", MODE_PRIVATE);
        String id = sharedPre.getString("id", "");
        return id;
    }

    private void showEditTextDialog() {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(DetailInfoActivity.this);
        builder.setTitle("个性签名")
                .setPlaceholder("在此输入您的个性签名")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .setDefaultText(UsrManager.getDesc())
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText();
                        if (text != null && text.length() > 0) {
                            UpdateDescr(text.toString());
                            dialog.dismiss();
                        } else {
                            Toast.makeText(DetailInfoActivity.this, "请勿置空", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    private void UpdateDescr(String descr){
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean res = DBHandle.UpdateDescr(UsrManager.getId(),descr);
                if(res)
                    UsrManager.setDesc(descr);
                Message msg = new Message();
                msg.what=MsgStatus.INFO_DESCR_CHANGE;
                handler.sendMessage(msg);
            }
        }).start();
    }
}
