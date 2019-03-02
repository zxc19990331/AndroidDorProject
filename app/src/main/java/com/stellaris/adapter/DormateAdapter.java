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

public class DormateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<User> mMates = new ArrayList<User>();

    public DormateAdapter(List<User> mates){
        mMates = mates;
    }

    static class DormateViewHolder extends RecyclerView.ViewHolder{
        public TextView text_name;
        public TextView text_desc;
        public DormateViewHolder(View itemView){
            super(itemView);
            text_name = (TextView)itemView.findViewById(R.id.item_dormate_name);
            text_desc = (TextView)itemView.findViewById(R.id.item_dormate_descr);

        }
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_dormate,viewGroup,false);
        return new DormateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DormateViewHolder dormateholder = (DormateViewHolder)holder;
        final User user = getItem(position);

        //填充数据
        dormateholder.text_desc.setText(user.getDesc());
        dormateholder.text_name.setText(user.getName());

        //点击事件
        dormateholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    protected User getItem(int position){
        return mMates.get(position);
    }

    @Override
    public int getItemCount() {
        return mMates.size();
    }


}
