package com.stellaris.practice;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.stellaris.constants.MsgStatus;
import com.stellaris.functions.DBHandle;
import com.stellaris.manager.UsrManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PayActivity extends AppCompatActivity {

    final private int TYPE_POWER = 0x0;
    final private int TYPE_WATER = 0x1;
    @BindView(R.id.pay_grid)
    GridView mGridView;

    @BindView(R.id.pay_topbar)
    QMUITopBarLayout mTopbar;

    @BindView(R.id.pay_dor_title)
    TextView mTextDor;

    @BindView(R.id.pay_grid_water)
    GridView mGridWater;

    private String TAG = "宿舍缴费";
    private List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

    String[] from = {"text"};
    int[] to = {R.id.item_pay_text};
    String[] texts = {"10元", "20元", "50元", "100元", "200元", "自定义"};
    double[] pays = {10, 20, 50, 100, 200};
    SimpleAdapter adapter;

    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTopbar.setTitle(TAG);
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MsgStatus.PAY_UPDATE_SUCCESS: {
                    //Toast.makeText(PayActivity.this,"充值成功！",Toast.LENGTH_SHORT).show();
                }
                break;
                case MsgStatus.PAY_UPDATE_FAIL: {
                    Toast.makeText(PayActivity.this, "充值失败！", Toast.LENGTH_SHORT).show();
                }
                break;
                case MsgStatus.MSG_FINISH: {
                    Toast.makeText(PayActivity.this, "充值完成！", Toast.LENGTH_SHORT).show();
                    setResult(MsgStatus.INTENT_NEW_CONTENT);
                    //finish();
                }
                break;
            }
            return false;
        }
    });


    private void UpdatePay(double num,int type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                boolean res = DBHandle.UpdatePay(UsrManager.getDorRoomId(), num,type);
                msg.what = res ? MsgStatus.PAY_UPDATE_SUCCESS : MsgStatus.PAY_UPDATE_FAIL;
                handler.sendMessage(msg);
            }
        }).start();
    }

    public AdapterView.OnItemClickListener OnGridClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position <= 4) {
                new QMUIDialog.MessageDialogBuilder(PayActivity.this)
                        .setTitle("操作确认")
                        .setMessage("是否确定要充值" + pays[position] + "元?")
                        .addAction("确定", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                //清空activity栈并跳转
                                QMUITipDialog tipDialog;
                                tipDialog = new QMUITipDialog.Builder(PayActivity.this)
                                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                                        .setTipWord("正在处理")
                                        .create();
                                tipDialog.show();
                                dialog.dismiss();
                                UpdatePay(pays[position],TYPE_POWER);
                                mGridView.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        tipDialog.dismiss();
                                        Message msg = new Message();
                                        msg.what = MsgStatus.MSG_FINISH;
                                        handler.sendMessage(msg);
                                    }
                                }, 1500);

                            }
                        })
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .create(mCurrentDialogStyle).show();
            } else {
                ;
            }
        }
    };
    public AdapterView.OnItemClickListener OnGridWaterClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position <= 4) {
                new QMUIDialog.MessageDialogBuilder(PayActivity.this)
                        .setTitle("操作确认")
                        .setMessage("是否确定要充值" + pays[position] + "元?")
                        .addAction("确定", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                //清空activity栈并跳转
                                QMUITipDialog tipDialog;
                                tipDialog = new QMUITipDialog.Builder(PayActivity.this)
                                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                                        .setTipWord("正在处理")
                                        .create();
                                tipDialog.show();
                                dialog.dismiss();
                                UpdatePay(pays[position],TYPE_WATER);
                                mGridView.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        tipDialog.dismiss();
                                        Message msg = new Message();
                                        msg.what = MsgStatus.MSG_FINISH;
                                        handler.sendMessage(msg);
                                    }
                                }, 1500);

                            }
                        })
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .create(mCurrentDialogStyle).show();
            } else {
                ;
            }
        }
    };

    private void initData() {
        mTextDor.setText(UsrManager.getDorBuildingId() + "栋/" + UsrManager.getDorRoomShortId() + "室");
        for (int i = 0; i < texts.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("text", texts[i]);
            dataList.add(map);
        }
        adapter = new SimpleAdapter(PayActivity.this, dataList, R.layout.item_simple_border, from, to);
        mGridView.setAdapter(adapter);
        mGridWater.setAdapter(adapter);
        mGridView.setOnItemClickListener(OnGridClick);
        mGridWater.setOnItemClickListener(OnGridWaterClick);
    }


}
