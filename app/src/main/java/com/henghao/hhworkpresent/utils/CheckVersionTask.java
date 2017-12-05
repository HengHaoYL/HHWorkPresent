package com.henghao.hhworkpresent.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.henghao.hhworkpresent.ProtocolUrl;
import com.henghao.hhworkpresent.entity.UpdateInfo;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by bryanrady on 2017/4/26.
 * 检查版本更新
 */

public class CheckVersionTask extends Thread {
    private Activity activity;
    private UpdateInfo info;
    private static final String TAG = "CheckVersionTask";
    private static final int INSTALL_SUCCESS = -1;//安装成功
    private static final int UPDATA_CLIENT = 5;//更新软件
    private static final int GET_UNDATAINFO_ERROR = 6;//获取服务器更新信息失败
    private static final int DOWN_ERROR = 3;//下载失败
    private static final int UPDATE_DIALOG = 4;//更新进度条标题
    private String XML_PATH = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_UPDATE_XML_PASER_ADDRESS;//xml解析地址
    private String APK_PATH = ProtocolUrl.ROOT_URL + ProtocolUrl.APP_DOWNLOAD_APK_ADDRESS;//apk下载地址
    private ProgressDialog pd;//下载进度条
    private int length;//下载长度,转换为M之后的大小

    public CheckVersionTask(Activity activity) {
        this.activity = activity;
    }

    public void run() {
        try {
            //服务器版本信息
            Log.i(TAG, "xml解析地址: " + XML_PATH);
            Log.i(TAG, "apk下载地址: " + APK_PATH);
            URL url = new URL(XML_PATH);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.connect();
            InputStream is = conn.getInputStream();
            info = getUpdataInfo(is);
            Log.d(TAG, "本地版本:" + getVersion(activity));
            if (!info.getVersion().equals(getVersion(activity))) {
                Log.i(TAG, "版本号不同 ,提示用户升级 ");
                Message msg = new Message();
                msg.what = UPDATA_CLIENT;
                handler.sendMessage(msg);
            } else {
                Log.i(TAG, "版本号相同无需升级");
            }
        } catch (Exception e) {
            Message msg = new Message();
            msg.what = GET_UNDATAINFO_ERROR;
            handler.sendMessage(msg);
            e.printStackTrace();
        }
    }

    /**
     * 获取版本号
     * @return 当前应用的版本号
     *
     */
    public String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "获取版本失败";
        }
    }



    private Handler handler;

    {
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case UPDATA_CLIENT:
                        //对话框通知用户升级程序
                        showUpdataDialog();
                        break;
                    case GET_UNDATAINFO_ERROR:
                        //服务器超时
                        Toast.makeText(activity, "服务器版本获取失败", Toast.LENGTH_LONG).show();
                        break;
                    case DOWN_ERROR:
                        //下载apk失败
                        Toast.makeText(activity, "新版本下载失败", Toast.LENGTH_LONG).show();
                        break;
                    case UPDATE_DIALOG:
                        pd.setTitle("正在下载更新:共" + length + "M");
                        break;
                }
            }
        };
    }

    private Handler installHandler;

    {
        installHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.arg1) {
                    case INSTALL_SUCCESS:
                        Toast.makeText(activity, "安装成功", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(activity, "安装失败", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
    }

    /*
     * 弹出对话框通知用户更新程序
     */
    private void showUpdataDialog() {
        AlertDialog.Builder builer = new AlertDialog.Builder(activity);
        builer.setTitle("版本升级");
        builer.setMessage(info.getDescription());
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "下载apk,更新");
                downLoadApk();
            }
        });
        builer.setNegativeButton("取消", null);
        AlertDialog dialog = builer.create();
        dialog.show();
    }

    /**
     * 从服务器中下载APK
     */
    private void downLoadApk() {
        pd = new ProgressDialog(activity);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setTitle("正在下载更新");
        pd.setCancelable(false);
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    getFileFromServer(pd);
                } catch (Exception e) {
                    Message msg = new Message();
                    msg.what = DOWN_ERROR;
                    handler.sendMessage(msg);
                    pd.dismiss(); //结束掉进度条对话框
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 安装apk
     *
     * @param file 安装apk
     */
    private void installApk(File file) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        activity.startActivity(intent);
    }

    /**
     * 用pull解析器解析服务器返回的xml文件 (xml封装了版本号)
     *
     * @param is 输入流
     * @return
     * @throws Exception
     */
    public static UpdateInfo getUpdataInfo(InputStream is) throws Exception {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(is, "utf-8");//设置解析的数据源
        int type = parser.getEventType();
        UpdateInfo info = new UpdateInfo();
        while (type != XmlPullParser.END_DOCUMENT) {
            switch (type) {
                case XmlPullParser.START_TAG:
                    if ("version".equals(parser.getName())) {
                        info.setVersion(parser.nextText()); //获取版本号
                    } else if ("description".equals(parser.getName())) {
                        info.setDescription(parser.nextText()); //获取该文件的信息
                    }
                    break;
            }
            type = parser.next();
        }
        Log.d(TAG, "服务器版本: " + info.getVersion());
        return info;
    }

    private void getFileFromServer(ProgressDialog pd) throws Exception {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(APK_PATH);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            length = conn.getContentLength() / 1024 / 1024;
            Message msg = new Message();
            msg.what = UPDATE_DIALOG;
            handler.sendMessage(msg);
            pd.setMax(length);
            conn.connect();
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(), "gyajj.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                //获取当前下载量
                pd.setProgress(total / 1024 / 1024);
            }
            fos.close();
            bis.close();
            is.close();
            installApk(file);
            pd.dismiss(); //结束掉进度条对话框
        }
    }
}
