/**
 * 
 */
package com.henghao.hhworkpresent;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.benefit.buy.library.utils.tools.ToolsFile;
import com.benefit.buy.library.utils.tools.ToolsKit;
import com.henghao.hhworkpresent.exception.CustomExceptionHandler;
import com.henghao.hhworkpresent.service.ReConnectService;
import com.henghao.hhworkpresent.utils.LocationUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * @author zhangxianwen
 */
public class FMApplication extends Application {

    /** 对外提供整个应用生命周期的Context **/
    private static Context instance;

    private final List<Activity> activityList = new LinkedList<Activity>();

    /**
     * 对外提供Application Context
     * @return
     */
    public static Context gainContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // 注意该方法要再setContentView方法之前实现

        initImageLoader(getApplicationContext());
        LocationUtils.Location(this);
        SDKInitializer.initialize(this);
        appException();
    }

    public static void initImageLoader(Context context) {
        //缓存文件的目录
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "universalimageloader/Cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3) //线程池内线程的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) //将保存的时候的URI名称用MD5 加密
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024) // 内存缓存的最大值
                .diskCacheSize(50 * 1024 * 1024)  // SD卡缓存的最大值
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                // 由原先的discCache -> diskCache
                .diskCache(new UnlimitedDiskCache(cacheDir))//自定义缓存路径
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();
        //全局初始化此配置
        ImageLoader.getInstance().init(config);
    }


    /**
     * 异常处理类
     * @see [类、类#方法、类#成员]
     * @since [产品/模块版本]
     */
    private void appException() {
        boolean sdcardExist = ToolsFile.isSdcardExist();
        if (!sdcardExist) {
            return;
        }
        String path = Constant.LOG_DIR_PATH;
        ToolsFile.createDirFile(path);
        // catch (Exception e) {
        // //这里不能再向上抛异常，如果想要将log信息保存起来，则抛出runtime异常，
        // 让自定义的handler来捕获，统一将文件保存起来上传
        // throw new RuntimeException(e);
        // }
        CustomExceptionHandler mCustomExceptionHandler = CustomExceptionHandler.getInstance();
        mCustomExceptionHandler.init(getApplicationContext(), path);
    }

    /**
     * 添加Activity到容器中
     * @param activity
     * @see [类、类#方法、类#成员]
     * @since [产品/模块版本]
     */
    public void addActivity(Activity activity) {
        Log.e("@@@", "add:" + activity);
        this.activityList.add(activity);
    }

    /**
     * 删除对应的activity 〈一句话功能简述〉 〈功能详细描述〉
     * @param activity
     * @see [类、类#方法、类#成员]
     * @since [产品/模块版本]
     */
    public void removeActivity(Activity activity) {
        if (!ToolsKit.isEmpty(this.activityList)) {
            Log.e("@@@", "remove:" + activity);
            this.activityList.remove(activity);
        }
    }

    public void stopService() {
        Intent reConnectService = new Intent(this, ReConnectService.class);
        stopService(reConnectService);
    }

    public void startService() {
        // 自动恢复连接服务
        Intent reConnectService = new Intent(this, ReConnectService.class);
        startService(reConnectService);
    }

    /**
     * 遍历所有Activity并finish
     * @see [类、类#方法、类#成员]
     * @since [产品/模块版本]
     */
    public void exit() {
        try {
            for (Activity activity: this.activityList) {
                Log.e("@@@", "exit:" + activity);
                activity.finish();
            }
        }
        catch (Exception e) {
            System.exit(1);
        }
    }

}
