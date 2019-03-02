package com.stellaris.practice;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stellaris.adapter.PostingAdapter;
import com.stellaris.constants.DBKeys;
import com.stellaris.constants.MsgStatus;
import com.stellaris.functions.DBHandle;
import com.stellaris.manager.UsrManager;
import com.stellaris.model.Posting;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment {


    //TODO: 增加百度地图API为基础的查房打卡系统
    @BindView(R.id.home_chalema_detail)
    TextView mChalemaDetail;

    @BindView(R.id.home_recycle_events)
    RecyclerView mEvents;

    @BindView(R.id.home_ayi_posts)
    RecyclerView mAyiPosts;

    List<Posting> mPosts;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


    private void setPostings() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //载入AYI身份的postings
                mPosts = DBHandle.getPostingBySchoolAndBui(UsrManager.getCollegeId(), UsrManager.getDorBuildingId(),true, DBKeys.POST_TYPE_AYI);
                Message msg = new Message();
                msg.what = MsgStatus.POST_GOT;
                msg.obj = mPosts;
                handler.sendMessage(msg);
            }
        }).start();
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MsgStatus.POST_GOT: {
                    PostingAdapter adapter = new PostingAdapter(mPosts);
                    mAyiPosts.setAdapter(adapter);
                }
                break;
                default:
                    break;
            }
            return false;
        }
    });

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
