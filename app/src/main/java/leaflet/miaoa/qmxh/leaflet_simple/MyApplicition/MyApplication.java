package leaflet.miaoa.qmxh.leaflet_simple.MyApplicition;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.amap.api.location.AMapLocationQualityReport;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.File;

import cn.jpush.android.api.JPushInterface;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;


/**
 * Created by Charmer on 2016/9/13.
 */
public class MyApplication extends Application{
//    private AMapLocationClient locationClient = null;
//    private AMapLocationClientOption locationOption = null;
//    public static String start="";

    public static boolean isNetWorkConnected=true;//网络状态（）
    public static Context  context;
    @Override
    public void onCreate() {
        super.onCreate();
        //极光初始化
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

//        //初始化定位
//        initLocation();
//        startLocation();
        //设置别名
        JPushInterface.setAlias(this,1,"0");
        initUniversalImageLoader();
        // | Here the player will set all the skin
//        JCVideoPlayer.setGlobleSkin(R.color.colorPrimary, R.color.colorAccent, R.drawable.skin_seek_progress,
//                R.color.bottom_bg, R.drawable.skin_enlarge_video, R.drawable.skin_shrink_video);
        //这里将会改变所有缩略图的ScaleType | Here will change all thumbnails ScaleType
        JCVideoPlayer.setThumbImageViewScalType(ImageView.ScaleType.FIT_XY);


        context = getApplicationContext();



    }

    private void initUniversalImageLoader() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(new ColorDrawable(Color.parseColor("#f0f0f0")))
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        int memClass = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))
                .getMemoryClass();
        int memCacheSize = 1024 * 1024 * memClass / 8;

        File cacheDir = new File(Environment.getExternalStorageDirectory().getPath() + "/jiecao/cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator())
                .memoryCache(new UsingFreqLimitedMemoryCache(memCacheSize))
                .memoryCacheSize(memCacheSize)
                .diskCacheSize(50 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000))
                .defaultDisplayImageOptions(options)
                .build();
        ImageLoader.getInstance().init(config);
    }

    public static int getScreenWidth(Context context){
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }
    public static int getScreenHeight(Context context){
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }


    @Override
    public void onTerminate() {
        // 程序终止的时候执行

        super.onTerminate();


    }

