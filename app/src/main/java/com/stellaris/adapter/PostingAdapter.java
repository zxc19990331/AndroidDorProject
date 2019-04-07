package com.stellaris.adapter;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.stellaris.constants.DBKeys;
import com.stellaris.constants.MsgStatus;
import com.stellaris.functions.DBHandle;
import com.stellaris.manager.UsrManager;
import com.stellaris.model.Posting;
import com.stellaris.practice.LoginActivity;
import com.stellaris.practice.PostingDetailActivity;
import com.stellaris.practice.R;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.stellaris.functions.TimeUtil;
import com.stellaris.practice.RegisterActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class PostingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<Posting> posts = new ArrayList<Posting>();
    Context context;
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;

    public PostingAdapter(List<Posting> newList,Context context){
        this.posts = newList;
        this.context = context;
    }

    static class PostingViewHolder extends RecyclerView.ViewHolder{
        public TextView text_user_name;
        public TextView text_date;
        public TextView text_content;
        public ImageView image_avatar;
        public PostingViewHolder(View itemView){
            super(itemView);
            image_avatar = (ImageView)itemView.findViewById(R.id.dor_bui_layout_post_usr_image);
            text_user_name = (TextView)itemView.findViewById(R.id.item_post_name);
            text_content = (TextView)itemView.findViewById(R.id.item_post_content);
            text_date = (TextView)itemView.findViewById(R.id.item_post_date);
        }
    }

    @Override
    @NonNull public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return createPostViewHolder(viewGroup);
    }

    private RecyclerView.ViewHolder createPostViewHolder(ViewGroup viewGroup){
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_posting,viewGroup,false);
        return new PostingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BindForPosting(holder,position);
    }

    private void BindForPosting(RecyclerView.ViewHolder holder, int position){
        PostingViewHolder postingViewHolder = (PostingViewHolder)holder;
        final Posting post = getItem(position);

        //填充数据
        //日期格式化
        postingViewHolder.text_date.setText(TimeUtil.formatDisplayTime(post.getDate(),"yyyy-MM-dd HH:mm:ss"));
        postingViewHolder.text_content.setText(post.getContent());
        postingViewHolder.text_user_name.setText(post.getUserName());
        String post_id = post.getId();
        //点击头像的事件
        postingViewHolder.image_avatar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final HashMap<String,String> mp = DBHandle.getDetailById(post.getUserID());
                        String info = String.format("学校：%s \n学号：%s \n姓名：%s \n性别：%s \n专业：%s \n宿舍楼：%s \n宿舍：%s \n个性签名:%s",
                                mp.get(DBKeys.USR_COL_ID),mp.get(DBKeys.USR_STU_ID),mp.get(DBKeys.USR_NAME),mp.get(DBKeys.USR_SEX),mp.get(DBKeys.USR_MAJ),
                                mp.get(DBKeys.USR_DOR_BUI_ID),mp.get(DBKeys.USR_DOR_ROOM_SHORT),mp.get(DBKeys.USR_DESR));
                        Message msg = new Message();
                        msg.what = MsgStatus.POST_GET_USR_INFO;
                        msg.obj =info;
                        handler.sendMessage(msg);
                    }
                }).start();
            }
        });

        postingViewHolder.text_content.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(UsrManager.getIdentity().equals(DBKeys.USR_IDENT_AYI)||UsrManager.getId().equals(post.getUserID())){
                    final String[] items = new String[]{"删除"};
                    new QMUIDialog.MenuDialogBuilder(context)
                            .addItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new QMUIDialog.MessageDialogBuilder(context)
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
                                                            boolean res = DBHandle.delPosting(post_id);
                                                            if(res) {
                                                                msg.what = MsgStatus.POST_DEL_SUCCESS;
                                                                msg.obj = position;
                                                            }
                                                            else
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
                return false;
            }
        });
        postingViewHolder.text_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("posting",post);
                Intent intent = new Intent(context, PostingDetailActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case MsgStatus.POST_GET_USR_INFO:{
                    new QMUIDialog.MessageDialogBuilder(context)
                            .setTitle("个人信息")
                            .setMessage((String)msg.obj)
                            .addAction("确定", new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                }
                            })
                            .addAction("取消", new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                }
                            })
                            .create(mCurrentDialogStyle).show();
                }break;
                case MsgStatus.POST_DEL_SUCCESS:{
                    //posts.remove((int)msg.obj);
                    Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
                }break;
                case MsgStatus.POST_DEL_FAIL:{
                    Toast.makeText(context,"删除失败",Toast.LENGTH_SHORT).show();
                }break;
            }
            return false;
        }
    });

    @Override
    public int getItemCount() {
        return posts.size();
    }


    protected Posting getItem(int position){
        return posts.get(position);
    }


}
