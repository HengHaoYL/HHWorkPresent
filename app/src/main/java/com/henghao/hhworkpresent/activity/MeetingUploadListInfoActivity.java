package com.henghao.hhworkpresent.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.entity.MeetingDataBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 会议上传记录的详细信息
 * Created by bryanrady on 2017/10/18.
 */

public class MeetingUploadListInfoActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.tv_meeting_upload_detail_theme)
    private TextView tv_meeting_upload_detail_theme;

    @ViewInject(R.id.tv_meeting_upload_detail_people)
    private TextView tv_meeting_upload_detail_people;

    @ViewInject(R.id.tv_meeting_upload_detail_start_time)
    private TextView tv_meeting_upload_detail_start_time;

    @ViewInject(R.id.tv_meeting_upload_detail_duration)
    private TextView tv_meeting_upload_detail_duration;

    @ViewInject(R.id.tv_meeting_upload_detail_place)
    private TextView tv_meeting_upload_detail_place;

    @ViewInject(R.id.tv_meeting_upload_detail_join_people)
    private TextView tv_meeting_upload_detail_join_people;

    @ViewInject(R.id.tv_meeting_upload_detail_qiandao_people)
    private TextView tv_meeting_upload_detail_qiandao_people;

    @ViewInject(R.id.tv_meeting_upload_detail_content)
    private TextView tv_meeting_upload_detail_content;

    @ViewInject(R.id.tv_meeting_upload_detail_summary)
    private TextView tv_meeting_upload_detail_summary;

    @ViewInject(R.id.upload_meeting_picture_gridview)
    private GridView upload_meeting_picture_gridview;

    private MeetingDataBean.MeetingUploadEntity meetingUploadEntity;

    private ImageLoader imageLoader;

    private DisplayImageOptions options;

    private List<String> mSelectPath = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_meeting_upload_detail);
        this.mActivityFragmentView.viewEmpty(R.layout.activity_empty);
        this.mActivityFragmentView.viewEmptyGone();
        this.mActivityFragmentView.viewLoading(View.GONE);
        this.mActivityFragmentView.clipToPadding(true);
        ViewUtils.inject(this, this.mActivityFragmentView);
        setContentView(this.mActivityFragmentView);
        initWidget();
        initData();
    }

    @Override
    public void initWidget() {
        super.initWidget();
        initWithBar();
        mLeftTextView.setVisibility(View.VISIBLE);
        mLeftTextView.setText("会议详情");

    }

    @Override
    public void initData() {
        super.initData();
        meetingUploadEntity = (MeetingDataBean.MeetingUploadEntity)getIntent().getSerializableExtra("meetingUploadEntity");
        tv_meeting_upload_detail_theme.setText(meetingUploadEntity.getMeetingUploadTheme());
        tv_meeting_upload_detail_people.setText(meetingUploadEntity.getMeetingUploadPeople());
        tv_meeting_upload_detail_start_time.setText(meetingUploadEntity.getMeetingUploadStartTime());
        tv_meeting_upload_detail_duration.setText(meetingUploadEntity.getMeetingUploadDuration());
        tv_meeting_upload_detail_place.setText(meetingUploadEntity.getMeetingUploadPlace());
        tv_meeting_upload_detail_join_people.setText(meetingUploadEntity.getMeetingUploadJoinPeople());
        tv_meeting_upload_detail_qiandao_people.setText(meetingUploadEntity.getMeetingUploadQiandaoPeople());
        tv_meeting_upload_detail_content.setText(meetingUploadEntity.getMeetingUploadContent());
        tv_meeting_upload_detail_summary.setText(meetingUploadEntity.getMeetingUploadSummary());
        String imagePath = meetingUploadEntity.getMeetingUploadImagePath();
        if(imagePath!=null){
            String[] imagePathArr = imagePath.split(",");
            mSelectPath = Arrays.asList(imagePathArr);
            GridAdapter adapter = new GridAdapter();
            upload_meeting_picture_gridview.setAdapter(adapter);
        }
    }

    class GridAdapter extends BaseAdapter {

        public LayoutInflater layoutInflater = LayoutInflater.from(context);
        @Override
        public int getCount() {
            return mSelectPath.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null ;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.gridview_picture_item, null);
                holder.image = (ImageView) convertView.findViewById(R.id.imageView);
                holder.btn = (Button) convertView.findViewById(R.id.delete);

                holder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);

                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder) convertView.getTag();
            }

            holder.btn.setVisibility(View.GONE);

            /**
             * 下载隐患图片
             */
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.icon_logo) // 设置图片下载期间显示的图片
                    .showImageForEmptyUri(R.drawable.ic_launcher) // 设置图片Uri为空或是错误的时候显示的图片
                    .showImageOnFail(R.drawable.ic_launcher) // 设置图片加载或解码过程中发生错误显示的图片
                    .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                    .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                    .build(); // 构建完成
            imageLoader = ImageLoader.getInstance();
            String[] imageUrl = mSelectPath.toArray(new String[mSelectPath.size()]);
            imageLoader.displayImage(ProtocolUrl.APP_QUERT_MEETING_UPLOAD_DETAIL_IMAGEPATH + imageUrl[position], holder.image, options);
            return convertView;
        }
    }

    public class ViewHolder {
        public ImageView image;
        public Button btn ;
    }
}
