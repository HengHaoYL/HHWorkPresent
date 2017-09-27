package com.henghao.hhworkpresent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.entity.JPushToUser;

import java.util.List;

/**
 * Created by ASUS on 2017/9/27.
 */

public class MeetingMessageListAdapter extends BaseAdapter {

    public Context mContext;
    public List<JPushToUser> mList;

    public MeetingMessageListAdapter(Context context, List<JPushToUser> list){
        super();
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return mList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(final int arg0, View convertView, ViewGroup arg2) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.listview_meeting_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_meeting_message_title=(TextView) convertView.findViewById(R.id.tv_meeting_message_title);
            viewHolder.tv_meeting_message_content=(TextView) convertView.findViewById(R.id.tv_meeting_message_content);
            viewHolder.tv_message_time=(TextView) convertView.findViewById(R.id.tv_message_time);
            viewHolder.tv_meeting_message_faqiren=(TextView) convertView.findViewById(R.id.tv_meeting_message_faqiren);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_meeting_message_title.setText(mList.get(arg0).getMessageTitle());
        viewHolder.tv_meeting_message_content.setText(mList.get(arg0).getMessageContent());
        viewHolder.tv_message_time.setText(mList.get(arg0).getMessageSendTime());
        viewHolder.tv_meeting_message_faqiren.setText(mList.get(arg0).getMessageSendPeople());
        return convertView;
    }

    class ViewHolder{
        TextView tv_meeting_message_title;
        TextView tv_meeting_message_content;
        TextView tv_message_time;
        TextView tv_meeting_message_faqiren;
    }
}
