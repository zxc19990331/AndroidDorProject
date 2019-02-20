package com.stellaris.practice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;


import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends AppCompatActivity {


    @BindView(R.id.setting_list)
    QMUIGroupListView mList;

    @BindView(R.id.setting_topbar)
    QMUITopBarLayout mTopBar;

    private String TAG = "应用设置";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initTopBar();
        initList();
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



    private void initList() {
        QMUICommonListItemView item_clear = mList.createItemView("清除缓存");
        item_clear.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        item_clear.setTag(0);

        QMUICommonListItemView item_version = mList.createItemView("应用版本");
        item_version.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        item_version.setDetailText(getResources().getString(R.string.version));
        item_version.setTag(1);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch ((int) v.getTag()) {
                    case 0: {
                        QMUITipDialog tipDialog;
                        tipDialog = new QMUITipDialog.Builder(SettingActivity.this)
                                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                                .setTipWord("正在清除缓存")
                                .create();
                        tipDialog.show();
                        mList.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tipDialog.dismiss();
                                Toast.makeText(SettingActivity.this, "清除成功", Toast.LENGTH_SHORT).show();
                            }
                        }, 1500);
                    }break;
                    case 1:{
                    }
                    break;
                    default:
                        break;
                }
            }
        };

        int size = QMUIDisplayHelper.dp2px(this, 20);
        QMUIGroupListView.newSection(this)
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(item_clear, onClickListener)
                .addItemView(item_version, onClickListener)
                .addTo(mList);
    }
}
