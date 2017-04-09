package com.henghao.hhworkpresent.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.benefit.buy.library.phoneview.MultiImageSelectorActivity;
import com.benefit.buy.library.utils.tools.ToolsKit;
import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.views.DatabaseHelper;
import com.henghao.hhworkpresent.views.MySlideButtonView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
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
import java.util.ArrayList;
import java.util.List;


/**
 * Created by bryanrady on 2017/3/14.
 * 发送公告下一步界面
 */

public class SendGonggaoNextActivity extends ActivityFragmentSupport{

    private MySlideButtonView slideButtonView;

  //  private ImageView image_addPicture;

    @ViewInject(R.id.add_picture_gridView)
    private GridView gridView;

    private ArrayList<String> mSelectPath;

    private static final int REQUEST_IMAGE = 0x00;

    private static final int REQUEST_CONTACTS = 0x01;

    private ArrayList<String> mImageList=new ArrayList<>();

    private GridAdapter adapter;

    @ViewInject(R.id.gonggao_linearLayout)
    private LinearLayout ll;

    @ViewInject(R.id.gonggao_tv_peopleNum)
    private TextView propleNumTV;

    /**记录要发送的人数*//*
    private String peopleNum;

    *//**判断是否为保密公告*//*
    private String gonggao_isEncrypt;*/

    private ArrayList<File> mFileList = new ArrayList<>();//被选中的公告封面图片文件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_sendgonggaonext);
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
        mLeftTextView.setText("发公告");
        mLeftTextView.setVisibility(View.VISIBLE);
        initWithRightBar();
        mRightTextView.setVisibility(View.VISIBLE);
        mRightTextView.setText("发布");
        mRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryAllUserID();
            }
        });
    }

    /**
     * 从本地数据库读取登录用户Id 用来作为数据请求id
     * @return
     */
    public String getLoginUid(){
        DatabaseHelper dbHelper = new DatabaseHelper(this,"user_login.db");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("user",new String[]{"uid"},null,null,null,null,null);
        String uid = null;
        while (cursor.moveToNext()){
            uid = cursor.getString((cursor.getColumnIndex("uid")));
        }
        return uid;
    }

    /**
     * 查询所有用户id
     */
    public void queryAllUserID(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        RequestBody requestBody = requestBodyBuilder.build();
        String request_url = ProtocolUrl.ROOT_URL + "/"+ ProtocolUrl.APP_GET_ALL_UERID;
        Request request = builder.url(request_url).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Toast.makeText(getContext(), "网络访问错误！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result_str = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result_str);
                    int status = jsonObject.getInt("status");
                    if(status == 0){
                        String uids = jsonObject.getString("data");
                        Intent intent = getIntent();
                        String gonggao_title = intent.getStringExtra("gonggao_title");
                        String gonggao_author = intent.getStringExtra("gonggao_author");
                        String gonggao_content = intent.getStringExtra("gonggao_content");
                        String gonggao_imageUrl = mSelectPath.get(0);

                        OkHttpClient okHttpClient = new OkHttpClient();
                        Request.Builder builder = new Request.Builder();
                        MultipartBuilder multipartBuilder = new MultipartBuilder();
                        multipartBuilder.type(MultipartBuilder.FORM)//
                                .addFormDataPart("uids", uids)//要发送给所有人的用户id
                                .addFormDataPart("gonggao_title",gonggao_title)//公告标题
                                .addFormDataPart("gonggao_author", gonggao_author)//公告作者
                                .addFormDataPart("gonggao_content", gonggao_content)//公告内容
                                .addFormDataPart("gonggao_imageUrl", gonggao_imageUrl);//公告图片Url

                        for (File file : mFileList) {
                            multipartBuilder.addFormDataPart("imageFile", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
                        }
                        RequestBody requestBody = multipartBuilder.build();
                        Request request = builder.post(requestBody).url(ProtocolUrl.ROOT_URL + "/" + ProtocolUrl.APP_SEND_GONGGAO).build();
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
                                finish();
                            }

                            @Override
                            public void onResponse(Response response) throws IOException {
                                Intent intent = new Intent();
                                intent.setClass(SendGonggaoNextActivity.this,GongGaoActivity.class);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SendGonggaoNextActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick({R.id.gonggao_linearLayout})
    private void viewOnClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.gonggao_linearLayout:
                intent.setClass(SendGonggaoNextActivity.this,ChooseContactsActivity.class);
                startActivityForResult(intent,REQUEST_CONTACTS);
                break;
        }
    }
    @Override
    public void initData() {
        super.initData();
        /*slideButtonView = (MySlideButtonView) findViewById(R.id.slideButton);
        slideButtonView.setOnStateChangedListener(new MySlideButtonView.OnStateChangedListener() {
            @Override
            public void onStateChanged(boolean state) {
                if(true == state)
                {
                    gonggao_isEncrypt = "1";
                    Toast.makeText(SendGonggaoNextActivity.this, "此公告已被设为保密公告，如想更改，可点击开关按钮关闭", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    gonggao_isEncrypt = "0";
              //      createDialog();
                }

            }
        });
*/
        adapter = new GridAdapter();
        gridView.setAdapter(adapter);
    }

    public void choosePicture(){
        //设置单选模式
        int selectedMode = MultiImageSelectorActivity.MODE_SINGLE;
        int maxNum = 9;
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
            } /*else if (requestCode ==REQUEST_CONTACTS){
                if ((resultCode == Activity.RESULT_OK) || (resultCode == Activity.RESULT_CANCELED)) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        peopleNum = bundle.getString("key");// 获取数据
                        propleNumTV.setText(peopleNum+"人");
                    }
                }
            }*/
        }
    }

    private String getImageName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    /**
     * 创建对话框
     */
    public void createDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置对话框图标，可以使用自己的图片，Android本身也提供了一些图标供我们使用

        // builder.setIcon(android.R.drawable.ic_dialog_alert);

        //设置对话框标题

       // builder.setTitle("放弃输入");

        //设置对话框内的文本

        builder.setMessage("关闭保密公告，该条公告员工将可以分享给其他人，是否继续？");

        //设置确定按钮，并给按钮设置一个点击侦听，注意这个OnClickListener使用的是DialogInterface类里的一个内部接口

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {
                // 执行点击确定按钮的业务逻辑
                dialog.dismiss();
            }
        });

        //设置取消按钮

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {

                // 执行点击取消按钮的业务逻辑
                dialog.dismiss();
            }
        });

        //使用builder创建出对话框对象

        AlertDialog dialog = builder.create();

        //显示对话框
        dialog.show();

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

            SendGonggaoNextActivity.ViewHolder holder = null ;
            if (convertView == null) {
                holder = new SendGonggaoNextActivity.ViewHolder();
                convertView = layoutInflater.inflate(R.layout.gridview_picture_item, null);
                holder.image = (ImageView) convertView.findViewById(R.id.imageView);
                holder.btn = (Button) convertView.findViewById(R.id.delete);

                holder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);

                convertView.setTag(holder);
            }
            else{
                holder = (SendGonggaoNextActivity.ViewHolder) convertView.getTag();
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
                Bitmap bm = BitmapFactory.decodeFile(mSelectPath.get(position));
                //将图片显示到ImageView中
                holder.image.setImageBitmap(bm);
                holder.btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点击后移除图片
                        mImageList.remove(position);

                        //更新UI
                        gridView.setAdapter(adapter);
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
