package com.stellaris.practice;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;
import com.stellaris.adapter.EventAdapter;
import com.stellaris.adapter.PostingAdapter;
import com.stellaris.constants.DBKeys;
import com.stellaris.constants.MsgStatus;
import com.stellaris.functions.DBHandle;
import com.stellaris.manager.UsrManager;
import com.stellaris.model.Event;
import com.stellaris.model.Posting;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class HomeFragment extends Fragment {


    @BindView(R.id.appbar)
    AppBarLayout mAppBar;

    @BindView(R.id.home_chalema_detail)
    TextView mChalemaDetail;

    @BindView(R.id.home_recycle_events)
    RecyclerView mEvents;

    @BindView(R.id.home_ayi_posts)
    RecyclerView mAyiPosts;

    List<Posting> mPosts;

    List<Event> mEventShow;

    @BindView(R.id.home_title_events_more)
    TextView mTitleEvents;

    @BindView(R.id.pull_to_refresh)
    QMUIPullRefreshLayout mPullRefreshLayout;

    @BindView(R.id.dor_bui_image_add)
    ImageView dorBuiImageAdd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);
        initView();
        initRefresh();
        setPostings();
        setEvents();
        return rootView;
    }

    private void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setAutoMeasureEnabled(true);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity());
        linearLayoutManager2.setAutoMeasureEnabled(true);
        mAyiPosts.setLayoutManager(linearLayoutManager);
        mAyiPosts.setNestedScrollingEnabled(false);
        mEvents.setLayoutManager(linearLayoutManager2);
        mEvents.setNestedScrollingEnabled(false);
        mEvents.setHasFixedSize(true);
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
                        setPostings();
                        setEvents();
                        mPullRefreshLayout.finishRefresh();
                    }
                }, 1000);
            }
        });

        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (verticalOffset >= 0) {
                    mPullRefreshLayout.setEnabled(true);
                } else {
                    mPullRefreshLayout.setEnabled(false);
                }
            }
        });
    }

    private void setPostings() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //载入AYI身份的postings
                mPosts = DBHandle.getPostingBySchoolAndBui(UsrManager.getCollegeId(), UsrManager.getDorBuildingId(), true, DBKeys.POST_TYPE_AYI);
                Message msg = new Message();
                msg.what = MsgStatus.POST_GOT;
                msg.obj = mPosts;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void setEvents() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //载入AYI身份的postings
                mEventShow = DBHandle.getEventsByBuiAndSch(UsrManager.getDorBuildingId(), UsrManager.getCollegeId(), 2);
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
                case MsgStatus.POST_GOT: {
                    PostingAdapter adapter = new PostingAdapter(mPosts,getActivity());
                    mAyiPosts.setAdapter(adapter);
                }
                break;
                case MsgStatus.EVENT_GOT: {
                    EventAdapter adapter = new EventAdapter(getActivity(), mEventShow);
                    mEvents.setAdapter(adapter);
                }
                break;
                default:
                    break;
            }
            return false;
        }
    });


    @OnClick({R.id.home_chalema_detail, R.id.home_title_events_more, R.id.home_title_ayisaying,R.id.dor_bui_image_add,R.id.home_layout_chalema})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.home_chalema_detail:
                break;
            case R.id.home_title_events_more: {
                Bundle bundle = new Bundle();
                bundle.putString("type", "all");
                Intent intent = new Intent(getActivity(), EventShowActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, MsgStatus.INTENT_SEND,bundle);
            }
            break;
            case R.id.home_title_ayisaying:
                break;
            case R.id.dor_bui_image_add:{
                Intent intent = new Intent(getActivity(),AddPostActivity.class);
                startActivityForResult(intent,MsgStatus.INTENT_SEND);
            }break;
            case R.id.home_layout_chalema:{
                Intent intent = new Intent(getActivity(),MapActivity.class);
                startActivity(intent);
            }break;
        }
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==MsgStatus.INTENT_SEND&&resultCode==MsgStatus.INTENT_NEW_CONTENT){
            setPostings();
            setEvents();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
