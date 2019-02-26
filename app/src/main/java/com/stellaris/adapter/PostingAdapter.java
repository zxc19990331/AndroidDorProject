package com.stellaris.adapter;

import com.stellaris.model.Posting;
import com.stellaris.practice.R;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stellaris.functions.TimeUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PostingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<Posting> posts = new ArrayList<Posting>();

    public PostingAdapter(List<Posting> newList){
        this.posts = newList;
    }

    static class PostingViewHolder extends RecyclerView.ViewHolder{
        public TextView text_user_name;
        public TextView text_date;
        public TextView text_content;
        public PostingViewHolder(View itemView){
            super(itemView);
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
        postingViewHolder.text_date.setText(TimeUtil.formatDisplayTime(post.getDate(),"yyyy-MM-dd HH:mm:ss"));
        postingViewHolder.text_content.setText(post.getContent());
        postingViewHolder.text_user_name.setText(post.getUserName());

        //点击事件
        postingViewHolder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    protected Posting getItem(int position){
        return posts.get(position);
    }


}
