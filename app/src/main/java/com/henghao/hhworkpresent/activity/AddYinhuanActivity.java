package com.henghao.hhworkpresent.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.benefit.buy.library.phoneview.MultiImageSelectorActivity;
import com.benefit.buy.library.utils.tools.ToolsKit;
import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.entity.SaveCheckTaskEntity;
import com.henghao.hhworkpresent.utils.AnimationUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 新增隐患界面
 * Created by ASUS on 2017/9/4.
 */

public class AddYinhuanActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.tv_addyinhuan_time)
    private TextView tv_addyinhuan_time;

    @ViewInject(R.id.tv_addyinhuan_degree)
    private TextView tv_addyinhuan_degree;

    @ViewInject(R.id.et_addyinhuan_position)
    private EditText et_addyinhuan_position;

    @ViewInject(R.id.et_addyinhuan_description)
    private EditText et_addyinhuan_description;

    @ViewInject(R.id.tv_addyinhuan_save)
    private TextView tv_addyinhuan_save;

    @ViewInject(R.id.tv_addyinhuan_cancel)
    private TextView tv_addyinhuan_cancel;

    @ViewInject(R.id.gray_layout)
    private View mGrayLayout;

    @ViewInject(R.id.add_yinhuan_picture_gridView)
    private GridView add_yinhuan_picture_gridView;

    private GridAdapter adapter;

    private ArrayList<String> mSelectPath;

    private static final int REQUEST_IMAGE = 0x00;

    private ArrayList<String> mImageList=new ArrayList<>();

    public static ArrayList<File> mYinhuanFileList = new ArrayList<>();//被选中隐患的图片文件

    public Handler mHandler = new Handler(){};

    private PopupWindow mPopupWindow;
    private boolean isPopWindowShowing = false;
    int fromYDelta;

    private SaveCheckTaskEntity.JianchaMaterialEntityListBean jianchaMaterialEntityListBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_add_yinhuan);
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
        mCenterTextView.setText("新增隐患");
        mCenterTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        super.initData();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        tv_addyinhuan_time.setText(format.format(new Date()));

        adapter = new GridAdapter();
        add_yinhuan_picture_gridView.setAdapter(adapter);

        jianchaMaterialEntityListBean = (SaveCheckTaskEntity.JianchaMaterialEntityListBean)getIntent().getSerializableExtra("JianchaMaterialEntityListBean");
    }

    @OnClick({R.id.tv_addyinhuan_degree,R.id.tv_addyinhuan_save,R.id.tv_addyinhuan_cancel})
    public void viewClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.tv_addyinhuan_degree:
                if(isPopWindowShowing){
                    mPopupWindow.getContentView().startAnimation(AnimationUtil.createOutAnimation(this, fromYDelta));
                    mPopupWindow.getContentView().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mPopupWindow.dismiss();
                        }
                    },AnimationUtil.ANIMATION_OUT_TIME);
                }else{
                    showPopupWindow();
                }
                break;
            case R.id.tv_addyinhuan_save:           //保存操作
                saveCheckYinhuanDataToSerivce();
                finish();
                break;
            case R.id.tv_addyinhuan_cancel:
                finish();
                break;
        }
    }


    /**
     * 保存数据到 多张隐患图片到服务器  一个隐患问题可能对应多张图片 或没有图片
     */
    public void saveCheckYinhuanDataToSerivce(){
        jianchaMaterialEntityListBean.setFindTime(tv_addyinhuan_time.getText().toString());
        jianchaMaterialEntityListBean.setCheckDegree(tv_addyinhuan_degree.getText().toString());
        jianchaMaterialEntityListBean.setCheckPosition(et_addyinhuan_position.getText().toString());
        jianchaMaterialEntityListBean.setCheckDescript(et_addyinhuan_description.getText().toString());
        if(mSelectPath!=null){
            jianchaMaterialEntityListBean.setSelectImagePath(listToString(mSelectPath,';'));    //以分隔符把图片路径拼接成为string
        }else{
            jianchaMaterialEntityListBean.setSelectImagePath("");
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        //这个要和服务器保持一致 application/json;charset=UTF-8
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        multipartBuilder.type(MultipartBuilder.FORM)
                .addFormDataPart("pid",String.valueOf(WoyaoJianchaActivity.Pid))  //pid
                .addFormDataPart("json", com.alibaba.fastjson.JSONObject.toJSONString(jianchaMaterialEntityListBean));//json数据
        for (File file : mYinhuanFileList) {
            //上传现场图片
            multipartBuilder.addFormDataPart("files", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"),file));
        }
        RequestBody requestBody = multipartBuilder.build();
        Request request = builder.post(requestBody).url("http://172.16.0.81:8080/istration/enforceapp/updatecheckcontent").build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e){
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        msg("网络请求错误！");
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        msg("隐患数据保存成功！");
                    }
                });
            }
        });
    }

    /**
     * list转String字符串
     * @param list
     * @param separator
     * @return
     */
    public String listToString(ArrayList<String> list, char separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(separator);
        }
        return sb.toString().substring(0,sb.toString().length()-1);}

    public void showPopupWindow(){
        final View contentView= LayoutInflater.from(this).inflate(R.layout.selectlist_degree,null);
        final TextView t1= (TextView) contentView.findViewById(R.id.text1);
        final TextView t2= (TextView) contentView.findViewById(R.id.text2);
        mPopupWindow=new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        //将这两个属性设置为false，使点击popupwindow外面其他地方不会消失
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setFocusable(false);
        mGrayLayout.setVisibility(View.VISIBLE);
        //获取popupwindow高度确定动画开始位置
        int contentHeight= com.henghao.hhworkpresent.utils.ViewUtils.getViewMeasuredHeight(contentView);
        mPopupWindow.showAsDropDown(tv_addyinhuan_degree);
        fromYDelta=-contentHeight - 50;
        mPopupWindow.getContentView().startAnimation(AnimationUtil.createInAnimation(this, fromYDelta));

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isPopWindowShowing=false;
                mGrayLayout.setVisibility(View.GONE);
            }
        });

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_addyinhuan_degree.setText(t1.getText().toString());
                mPopupWindow.dismiss();
            }
        });

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_addyinhuan_degree.setText(t2.getText().toString());
                mPopupWindow.dismiss();
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
                        mImageList.clear();
                        mYinhuanFileList.clear();
                        for (String filePath : mSelectPath) {  //mSelectPath是被选中的所有图片路径集合
                            File file = new File(filePath);
                            mYinhuanFileList.add(file);  //mFileList用来存储被选中的图片文件，不是路径
                            if(!mImageList.contains(filePath)){
                                mImageList.add(filePath);
                                add_yinhuan_picture_gridView.setAdapter(adapter);
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
                holder.btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点击后移除图片
                        mImageList.remove(position);
                        mSelectPath.remove(position);

                        //更新UI
                        add_yinhuan_picture_gridView.setAdapter(adapter);
                    }
                });

            }
            return convertView;
        }
    }

    public class ViewHolder {
        public ImageView image;
        public Button btn ;
    }

}
