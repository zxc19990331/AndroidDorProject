package com.stellaris.practice;

import android.os.Bundle;
import android.os.UserManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.stellaris.manager.UsrManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PayActivity extends AppCompatActivity {

    @BindView(R.id.pay_grid)
    GridView mGridView;

    @BindView(R.id.pay_topbar)
    QMUITopBarLayout mTopbar;

    @BindView(R.id.pay_dor_title)
    TextView mTextDor;

    private String TAG = "宿舍缴费";
    private List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

    String[] from = {"text"};
    int[] to = {R.id.item_pay_text};
    String[] texts = {"10元","20元","50元","100元","200元","自定义"};
    SimpleAdapter adapter;


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

    private void initData(){
        mTextDor.setText(UsrManager.getDorBuildingId()+"栋/"+ UsrManager.getDorRoomShortId()+"室");
        for(int i = 0;i<texts.length;i++){
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("text",texts[i]);
            dataList.add(map);
        }
        adapter = new SimpleAdapter(PayActivity.this,dataList,R.layout.item_simple_border,from,to);
        mGridView.setAdapter(adapter);
    }

}
