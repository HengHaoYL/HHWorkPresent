package com.henghao.hhworkpresent.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.henghao.hhworkpresent.ActivityFragmentSupport;
import com.henghao.hhworkpresent.R;
import com.henghao.hhworkpresent.views.DatabaseHelper;
import com.henghao.hhworkpresent.views.ProgressWebView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 *
 * 发送公告界面
 * Created by bryanrady on 2017/3/13.
 */

public class SendGonggaoActivity extends ActivityFragmentSupport {

    @ViewInject(R.id.carapply_webview)
    private ProgressWebView progressWebView;

    private String requestUrl = "http://172.16.0.57:8080/hz7/horizon/basics/getBasics.wf?loginName=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_carapply);
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
    }

    @Override
    public void initData() {
        super.initData();
        init();
    }

    public String getUsername(){
        DatabaseHelper dbHelper = new DatabaseHelper(this,"user_login.db");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String username = null;
        Cursor cursor = db.query("user",new String[]{"username"},null,null,null,null,null);
        // 将光标移动到下一行，从而判断该结果集是否还有下一条数据，如果有则返回true，没有则返回false
        while (cursor.moveToNext()){
            username = cursor.getString((cursor.getColumnIndex("username")));
        }
        return username;
    }

    public void init() {
        WebSettings webSettings = progressWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//关闭WebView中缓存
        webSettings.setDomStorageEnabled(true);  // 开启 DOM storage API 功能
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setAllowFileAccess(true);  // 可以读取文件缓存(manifest生效)
        progressWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                System.out.println("Page开始  " + url + "   " + favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                System.out.println("Page结束  " + url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //return super.shouldOverrideUrlLoading(view, url);
                view.loadUrl(url);
                return true;
            }
        });
        progressWebView.loadUrl(requestUrl+getUsername());
    }

   /* private TabHost tabHost;

    @ViewInject(R.id.gonggao_et_title)
    private EditText etTitle;

    @ViewInject(R.id.gonggao_et_author)
    private EditText etAuthor;

    @ViewInject(R.id.gonggao_et_content)
    private EditText etContent;

    @ViewInject(R.id.gonggao_img_choose)
    private ImageView imageChoose;

    private ArrayList<String> mSelectPath;

    private static final int REQUEST_IMAGE = 0x00;

    private ArrayList<File> mFileList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mActivityFragmentView.viewMain(R.layout.activity_sendgonggao);
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
        mLeftImageView.setVisibility(View.VISIBLE);
        mLeftImageView.setImageResource(R.drawable.icon_close);
        mLeftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteJiemian();
            }
        });
        mLeftTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteJiemian();
            }
        });
        initWithRightBar();
        mRightTextView.setVisibility(View.VISIBLE);
        mRightTextView.setText("下一步");
        mRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etTitle.getText().toString().trim().equals("") && !etContent.getText().toString().trim().equals("") ){
                    Intent intent = new Intent();
                    intent.putExtra("gonggao_title",etTitle.getText().toString().trim());
                    intent.putExtra("gonggao_author",etAuthor.getText().toString().trim());
                    intent.putExtra("gonggao_content",etContent.getText().toString().trim());
                    intent.setClass(SendGonggaoActivity.this, SendGonggaoNextActivity.class);
                    startActivity(intent);
                }else if(etTitle.getText().toString().trim().equals("")){
                    Toast.makeText(getApplicationContext(),"标题不能为空",Toast.LENGTH_SHORT).show();
                }else if(etContent.getText().toString().trim().equals("")){
                    Toast.makeText(getApplicationContext(),"公告内容不允许为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick({R.id.gonggao_img_choose})
    private void viewClick(View v) {
        switch (v.getId()) {
            case R.id.gonggao_img_choose:
                choosePicture();
                break;
        }
    }

    *//**
     * 删除此界面
     *//*
    public void deleteJiemian(){
        if(etTitle.getText().toString().equals("")
                && etAuthor.getText().toString().equals("")
                    && etContent.getText().toString().equals("")){
            finish();
        } else {
            createDialog();
        }
    }

    *//**
     * 创建对话框
     *//*
    public void createDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
       //设置对话框图标，可以使用自己的图片，Android本身也提供了一些图标供我们使用
       // builder.setIcon(android.R.drawable.ic_dialog_alert);
       //设置对话框标题
        builder.setTitle("放弃输入");
        //设置对话框内的文本
        builder.setMessage("是否要放弃此次编辑？");
        //设置确定按钮，并给按钮设置一个点击侦听，注意这个OnClickListener使用的是DialogInterface类里的一个内部接口
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

              @Override

               public void onClick(DialogInterface dialog, int which) {
                          // 执行点击确定按钮的业务逻辑
                  dialog.dismiss();
                  finish();
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


    public void choosePicture(){
        // 查看session是否过期
        // int selectedMode = MultiImageSelectorActivity.MODE_SINGLE;
        int selectedMode = MultiImageSelectorActivity.MODE_MULTI;
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
                    //返回的图片路径集合
                    this.mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    if (!ToolsKit.isEmpty(this.mSelectPath)) {
                        mFileList.clear();
                        for (String imagePath : mSelectPath) {
                            Uri uri = Uri.parse("content:/"+imagePath);
                            Log.d("uri",uri+"");
                            Bitmap bitmap = getOriginalBitmap(uri);
                            SpannableString ss = getBitmapMime(bitmap, uri);
                            insertIntoEditText(ss);
                        }
                    }

                }
            }
        }
    }


    *//**
     * EditText中可以接收的图片(要转化为SpannableString)
     *
     * @param pic
     * @param uri
     * @return SpannableString
     *//*
    private SpannableString getBitmapMime(Bitmap pic, Uri uri) {
       *//* int imgWidth = pic.getWidth();
        int imgHeight = pic.getHeight();
        // 只对大尺寸图片进行下面的压缩，小尺寸图片使用原图
        if (imgWidth >= mInsertedImgWidth) {
            float scale = (float) mInsertedImgWidth / imgWidth;
            Matrix mx = new Matrix();
            mx.setScale(scale, scale);
            pic = Bitmap.createBitmap(pic, 0, 0, imgWidth, imgHeight, mx, true);
        }*//*
        String smile = uri.getPath();
        SpannableString ss = new SpannableString(smile);
        ImageSpan span = new ImageSpan(this, pic);
        ss.setSpan(span, 0, smile.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }


    private void insertIntoEditText(SpannableString ss) {
        // 先获取Edittext中原有的内容
        Editable et = etContent.getText();
        int start = etContent.getSelectionStart();
        // 设置ss要添加的位置
        et.insert(start, ss);
        // 把et添加到Edittext中
        etContent.setText(et);
        // 设置Edittext光标在最后显示
        etContent.setSelection(start + ss.length());
    }


    private Bitmap getOriginalBitmap(Uri photoUri) {
        if (photoUri == null) {
            return null;
        }
        Bitmap bitmap = null;
        try {
            ContentResolver conReslv = getContentResolver();
            // 得到选择图片的Bitmap对象
            bitmap = MediaStore.Images.Media.getBitmap(conReslv, photoUri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }*/
}
