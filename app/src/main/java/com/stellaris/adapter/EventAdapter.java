package com.stellaris.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.stellaris.constants.DBKeys;
import com.stellaris.constants.MsgStatus;
import com.stellaris.functions.DBHandle;
import com.stellaris.manager.UsrManager;
import com.stellaris.model.Event;
import com.stellaris.practice.AddEventActivity;
import com.stellaris.practice.R;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<Event> mEvents = new ArrayList<Event>();
    Context context;
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;

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
        public String eventId;
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

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case MsgStatus.POST_DEL_SUCCESS:
                    Log.d("EventAdapter",String.valueOf(getItemCount()));
                    Log.d("EventAdapter",mEvents.toString());
                    mEvents.remove((int)message.obj);
                    Log.d("EventAdapter",String.valueOf(getItemCount()));
                    Log.d("EventAdapter",mEvents.toString());
                    Toast.makeText(context,"事件删除成功",Toast.LENGTH_SHORT).show();
                    break;
                case MsgStatus.POST_DEL_FAIL:
                    Toast.makeText(context,"事件删除失败",Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        EventViewHolder eventholder = (EventViewHolder) holder;
        final Event event = getItem(position);

        //填充数据
        eventholder.text_title.setText(event.getTitle());
        String dors = event.getRoomId().replaceAll("[\\[\\]\"]","");
        eventholder.text_dors.setText(dors.isEmpty()?"所有宿舍":dors);
        eventholder.text_date.setText(event.getDate().substring(0,19));
        eventholder.text_tag.setText(event.getType());
        eventholder.text_note.setText(event.getNote().isEmpty()?"请继续努力！":event.getNote());
        eventholder.eventId = event.getId();
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
                if(UsrManager.getIdentity().equals(DBKeys.USR_IDENT_AYI)){
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
                                                            boolean res = DBHandle.delEvent(eventholder.eventId);
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
