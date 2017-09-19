package com.henghao.hhworkpresent.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.entity.SaveCheckTaskEntity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 查看隐患信息界面
 * Created by ASUS on 2017/9/13.
 */

public class QueryYinhuanInfoActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.tv_query_check_time)
    private TextView tv_query_check_time;

    @ViewInject(R.id.tv_query_check_degree)
    private TextView tv_query_check_degree;

    @ViewInject(R.id.tv_query_check_position)
    private TextView tv_query_check_position;

    @ViewInject(R.id.tv_query_check_description)
    private TextView tv_query_check_description;

    @ViewInject(R.id.query_check_image_gridview)
    private GridView query_check_image_gridview;

    @ViewInject(R.id.tv_query_check_ok)
    private TextView tv_query_check_ok;

    private SaveCheckTaskEntity.JianchaMaterialEntityListBean jianchaMaterialEntity;

    private ImageLoader imageLoader;

    private DisplayImageOptions options;

    private List<String> mSelectPath = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_query_yinhuan_info);
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
        mLeftImageView.setVisibility(View.VISIBLE);
        initWithCenterBar();
        mCenterTextView.setText("查看隐患信息");
        mCenterTextView.setVisibility(View.VISIBLE);

        tv_query_check_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        jianchaMaterialEntity = (SaveCheckTaskEntity.JianchaMaterialEntityListBean) getIntent().getSerializableExtra("JianchaMaterialEntity");
        tv_query_check_time.setText(jianchaMaterialEntity.getFindTime());
        tv_query_check_degree.setText(jianchaMaterialEntity.getCheckDegree());
        tv_query_check_description.setText(jianchaMaterialEntity.getCheckDescript());
        tv_query_check_position.setText(jianchaMaterialEntity.getCheckPosition());
        String imagePath = jianchaMaterialEntity.getSelectImagePath();
        if(imagePath!=null){
            String[] imagePathArr = imagePath.split(",");
            mSelectPath = Arrays.asList(imagePathArr);
            GridAdapter adapter = new GridAdapter();
            query_check_image_gridview.setAdapter(adapter);
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
            imageLoader.displayImage("http://172.16.0.81:8080/image/" + imageUrl[position], holder.image, options);
            return convertView;
        }
    }

    public class ViewHolder {
        public ImageView image;
        public Button btn ;
    }
}
