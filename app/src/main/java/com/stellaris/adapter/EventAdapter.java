package com.stellaris.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stellaris.constants.DBKeys;
import com.stellaris.model.Event;
import com.stellaris.practice.R;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<Event> mEvents = new ArrayList<Event>();
    Context context;
    public EventAdapter(Context context,List<Event> newList){
        this.mEvents = newList;
        this.context = context;
    }

    static class EventViewHolder extends RecyclerView.ViewHolder{
        public TextView text_title;
        public TextView text_date;
        public TextView text_note;
        public TextView text_dors;
        public TextView text_tag;
        public ImageView image;
        public EventViewHolder(View itemView){
            super(itemView);
            text_title = (TextView)itemView.findViewById(R.id.item_event_title);
            text_date = (TextView)itemView.findViewById(R.id.item_event_date);
            text_dors = (TextView)itemView.findViewById(R.id.item_event_dors);
            text_tag = (TextView)itemView.findViewById(R.id.item_event_tag);
            text_note = (TextView)itemView.findViewById(R.id.item_event_note);
            image = (ImageView)itemView.findViewById(R.id.item_event_image);
        }
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_event,viewGroup,false);
        return new EventAdapter.EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        EventViewHolder eventholder = (EventViewHolder) holder;
        final Event event = getItem(position);

        //填充数据
        eventholder.text_title.setText(event.getTitle());
        eventholder.text_dors.setText(event.getRoomId().replaceAll("[\\[\\]\"]",""));
        eventholder.text_date.setText(event.getDate().substring(0,19));
        eventholder.text_tag.setText(event.getType());
        eventholder.text_note.setText(event.getNote());
        String type = event.getType();
        switch (type){
            case DBKeys.EVENT_TYPE_GOOD:
                //更改相应图标和tag
                eventholder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_smile_black_24dp));
                break;
            case DBKeys.EVENT_TYPE_BAD:
                eventholder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_frown_black_24dp));
                eventholder.text_tag.setBackgroundColor(context.getResources().getColor(R.color.colorYellow));
                break;
            case DBKeys.EVENT_TYPE_COMMON:
                break;
        }

        //点击事件
        eventholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    protected Event getItem(int position){
        return mEvents.get(position);
    }
}
