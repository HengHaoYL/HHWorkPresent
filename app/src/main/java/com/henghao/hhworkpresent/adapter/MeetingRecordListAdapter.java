package com.henghao.hhworkpresent.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.henghao.hhworkpresent.entity.MeetingUploadEntity;

import java.util.List;

/**
 * 会议记录列表适配器
 * Created by bryanrady on 2017/10/16.
 */

public class MeetingRecordListAdapter extends BaseAdapter {

    public Context mContext;
    public List<MeetingUploadEntity> mList;

    public MeetingRecordListAdapter(Context context, List<MeetingUploadEntity> list){
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
        /*if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.listview_meeting_pass_item, null);
            viewHolder = new ViewHolder();
            viewHolder.message_image=(ImageView) convertView.findViewById(R.id.message_image);
            viewHolder.tv_meeting_title=(TextView) convertView.findViewById(R.id.tv_meeting_title);
            viewHolder.tv_meeting_start_time=(TextView) convertView.findViewById(R.id.tv_meeting_start_time);
            viewHolder.tv_meeting_join_people=(TextView) convertView.findViewById(R.id.tv_meeting_join_people);
            viewHolder.btn_meeting_upload=(Button) convertView.findViewById(R.id.btn_meeting_upload);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_meeting_title.setText(mList.get(arg0).getMeetingTheme());
        viewHolder.tv_meeting_start_time.setText(mList.get(arg0).getMeetingStartTime());
        viewHolder.tv_meeting_join_people.setText(mList.get(arg0).getUserIds());
        viewHolder.btn_meeting_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, MeetingUploadActivity.class);
                intent.putExtra("meetingTheme",mList.get(arg0).getMeetingTheme());
                intent.putExtra("meetingStartTime",mList.get(arg0).getMeetingStartTime());
                intent.putExtra("meetingDuration",mList.get(arg0).getMeetingDuration());
                intent.putExtra("meetingPlace",mList.get(arg0).getMeetingPlace());
                intent.putExtra("meetingJoinPeople",mList.get(arg0).getUserIds());
                mContext.startActivity(intent);
            }
        });*/
        return convertView;
    }

    class ViewHolder{
        ImageView message_image;
        TextView tv_meeting_title;
        TextView tv_meeting_start_time;
        TextView tv_meeting_join_people;
        Button btn_meeting_upload;
    }
}
