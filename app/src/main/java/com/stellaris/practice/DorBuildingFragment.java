package com.stellaris.practice;

import android.content.Intent;
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
import android.widget.LinearLayout;

import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;
import com.stellaris.adapter.PostingAdapter;
import com.stellaris.constants.DBKeys;
import com.stellaris.constants.MsgStatus;
import com.stellaris.functions.DBHandle;
import com.stellaris.manager.UsrManager;
import com.stellaris.model.Posting;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DorBuildingFragment extends Fragment {


    @BindView(R.id.dor_bui_image_add)
    ImageView ImageAdd;

    @BindView(R.id.dor_bui_posts)
    RecyclerView mRecyclePosts;

    @BindView(R.id.pull_to_refresh)
    QMUIPullRefreshLayout mPullRefreshLayout;

    @BindView(R.id.appbar)
    AppBarLayout mAppBar;

    @BindView(R.id.dor_bui_layout_find_area)
    LinearLayout mLayoutFind;

    private List<Posting> mPosts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dor_building, container, false);
        ButterKnife.bind(this, rootView);
        mPosts = new ArrayList<Posting>();
        //设置线性布局
        mRecyclePosts.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclePosts.setHasFixedSize(true);
        //禁止滑动
        mRecyclePosts.setNestedScrollingEnabled(false);
        //设置自适应高度
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setAutoMeasureEnabled(true);
        initRefresh();
        setPostings();
        return rootView;
    }


    @OnClick({R.id.dor_bui_image_add,R.id.dor_bui_layout_find_area})
    public void onViewClicked(View view) {
        //View itemPostView = View.inflate(getContext(),R.layout.item_posting,null);
        //LayoutAllposts.addView(itemPostView);
        switch (view.getId()){
            case R.id.dor_bui_image_add:{
                Bundle bundle = new Bundle();
                bundle.putString("type","post");
                Intent intent = new Intent(getActivity(), AddPostActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent,MsgStatus.INTENT_SEND,bundle);
            }break;
            case R.id.dor_bui_layout_find_area:{
                Intent intent = new Intent(getActivity(), FindStuActivity.class);
                startActivity(intent);
            }break;
        }

    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MsgStatus.POST_GOT: {
                    PostingAdapter adapter = new PostingAdapter(mPosts,getActivity());
                    mRecyclePosts.setAdapter(adapter);
                }
                break;
                default:
                    break;
            }
            return false;
        }
    });


    //注：此处的方法完成滑动为快捷之选，正常操作应将上方的控件加入Recycleview，而不是修改scrollview的属性
    //非常不建议scrollview嵌套recycleview
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
                mPosts = DBHandle.getPostingBySchoolAndBui(UsrManager.getCollegeId(), UsrManager.getDorBuildingId(), true, DBKeys.POST_TYPE_STU);
                Message msg = new Message();
                msg.what = MsgStatus.POST_GOT;
                msg.obj = mPosts;
                handler.sendMessage(msg);
            }
        }).start();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==MsgStatus.INTENT_SEND&&resultCode==MsgStatus.INTENT_NEW_CONTENT){
            setPostings();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