//    /**
//     * 初始化定位
//     *
//     * @since 2.8.0
//     * @author hongming.wang
//     *
//     */
//    private void initLocation(){
//        //初始化client
//        locationClient = new AMapLocationClient(this.getApplicationContext());
//        locationOption = getDefaultOption();
//        //设置定位参数
//        locationClient.setLocationOption(locationOption);
//        // 设置定位监听
//        locationClient.setLocationListener(locationListener);
//    }
//    /**
//     * 开始定位
//     *
//     * @since 2.8.0
//     * @author hongming.wang
//     *
//     */
//    private void startLocation(){
////        //根据控件的选择，重新设置定位参数
////        resetOption();
//        // 设置定位参数
//        locationClient.setLocationOption(locationOption);
//        // 启动定位
//        locationClient.startLocation();
//    }
//    /**
//     * 停止定位
//     *
//     * @since 2.8.0
//     * @author hongming.wang
//     *
//     */
//    private void stopLocation(){
//        // 停止定位
//        locationClient.stopLocation();
//    }
//    /**
//     * 销毁定位
//     *
//     * @since 2.8.0
//     * @author hongming.wang
//     *
//     */
//    private void destroyLocation(){
//        if (null != locationClient) {
//            /**
//             * 如果AMapLocationClient是在当前Activity实例化的，
//             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
//             */
//            locationClient.onDestroy();
//            locationClient = null;
//            locationOption = null;
//        }
//    }
//
//    /**
//     * 默认的定位参数
//     * @since 2.8.0
//     * @author hongming.wang
//     *
//     */
//    private AMapLocationClientOption getDefaultOption(){
//        AMapLocationClientOption mOption = new AMapLocationClientOption();
//        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
//        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
//        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
//        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
//        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
//        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
//        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
//        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
//        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
//        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
//        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
//        return mOption;
//    }
//    /**
//     * 定位监听
//     */
//    AMapLocationListener locationListener = new AMapLocationListener() {
//        @Override
//        public void onLocationChanged(AMapLocation location) {
//            if (null != location) {
//
//                StringBuffer sb = new StringBuffer();
//                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
//                if(location.getErrorCode() == 0){
////                    sb.append("定位成功" + "\n");
////                    sb.append("定位类型: " + location.getLocationType() + "\n");
////                    sb.append("经    度    : " + location.getLongitude() + "\n");
////                    sb.append("纬    度    : " + location.getLatitude() + "\n");
////                    sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
////                    sb.append("提供者    : " + location.getProvider() + "\n");
////
////                    sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
////                    sb.append("角    度    : " + location.getBearing() + "\n");
////                    // 获取当前提供定位服务的卫星个数
////                    sb.append("星    数    : " + location.getSatellites() + "\n");
////                    sb.append("国    家    : " + location.getCountry() + "\n");
////                    sb.append("省            : " + location.getProvince() + "\n");
////                    sb.append("市            : " + location.getCity() + "\n");
////                    sb.append("城市编码 : " + location.getCityCode() + "\n");
////                    sb.append("区            : " + location.getDistrict() + "\n");
////                    sb.append("区域 码   : " + location.getAdCode() + "\n");
////                    sb.append("地    址    : " + location.getAddress() + "\n");
////                    sb.append("兴趣点    : " + location.getPoiName() + "\n");
////                    //定位完成的时间
////                    sb.append("定位时间: " + DateUtils.formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
//
//                } else {
//                    //定位失败
//                    sb.append("定位失败" + "\n");
//                    sb.append("错误码:" + location.getErrorCode() + "\n");
//                    sb.append("错误信息:" + location.getErrorInfo() + "\n");
//                    sb.append("错误描述:" + location.getLocationDetail() + "\n");
//                }
////                sb.append("***定位质量报告***").append("\n");
////                sb.append("* WIFI开关：").append(location.getLocationQualityReport().isWifiAble() ? "开启":"关闭").append("\n");
////                sb.append("* GPS状态：").append(getGPSStatusString(location.getLocationQualityReport().getGPSStatus())).append("\n");
////                sb.append("* GPS星数：").append(location.getLocationQualityReport().getGPSSatellites()).append("\n");
////                sb.append("****************").append("\n");
//                //定位之后的回调时间
////                sb.append("回调时间: " + DateUtils.formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");
//
//                //解析定位结果，
////                String result = sb.toString();
//
////                Toast.makeText(PersonalUserHomePageActivity.this,"定位结果："+result,Toast.LENGTH_SHORT).show();
//                start=location.getLongitude()+","+location.getLatitude();
//                stopLocation();
//
////                tvResult.setText(result);
//            } else {
//
//                Toast.makeText(getApplicationContext(),"定位失败，loc is null",Toast.LENGTH_SHORT).show();
////                tvResult.setText("定位失败，loc is null");
//            }
//        }
//    };
    /**
     * 获取GPS状态的字符串
     * @param statusCode GPS状态码
     * @return
     */
    private String getGPSStatusString(int statusCode){
        String str = "";
        switch (statusCode){
            case AMapLocationQualityReport.GPS_STATUS_OK:
                str = "GPS状态正常";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPROVIDER:
                str = "手机中没有GPS Provider，无法进行GPS定位";
                break;
            case AMapLocationQualityReport.GPS_STATUS_OFF:
                str = "GPS关闭，建议开启GPS，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_MODE_SAVING:
                str = "选择的定位模式中不包含GPS定位，建议选择包含GPS定位的模式，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPERMISSION:
                str = "没有GPS定位权限，建议开启gps定位权限";
                break;
        }
        return str;
    }

    public static Context getContext(){return context;}


}