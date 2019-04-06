package com.stellaris.practice;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;
import com.stellaris.adapter.DormateAdapter;
import com.stellaris.adapter.EventAdapter;
import com.stellaris.constants.DBKeys;
import com.stellaris.constants.MsgStatus;
import com.stellaris.functions.DBHandle;
import com.stellaris.manager.UsrManager;
import com.stellaris.model.Event;
import com.stellaris.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DorMyDorFragment extends Fragment {
    @BindView(R.id.dor_my_dor_mates)
    RecyclerView mRecyclerMates;

    @BindView(R.id.dor_my_pay_detail)
    TextView mPayDetail;
    @BindView(R.id.dor_my_dor_events)
    RecyclerView mRecyclerEvents;
    @BindView(R.id.dor_my_dor_text_event_more)
    TextView dorMyDorTextEventMore;

    @BindView(R.id.pull_to_refresh)
    QMUIPullRefreshLayout mPullRefreshLayout;

    private List<Event> mEventShow = new ArrayList<>();

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
                        setMatesData();
                        setPayment();
                        setEvents();
                        mPullRefreshLayout.finishRefresh();
                    }
                }, 1000);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_dor_my_dor, container, false);
        ButterKnife.bind(this, rootView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setAutoMeasureEnabled(true);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity());
        linearLayoutManager2.setAutoMeasureEnabled(true);

        //这行不要忘了
        mRecyclerMates.setLayoutManager(linearLayoutManager);
        mRecyclerMates.setNestedScrollingEnabled(false);
        mRecyclerEvents.setLayoutManager(linearLayoutManager2);
        mRecyclerEvents.setNestedScrollingEnabled(false);
        setMatesData();
        setPayment();
        setEvents();
        initRefresh();
        return rootView;

    }

    List<User> mUsers;
    HashMap<String, String> dorInfo;

    private void setMatesData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                mUsers = DBHandle.getUsersByDor(UsrManager.getDorRoomId());
                msg.what = MsgStatus.DOR_MATES_GOT;
                handler.sendMessage(msg);
            }
        }).start();

    }

    private void setPayment() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                dorInfo = DBHandle.getDorInfo(UsrManager.getDorRoomId());
                msg.what = MsgStatus.DOR_PAYMENT_GOT;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void setEvents() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //载入AYI身份的postings
                mEventShow = DBHandle.getEventsByBuiAndSchAndRoom(UsrManager.getDorBuildingId(), UsrManager.getCollegeId(),UsrManager.getDorRoomShortId(),2);
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
                case MsgStatus.DOR_MATES_GOT: {
                    DormateAdapter adapter = new DormateAdapter(mUsers);
                    mRecyclerMates.setAdapter(adapter);
                }
                break;
                case MsgStatus.DOR_PAYMENT_GOT: {
                    String newdescr = String.format("上次查询时间：%s\n上次查询结果 水费:%s 电费:%s", dorInfo.get(DBKeys.ROOM_LAST_REQ_DATE), dorInfo.get(DBKeys.ROOM_WATER), dorInfo.get(DBKeys.ROOM_POWER));
                    mPayDetail.setText(newdescr);
                }
                break;
                case MsgStatus.EVENT_GOT: {
                    EventAdapter adapter = new EventAdapter(getActivity(), mEventShow);
                    mRecyclerEvents.setAdapter(adapter);
                }
                break;
                default:
                    break;
            }
            return false;
        }
    });


    @OnClick({R.id.dor_my_pay_detail, R.id.dor_my_dor_text_event_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dor_my_pay_detail: {
                Intent intent = new Intent(getActivity(),PayActivity.class);
                startActivityForResult(intent,MsgStatus.INTENT_SEND);
            }break;
            case R.id.dor_my_dor_text_event_more:{
                Bundle bundle = new Bundle();
                //筛选是宿舍相关
                bundle.putString("type","dor");
                Intent intent = new Intent(getActivity(),EventShowActivity.class);
                intent.putExtras(bundle);
                startActivity(intent,bundle);
            }break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==MsgStatus.INTENT_SEND&&resultCode==MsgStatus.INTENT_NEW_CONTENT){
            setMatesData();
            setEvents();
            setPayment();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
