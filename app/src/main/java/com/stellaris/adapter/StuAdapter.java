package com.stellaris.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stellaris.model.User;
import com.stellaris.practice.R;

import java.util.ArrayList;
import java.util.List;

public class StuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<User> mStus = new ArrayList<User>();

    public StuAdapter(List<User> stus){
        mStus = stus;
    }

    static class StuViewHolder extends RecyclerView.ViewHolder{
        public TextView text_dor;
        public TextView text_name;
        public TextView text_stu_id;
        public TextView text_major;
        public StuViewHolder(View itemView){
            super(itemView);
            text_dor = (TextView)itemView.findViewById(R.id.item_stu_dor);
            text_name = (TextView)itemView.findViewById(R.id.item_stu_name);
            text_stu_id = (TextView)itemView.findViewById(R.id.item_stu_stuId);
            text_major = (TextView)itemView.findViewById(R.id.item_stu_major);
        }
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_stu,viewGroup,false);
        return new StuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        StuViewHolder stuholder = (StuViewHolder)holder;
        final User user = getItem(position);

        //填充数据
        stuholder.text_major.setText(user.getMajor());
        stuholder.text_dor.setText(user.getDorRoomShortId());
        stuholder.text_name.setText(user.getName());
        stuholder.text_stu_id.setText(user.getStudentId());

        stuholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    protected User getItem(int position){
        return mStus.get(position);
    }

    @Override
    public int getItemCount(){
        return mStus.size();
    }
}
