package com.stellaris.practice;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.stellaris.adapter.PostingAdapter;
import com.stellaris.constants.DBKeys;
import com.stellaris.constants.MsgStatus;
import com.stellaris.functions.DBHandle;
import com.stellaris.manager.UsrManager;
import com.stellaris.model.Posting;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostingDetailActivity extends AppCompatActivity {

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopbar;
    @BindView(R.id.item_post_name)
    TextView itemPostName;
    @BindView(R.id.item_post_date)
    TextView itemPostDate;
    @BindView(R.id.item_post_content)
    TextView itemPostContent;
    @BindView(R.id.dor_bui_image_add)
    ImageView dorBuiImageAdd;
    @BindView(R.id.RecycleView)
    RecyclerView mRecycleView;
    @BindView(R.id.textView)
    TextView mTextView;

    private String TAG = "详情";
    private Posting mPosting;
    private List<Posting> mComments;
    private Bundle bundle;
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting_detail);
        ButterKnife.bind(this);
        bundle = getIntent().getExtras();
        mPosting = (Posting) bundle.getSerializable("posting");
        setComments();
        initView();
        initTopbar();
    }

    void initView() {
        itemPostContent.setText(mPosting.getContent());
        itemPostDate.setText(mPosting.getDate());
        itemPostName.setText(mPosting.getUserName());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PostingDetailActivity.this);
        linearLayoutManager.setAutoMeasureEnabled(true);
        mRecycleView.setHasFixedSize(true);

        mRecycleView.setLayoutManager(linearLayoutManager);
    }

    void setComments() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mComments = DBHandle.getCommentsByPostId(mPosting.getId());
                Message msg = new Message();
                msg.what = MsgStatus.POST_GOT;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void initTopbar() {
        mTopbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //如果是阿姨就添加管理按钮
        if (UsrManager.getIdentity().equals(DBKeys.USR_IDENT_AYI)||UsrManager.getId().equals(mPosting.getUserID())) {
            mTopbar.addRightTextButton("管理", R.layout.button_empty).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showBottomSheet();
                }
            });
        }
        mTopbar.setTitle(TAG);
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MsgStatus.POST_DEL_SUCCESS: {
                    //posts.remove((int)msg.obj);
                    Toast.makeText(PostingDetailActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    setResult(MsgStatus.INTENT_NEW_CONTENT);
                    finish();
                }
                break;
                case MsgStatus.POST_DEL_FAIL: {
                    Toast.makeText(PostingDetailActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                }
                break;
                case MsgStatus.POST_GOT: {
                    PostingAdapter adapter = new PostingAdapter(mComments, PostingDetailActivity.this);
                    mRecycleView.setAdapter(adapter);
                    if (mComments.size() == 0) {
                        mTextView.setText("暂时还没有人回复，来抢沙发吧");
                    }
                }
                break;
            }
            return false;
        }
    });

    private void showBottomSheet() {
        new QMUIBottomSheet.BottomListSheetBuilder(PostingDetailActivity.this)
                .addItem("删除")
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        switch (position) {
                            case 0: {
                                if (UsrManager.getIdentity().equals(DBKeys.USR_IDENT_AYI) || UsrManager.getId().equals(mPosting.getUserID())) {
                                    final String[] items = new String[]{"删除"};
                                    new QMUIDialog.MenuDialogBuilder(PostingDetailActivity.this)
                                            .addItems(items, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    new QMUIDialog.MessageDialogBuilder(PostingDetailActivity.this)
                                                            .setMessage("确定要删除吗？")
                                                            .addAction("取消", new QMUIDialogAction.ActionListener() {
                                                                @Override
                                                                public void onClick(QMUIDialog dialog, int index) {
                                                                    dialog.dismiss();
                                                                }
                                                            })
                                                            .addAction(0, "删除", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                                                                @Override
                                                                public void onClick(QMUIDialog dialog, int index) {
                                                                    new Thread(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            Message msg = new Message();
                                                                            boolean res = DBHandle.delPosting(mPosting.getId());
                                                                            if (res) {
                                                                                msg.what = MsgStatus.POST_DEL_SUCCESS;
                                                                                msg.obj = position;
                                                                            } else
                                                                                msg.what = MsgStatus.POST_DEL_FAIL;
                                                                            handler.sendMessage(msg);
                                                                        }
                                                                    }).start();
                                                                    dialog.dismiss();
                                                                }
                                                            })
                                                            .create(mCurrentDialogStyle).show();
                                                    dialog.dismiss();
                                                }
                                            })
                                            .create(mCurrentDialogStyle).show();
                                }
                            }
                            break;
                        }
                        dialog.dismiss();
                    }
                })
                .build().show();
    }

    @OnClick(R.id.dor_bui_image_add)
    public void onViewClicked() {
        Bundle bundle = new Bundle();
        bundle.putString("type","comment");
        bundle.putString("postId",mPosting.getId());
        Intent intent = new Intent(PostingDetailActivity.this,AddPostActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent,MsgStatus.INTENT_SEND,bundle);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==MsgStatus.INTENT_SEND&&resultCode==MsgStatus.INTENT_NEW_CONTENT){
            setComments();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
