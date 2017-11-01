package com.henghao.hhworkpresent.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.benefit.buy.library.phoneview.MultiImageSelectorActivity;
import com.benefit.buy.library.utils.tools.ToolsKit;
import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.adapter.JoinPeopleListAdapter;
import com.henghao.hhworkpresent.entity.MeetingDataBean;
import com.henghao.hhworkpresent.utils.SqliteDBUtils;
import com.henghao.hhworkpresent.views.CustomDialog;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 会议上传界面
 * Created by ASUS on 2017/9/28.
 */

public class MeetingUploadActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.tv_meeting_upload_theme)
    private TextView tv_meeting_upload_theme;

    @ViewInject(R.id.tv_meeting_upload_people)
    private TextView tv_meeting_upload_people;

    @ViewInject(R.id.tv_meeting_upload_start_time)
    private TextView tv_meeting_upload_start_time;

    @ViewInject(R.id.tv_meeting_upload_duration)
    private TextView tv_meeting_upload_duration;

    @ViewInject(R.id.tv_meeting_upload_place)
    private TextView tv_meeting_upload_place;

    @ViewInject(R.id.tv_meeting_join_people)
    private TextView tv_meeting_join_people;

    @ViewInject(R.id.tv_meeting_upload_qiandao_people)
    private TextView tv_meeting_upload_qiandao_people;

    @ViewInject(R.id.et_meeting_upload_content)
    private EditText et_meeting_upload_content;

    @ViewInject(R.id.et_meeting_upload_summary)
    private EditText et_meeting_upload_summary;

    @ViewInject(R.id.upload_meeting_picture_gridview)
    private GridView upload_meeting_picture_gridview;

    @ViewInject(R.id.tv_meeting_upload_save)
    private TextView tv_meeting_upload_save;

    @ViewInject(R.id.tv_meeting_upload_cancel)
    private TextView tv_meeting_upload_cancel;

    private GridAdapter adapter;

    private ArrayList<String> mSelectPath;

    private static final int REQUEST_IMAGE = 0x00;

    private ArrayList<String> mImageList=new ArrayList<>();

    public static ArrayList<File> mMeetingFileList = new ArrayList<>();//被选中隐患的图片文件

    private JoinPeopleListAdapter joinPeopleListAdapter;

    private List<String> joinPeopleList;      //应该参会的人员列表

    private List<String> mSelectPeopleList;     //被选中的参会人员列表

    public Handler mHandler = new Handler(){};

    private int mid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_meeting_upload);
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
        initWithCenterBar();
        mCenterTextView.setText("会议上传");
        mCenterTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        super.initData();
        SqliteDBUtils sqliteDBUtils = new SqliteDBUtils(this);
        Intent data = getIntent();
        mid = Integer.parseInt(data.getStringExtra("mid"));
        tv_meeting_upload_theme.setText(data.getStringExtra("meetingTheme"));
        tv_meeting_upload_people.setText(sqliteDBUtils.getLoginFirstName()+sqliteDBUtils.getLoginGiveName());
        tv_meeting_upload_start_time.setText(data.getStringExtra("meetingStartTime"));
        tv_meeting_upload_duration.setText(data.getStringExtra("meetingDuration"));
        tv_meeting_upload_place.setText(data.getStringExtra("meetingPlace"));
        tv_meeting_join_people.setText(data.getStringExtra("meetingJoinPeople"));

        joinPeopleList = new ArrayList<>();
        String[] joinArr = data.getStringExtra("meetingJoinPeople").split(",");
        for (String name : joinArr) {
            joinPeopleList.add(name);
        }
        mSelectPeopleList = new ArrayList<>();

        adapter = new GridAdapter();
        upload_meeting_picture_gridview.setAdapter(adapter);
    }

    @OnClick({R.id.tv_meeting_upload_qiandao_people,R.id.tv_meeting_upload_save,R.id.tv_meeting_upload_cancel})
    public void viewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_meeting_upload_qiandao_people:     //选择到场人员
                chooseQiandaoPeople();
                break;
            case R.id.tv_meeting_upload_save:
                saveCheckMeetingUploadDataToSerivce();
                finish();
                break;
            case R.id.tv_meeting_upload_cancel:
                finish();
                break;
        }
    }

    public void saveCheckMeetingUploadDataToSerivce(){
        String meetingUploadTheme = tv_meeting_upload_theme.getText().toString();
        String meetingUploadFaqiren = tv_meeting_upload_people.getText().toString();
        String meetingUploadStartTime = tv_meeting_upload_start_time.getText().toString();
        String meetingUploadDuration = tv_meeting_upload_duration.getText().toString();
        String meetingUploadPlace = tv_meeting_upload_place.getText().toString();
        String meetingUploadJoinPeople = tv_meeting_join_people.getText().toString();
        String meetingUploadQiandaoPeople = tv_meeting_upload_qiandao_people.getText().toString();
        String meetingUploadContent = et_meeting_upload_content.getText().toString();
        String meetingUploadSummary = et_meeting_upload_summary.getText().toString();
        if(meetingUploadQiandaoPeople.equals("")){
            Toast.makeText(this,"必须选择实际到场人员",Toast.LENGTH_SHORT).show();
            return;
        }
        if(meetingUploadContent.equals("")){
            Toast.makeText(this,"必须填写会议内容",Toast.LENGTH_SHORT).show();
            return;
        }
        if(meetingUploadSummary.equals("")){
            Toast.makeText(this,"必须填写会议总结",Toast.LENGTH_SHORT).show();
            return;
        }
        MeetingDataBean.MeetingUploadEntity meetingUploadEntity = new MeetingDataBean.MeetingUploadEntity();
        meetingUploadEntity.setMid(mid);
        meetingUploadEntity.setMeetingUploadTheme(meetingUploadTheme);
        meetingUploadEntity.setMeetingUploadPeople(meetingUploadFaqiren);
        meetingUploadEntity.setMeetingUploadStartTime(meetingUploadStartTime);
        meetingUploadEntity.setMeetingUploadDuration(meetingUploadDuration);
        meetingUploadEntity.setMeetingUploadPlace(meetingUploadPlace);
        meetingUploadEntity.setMeetingUploadJoinPeople(meetingUploadJoinPeople);
        meetingUploadEntity.setMeetingUploadQiandaoPeople(meetingUploadQiandaoPeople);
        meetingUploadEntity.setMeetingUploadContent(meetingUploadContent);
        meetingUploadEntity.setMeetingUploadSummary(meetingUploadSummary);
        if(mSelectPath!=null){
            String meetingUploadImagePath = listToString(mSelectPath,';');
            meetingUploadEntity.setMeetingUploadImagePath(meetingUploadImagePath);
        }

        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        multipartBuilder.type(MultipartBuilder.FORM)
                .addFormDataPart("json", com.alibaba.fastjson.JSONObject.toJSONString(meetingUploadEntity));//json数据
        for (File file : mMeetingFileList) {
            //上传现场图片
            multipartBuilder.addFormDataPart("files", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"),file));
        }
        RequestBody requestBody = multipartBuilder.build();
        String requestUrl = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_ADD_MEETING_UPLOAD;
        Request request = builder.post(requestBody).url(requestUrl).build();
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
                        msg("数据上传成功！");
                    }
                });
            }
        });

    }

    /**
     * 选择实际到场人员
     */
    public void chooseQiandaoPeople(){
        View customView = View.inflate(this,R.layout.layout_choose_qiandao_people,null);
        ListView personal_listview = (ListView) customView.findViewById(R.id.personal_listview);
        joinPeopleListAdapter = new JoinPeopleListAdapter(this,joinPeopleList);
        personal_listview.setAdapter(joinPeopleListAdapter);
        joinPeopleListAdapter.notifyDataSetChanged();

        personal_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JoinPeopleListAdapter.HodlerView holder = (JoinPeopleListAdapter.HodlerView) view.getTag();
                joinPeopleListAdapter.getIsSelected().put(position,true);
                holder.personal_checkbox.toggle();
                joinPeopleListAdapter.getIsSelected().put(position, holder.personal_checkbox.isChecked());
            }
        });
        CustomDialog.Builder dialog=new CustomDialog.Builder(this);
        dialog.setTitle("选择实际到场人员")
                .setContentView(customView)//设置自定义customView
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        //得到参选人的集合
                        HashMap<Integer, Boolean> isSelected = JoinPeopleListAdapter.getIsSelected();
                        for(int j=0;j<joinPeopleList.size();j++){
                            if(isSelected.get(j)){      //如果被选中
                                mSelectPeopleList.add(joinPeopleList.get(j));
                            }
                        }
                        StringBuilder stringBuilder = new StringBuilder();
                        for(String peopleName : mSelectPeopleList){
                            stringBuilder.append(peopleName+",");
                            tv_meeting_upload_qiandao_people.setText(stringBuilder);
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();

    }


    public void choosePicture(){
        int selectedMode = MultiImageSelectorActivity.MODE_MULTI;
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
                        mMeetingFileList.clear();
                        for (String filePath : mSelectPath) {  //mSelectPath是被选中的所有图片路径集合
                            File file = new File(filePath);
                            mMeetingFileList.add(file);  //mFileList用来存储被选中的图片文件，不是路径
                            if(!mImageList.contains(filePath)){
                                mImageList.add(filePath);
                                upload_meeting_picture_gridview.setAdapter(adapter);
                            }
                        }
                    }
                }
            }
        }
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
                        upload_meeting_picture_gridview.setAdapter(adapter);
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
