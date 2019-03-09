package com.stellaris.practice;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.stellaris.adapter.StuAdapter;
import com.stellaris.constants.MsgStatus;
import com.stellaris.functions.DBHandle;
import com.stellaris.manager.UsrManager;
import com.stellaris.model.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FindStuActivity extends AppCompatActivity {

    String TAG = "找人";

    @BindView(R.id.find_topbar)
    QMUITopBarLayout mTopbar;

    @BindView(R.id.search_bar)
    SearchView mSearchBar;

    @BindView(R.id.recycleView)
    RecyclerView mRecycleView;

    private List<User> mUsers = new ArrayList<>();
    private List<User> mShowUsers = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_stu);
        ButterKnife.bind(this);
        initTopBar();
        initView();
        initSearchBar();
        setData();
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
           switch (msg.what){
               case MsgStatus.STU_FIND_GOT:{
                   StuAdapter adapter = new StuAdapter(mShowUsers);
                   mRecycleView.setAdapter(adapter);
               }break;
           }
           return false;
        }});



    private void setData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                mUsers = DBHandle.getStusByBui(UsrManager.getDorBuildingId());
                mShowUsers.addAll(mUsers);//深拷贝
                msg.what = MsgStatus.STU_FIND_GOT;
                handler.sendMessage(msg);
            }
        }).start();

    }

    private void initView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FindStuActivity.this);
        mRecycleView.setLayoutManager(linearLayoutManager);
    }

    private void initSearchBar(){
        mSearchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.isEmpty()){
                    mShowUsers = mUsers;
                }
                else if(isNumeric(query)){
                    mShowUsers.clear();
                    for(int i =0;i<mUsers.size();i++){
                        if(mUsers.get(i).getDorRoomShortId().contains(query)||mUsers.get(i).getStudentId().contains(query)){
                            mShowUsers.add(mUsers.get(i));
                        }
                    }
                }else{
                    mShowUsers.clear();
                    for(int i =0;i<mUsers.size();i++){
                        if(mUsers.get(i).getName().contains(query)||mUsers.get(i).getMajor().contains(query)){
                            mShowUsers.add(mUsers.get(i));
                        }
                    }

                }
                //更新显示列表
                Message msg = new Message();
                msg.what = MsgStatus.STU_FIND_GOT;
                handler.sendMessage(msg);
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)){
                    mShowUsers.clear();
                    mShowUsers.addAll(mUsers);
                }
                else if(isNumeric(newText)){
                    mShowUsers.clear();
                    for(int i =0;i<mUsers.size();i++){
                        if(mUsers.get(i).getDorRoomShortId().contains(newText)||mUsers.get(i).getStudentId().contains(newText)){
                            mShowUsers.add(mUsers.get(i));
                        }
                    }
                }else{
                    mShowUsers.clear();
                    for(int i =0;i<mUsers.size();i++){
                        if(mUsers.get(i).getName().contains(newText)||mUsers.get(i).getMajor().contains(newText)){
                            mShowUsers.add(mUsers.get(i));
                        }
                    }

                }
                Message msg = new Message();
                msg.what = MsgStatus.STU_FIND_GOT;
                handler.sendMessage(msg);
                return false;
            }
        });
    }

    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

}
