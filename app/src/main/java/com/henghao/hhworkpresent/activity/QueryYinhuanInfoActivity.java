package com.henghao.hhworkpresent.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
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
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.JianchaYinhuanListAdpter;
import com.henghao.hhworkpresent.entity.JianchaYinhuanEntity;
import com.henghao.hhworkpresent.views.YinhuanDatabaseHelper;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

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

    private JianchaYinhuanEntity jianchaYinhuanEntity;


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

    private List<String> mSelectPath = new ArrayList<>();
    @Override
    public void initData() {
        super.initData();
        jianchaYinhuanEntity = (JianchaYinhuanEntity) getIntent().getSerializableExtra("JianchaYinhuanEntity");
        tv_query_check_time.setText(jianchaYinhuanEntity.getThreat_time());
        tv_query_check_degree.setText(jianchaYinhuanEntity.getThreat_degree());
        tv_query_check_description.setText(jianchaYinhuanEntity.getThreat_description());
        tv_query_check_position.setText(jianchaYinhuanEntity.getThreat_position());
        String imagePath = jianchaYinhuanEntity.getThreat_imagepath();
        if(imagePath!=null){
            String[] imagePathArr = imagePath.split(";");
            mSelectPath = Arrays.asList(imagePathArr);
        }
        GridAdapter adapter = new GridAdapter();
        query_check_image_gridview.setAdapter(adapter);
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
            //做压缩处理后就不会爆出OOM异常
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bm = BitmapFactory.decodeFile(mSelectPath.get(position),options);
            // 获取到这个图片的原始宽度和高度  
            int picWidth = options.outWidth;
            int picHeight = options.outHeight;
            // 获取屏的宽度和高度  
            WindowManager windowManager = getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            int screenWidth = display.getWidth();
            int screenHeight = display.getHeight();
            // isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2  
            options.inSampleSize = 2;
            // 根据屏的大小和图片大小计算出缩放比例  
            if(picWidth > picHeight){
                if(picWidth > screenWidth)
                    options.inSampleSize = picWidth/screenWidth;
            }else{
                if (picHeight > screenHeight)
                    options.inSampleSize = picHeight/screenHeight;
            }
            // 这次再真正地生成一个有像素的，经过缩放了的bitmap  
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeFile(mSelectPath.get(position),options);
            //将图片显示到ImageView中
            holder.image.setImageBitmap(bm);
            return convertView;
        }
    }

    public class ViewHolder {
        public ImageView image;
        public Button btn ;
    }
}
