package com.henghao.hhworkpresent.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.benefit.buy.library.phoneview.MultiImageSelectorActivity;
import com.benefit.buy.library.utils.tools.ToolsKit;
import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.utils.LocationUtils;
import com.henghao.hhworkpresent.utils.SqliteDBUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by bryanrady on 2017/7/18.
 */

public class AddTrajectoryActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.et_tarjectory_event)
    private EditText et_tarjectory_event;

    @ViewInject(R.id.tv_tarjectory_time)
    private TextView tv_tarjectory_time;

    @ViewInject(R.id.tv_tarjectory_place)
    private TextView tv_tarjectory_place;

    @ViewInject(R.id.trajectory_add_picture_gridView)
    private GridView gridView;

    private GridAdapter adapter;

    private ArrayList<String> mSelectPath;

    private static final int REQUEST_IMAGE = 0x00;

    private static final int REQUEST_CONTACTS = 0x01;

    private ArrayList<String> mImageList=new ArrayList<>();

    private ArrayList<File> mFileList = new ArrayList<>();//被选中的图片文件

    public Handler mHandler = new Handler(){};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        SDKInitializer.initialize(getApplicationContext());
        LocationUtils.Location(getApplicationContext());
        this.mActivityFragmentView.viewMain(R.layout.activity_add_trajectory);
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
        mLeftTextView.setText("添加轨迹");
        mLeftTextView.setVisibility(View.VISIBLE);
        initWithRightBar();
        mRightTextView.setText("完成");
        mRightTextView.setVisibility(View.VISIBLE);
        mRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadWorkTrajectory();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        adapter = new GridAdapter();
        gridView.setAdapter(adapter);

        tv_tarjectory_place.setText(LocationUtils.getAddress());
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        tv_tarjectory_time.setText(simpleDateFormat.format(date));

    }

    /**
     * 上传工作轨迹到服务器
     */
    private void uploadWorkTrajectory(){
        SqliteDBUtils sqliteDBUtils = new SqliteDBUtils(this);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(300, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(300,TimeUnit.SECONDS);
        Request.Builder builder = new Request.Builder();
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        if(et_tarjectory_event.getText().toString().trim()==null){
            Toast.makeText(this,"事件不能为空！",Toast.LENGTH_SHORT).show();
            return;
        }
        multipartBuilder.type(MultipartBuilder.FORM)
                .addFormDataPart("userId", sqliteDBUtils.getLoginUid())
                .addFormDataPart("eventDate",simpleDateFormat.format(new Date()))
                .addFormDataPart("eventName",et_tarjectory_event.getText().toString())
                .addFormDataPart("eventTime",tv_tarjectory_time.getText().toString())
                .addFormDataPart("eventAddress",tv_tarjectory_place.getText().toString());
        for (File file : mFileList) {
            /*for(String filePath : mSelectPath) {
                Bitmap bm = BitmapFactory.decodeFile(filePath);
                File outputFile=new File(filePath);
                try {
                    if (!outputFile.exists()) {
                        outputFile.getParentFile().mkdirs();
                    }else{
                        outputFile.delete();
                    }
                    FileOutputStream out = new FileOutputStream(outputFile);
                    bm.compress(Bitmap.CompressFormat.JPEG, 20, out);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }*/
            multipartBuilder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
        }
        Log.d("wangqingbin","循环后");
        RequestBody requestBody = multipartBuilder.build();
        Request request = builder.post(requestBody).url(ProtocolUrl.ROOT_URL + ProtocolUrl.APP_UPLOAD_WORK_TRAJECTORY).build();
        mActivityFragmentView.viewLoading(View.VISIBLE);
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mActivityFragmentView.viewLoading(View.GONE);
                        msg("网络请求错误！");
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String content = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mActivityFragmentView.viewLoading(View.GONE);
                            msg("添加轨迹成功");
                            finish();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void choosePicture(){
        //设置单选模式
        int selectedMode = MultiImageSelectorActivity.MODE_MULTI;
        //设置最大选择张数
        int maxNum = 6;
        Intent picIntent = new Intent(this, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        picIntent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量
        picIntent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxNum);
        // 选择模式
        picIntent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectedMode);
        // 默认选择
        if ((this.mSelectPath != null) && (this.mSelectPath.size() > 0)) {
            picIntent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, this.mSelectPath);
        }
        startActivityForResult(picIntent, REQUEST_IMAGE);
    }

    @SuppressWarnings("static-access")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == REQUEST_IMAGE) {
                if ((resultCode == Activity.RESULT_OK) || (resultCode == Activity.RESULT_CANCELED)) {
                    this.mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    if (!ToolsKit.isEmpty(this.mSelectPath)) {
                        List<String> fileNames = new ArrayList<>();
                        mImageList.clear();
                        mFileList.clear();
                        for (String filePath : mSelectPath) {
                            String imageName = getImageName(filePath);
                            fileNames.add(imageName);
                            File file = new File(filePath);
                            mFileList.add(file);
                            if(!mImageList.contains(filePath)){
                                mImageList.add(filePath);
                                gridView.setAdapter(adapter);
                            }
                        }
                    }
                }
            }
        }
    }

    private String getImageName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    class GridAdapter extends BaseAdapter {

        public LayoutInflater layoutInflater = LayoutInflater.from(context);
        @Override
        public int getCount() {
            return mImageList.size()+ 1;

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
            if (position == mImageList.size()) {

                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        context.getResources(), R.drawable.item_add_picture));
                holder.btn.setVisibility(View.GONE);

                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        choosePicture();
                    }
                });
            } else {
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
                /*holder.btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点击后移除图片
                        mImageList.remove(position);
                        mSelectPath.remove(position);

                        //更新UI
                        gridView.setAdapter(adapter);
                    }
                });*/

            }
            return convertView;
        }
    }

    public class ViewHolder {
        public ImageView image;
        public Button btn ;
    }
}
