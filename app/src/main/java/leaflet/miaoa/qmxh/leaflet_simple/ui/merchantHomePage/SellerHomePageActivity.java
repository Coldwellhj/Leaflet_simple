package leaflet.miaoa.qmxh.leaflet_simple.ui.merchantHomePage;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iceteck.silicompressorr.VideoCompress;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import leaflet.miaoa.qmxh.leaflet_simple.Login.LoginActivity;
import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.base.BaseFragmentActivity;
import leaflet.miaoa.qmxh.leaflet_simple.ui.merchantHomePage.home.SellerHomeFragment;
import leaflet.miaoa.qmxh.leaflet_simple.ui.merchantHomePage.mine.Seller_mine_Fragment;
import leaflet.miaoa.qmxh.leaflet_simple.ui.merchantHomePage.publish.EditPictureAdvActivity;
import leaflet.miaoa.qmxh.leaflet_simple.ui.merchantHomePage.publish.EditVideoActivity;
import leaflet.miaoa.qmxh.leaflet_simple.utils.PhotoUtils;
import leaflet.miaoa.qmxh.leaflet_simple.utils.ToastUtils;

import static leaflet.miaoa.qmxh.leaflet_simple.Login.WelcomeActivity.Usertel;
import static leaflet.miaoa.qmxh.leaflet_simple.Login.WelcomeActivity.start;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.initialTimeOutMs;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.overdueReminder;
import static leaflet.miaoa.qmxh.leaflet_simple.utils.GetMacAddress.getAdresseMAC;
import static leaflet.miaoa.qmxh.leaflet_simple.utils.IpAdressUtils.getIpAdress;


/**
 * 商家主页面
 */
public class SellerHomePageActivity extends BaseFragmentActivity implements View.OnClickListener {

    public static final String FRAGMENT_SELLER_NEWS = "SellerNewsFragment";
    public static final String FRAGMENT_SELLER_HOME = "SellerHomeFragment";
    public static final String FRAGMENT_SELLER_MY = "SellerMyFragment";
    private RadioGroup rgMain;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private FragmentTransaction currentTransaction = null;
    private Fragment currentFragment;
    private FrameLayout fl_seller_main;
    private ImageView iv_seller_user_home;
    private TextView tv_seller_user_home;
    private LinearLayout ll_seller_user_home;
    private ImageView iv_seller_user_my;
    private TextView tv_seller_user_my;
    private LinearLayout ll_seller_user_my;
    private List<Fragment> listFragment = new ArrayList<>();
    int oldIndex;//用户看到的item
    int newIndex;//用户即将看到的item
    int count;//
    private boolean clicked = false;// 记录加号按钮的点击状态，默认为没有点击
    private RelativeLayout plus_rl;
    private ImageView plus_yuan;
    private ImageView take_photo;
    private ImageView take_video;
    private String videoTime;
    private int videoTime1;
    private ImageView upload_video;
    private Animation  scale_max, scale_min,alpha_button;
    public static final int RECORD_SYSTEM_VIDEO = 1;
    SellerHomeFragment sellerHomeFragment;
    Seller_mine_Fragment seller_mine_fragment;
    private QMUITipDialog LondingDialog;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private String outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    String[] permissions = { Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
    //声明一个静态常量，用作退出BaseActivity的Tag
    public static final String EXIST = "exist";
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {//判断其他Activity启动本Activity时传递来的intent是否为空
            //获取intent中对应Tag的布尔值
            if("true".equals(intent.getStringExtra("pay"))&newIndex==1){

                seller_mine_fragment.getPersonalBalance();//支付成功改变余额
                switchFragment();
            }else {
                sellerHomeFragment.refreshElse();
            }

            boolean isExist = intent.getBooleanExtra(EXIST, false);
            //如果为真则退出本Activity
            if (isExist) {
                //设置别名
                JPushInterface.setAlias(this,1,"");
                this.finish();

                Intent intent1=new Intent(SellerHomePageActivity.this, LoginActivity.class);
                startActivity(intent1);
            }
        }
    }
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_seller_home_page);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //进入APP时触发广告过期动作并接收消息提醒
        overdueReminder();
        //设置别名
        JPushInterface.setAlias(this,1,Usertel);
        if("".equals(start)){


            //初始化定位
            initLocation();
            startLocation();
        }
    }

    @Override
    protected void setFindViewById() {
        fl_seller_main = (FrameLayout) findViewById(R.id.fl_seller_main);
        ll_seller_user_home = (LinearLayout) findViewById(R.id.ll_seller_user_home);
        ll_seller_user_my = (LinearLayout) findViewById(R.id.ll_seller_user_my);
        iv_seller_user_home = (ImageView) findViewById(R.id.iv_seller_user_home);
        iv_seller_user_my = (ImageView) findViewById(R.id.iv_seller_user_my);
        tv_seller_user_home = (TextView) findViewById(R.id.tv_seller_user_home);
        tv_seller_user_my = (TextView) findViewById(R.id.tv_seller_user_my);
        plus_rl = (RelativeLayout) findViewById(R.id.plus_rl);
        plus_yuan = (ImageView) findViewById(R.id.plus_yuan);
        take_photo = (ImageView) findViewById(R.id.take_photo);
        take_video = (ImageView) findViewById(R.id.take_video);
        upload_video = (ImageView) findViewById(R.id.upload_video);
        scale_max = AnimationUtils.loadAnimation(this, R.anim.scale_max);
        scale_min = AnimationUtils.loadAnimation(this, R.anim.scale_min);
        alpha_button = AnimationUtils.loadAnimation(this, R.anim.alpha_button);
        sellerHomeFragment= new SellerHomeFragment();
        seller_mine_fragment=new Seller_mine_Fragment();
        listFragment.add(sellerHomeFragment);
        listFragment.add(seller_mine_fragment);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_seller_main, listFragment.get(0)).commit();
    }

    @Override
    protected void setListener() {
        ll_seller_user_home.setOnClickListener(this);
        ll_seller_user_my.setOnClickListener(this);
        plus_yuan.setOnClickListener(new View.OnClickListener() {
            // 监听加号按钮的点击
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                changeAnimation();


            }
        });
        plus_rl.setOnClickListener(new View.OnClickListener() {
            // 监听加号按钮的点击
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                changeAnimation();

            }
        });

        //初始化模糊界面
        plus_rl.setClickable(false);

        take_photo.setOnClickListener(onClickListener_w);
        take_video.setOnClickListener(onClickListener);
        upload_video.setOnClickListener(onClickListener_u);

    }

    private View.OnClickListener onClickListener_w = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            changeAnimation();
            v.startAnimation(alpha_button);
//            plus_yuan.performClick();
            Intent intent =new Intent(SellerHomePageActivity.this,EditPictureAdvActivity.class);
            startActivity(intent);
        }
    };
    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            changeAnimation();
            v.startAnimation(alpha_button);
//            plus_yuan.performClick();
            if (Build.VERSION.SDK_INT >= 23) {
                checkPermission(SellerHomePageActivity.this);
            } else {
                reconverIntent();
            }

        }
    };
    private View.OnClickListener onClickListener_u = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            changeAnimation();
            v.startAnimation(alpha_button);
//            plus_yuan.performClick();
            Intent intent =new Intent(SellerHomePageActivity.this,EditVideoActivity.class);
            intent.putExtra("takeVideo","false");
            startActivity(intent);
        }
    };
    @Override
    protected void setControl() {

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyLocation();
    }
    @Override
    public void onClick(View view) {
            switch (view.getId()){
                case R.id.ll_seller_user_home:
                    newIndex = 0;//选中第一项
                    break;
                case R.id.ll_seller_user_my:
                    newIndex = 1;//选中第二项
                    if(count>0){
                        seller_mine_fragment.getPersonalBalance();
                    }else {
                        count+=1;
                    }
                    break;
            }
        switchFragment();
    }
    public void switchFragment() {
        FragmentTransaction transaction;
        //如果选择的项不是当前选中项，则替换；否则，不做操作
        if (newIndex != oldIndex) {

            transaction = getSupportFragmentManager().beginTransaction();

            transaction.hide(listFragment.get(oldIndex));//隐藏当前显示项

            switch (oldIndex) {
                case 0:


                    iv_seller_user_home.setImageResource(R.mipmap.seller_home_uncheck);
                    tv_seller_user_home.setTextColor(Color.parseColor("#5d5f6a"));
                    break;

                case 1:
                    iv_seller_user_my.setImageResource(R.mipmap.seller_my_unchecked);
                    tv_seller_user_my.setTextColor(Color.parseColor("#5d5f6a"));
                    break;

            }

            //如果选中项没有加过，则添加
            if (!listFragment.get(newIndex).isAdded()) {
                //添加fragment
                transaction.add(R.id.fl_seller_main, listFragment.get(newIndex));
            }
            //显示当前选择项
            transaction.show(listFragment.get(newIndex)).commit();

            switch (newIndex) {
                case 0:
                    iv_seller_user_home.setImageResource(R.mipmap.seller_home_checked);
                    tv_seller_user_home.setTextColor(Color.parseColor("#f23030"));
                    break;

                case 1:
                    iv_seller_user_my.setImageResource(R.mipmap.seller_my_checked);
                    tv_seller_user_my.setTextColor(Color.parseColor("#f23030"));

                    break;

            }

        }


        //当前选择项变为选中项
        oldIndex = newIndex;

    }
    //加号一系列动画改变
    private void changeAnimation(){
        clicked = !clicked;
        // 两个按钮的显示隐藏
        take_photo.setVisibility(clicked ? View.VISIBLE : View.GONE);
        take_video.setVisibility(clicked ? View.VISIBLE : View.GONE);
        upload_video.setVisibility(clicked ? View.VISIBLE : View.GONE);
//                // 加号旋转
//                plus_im.startAnimation(clicked ? rotate_anticlockwise
//                        : rotate_clockwise);
        plus_yuan.setImageDrawable(getResources().getDrawable(clicked ? R.drawable.plus_yuan_publish: R.drawable.plus_yuan));
        // 按钮显示隐藏效果
        take_photo.startAnimation(clicked ? scale_max : scale_min);
        take_video.startAnimation(clicked ? scale_max : scale_min);
        upload_video.startAnimation(clicked ? scale_max : scale_min);
        // 背景色的改变
        plus_rl.setBackgroundColor(clicked ?
                getResources().getColor(R.color.seller_publish_bg) : Color.TRANSPARENT);
        // 背景是否可点击，用于控制Framelayout层下面的视图是否可点击
        plus_rl.setClickable(clicked);
        take_photo.setClickable(clicked? true : false);
        take_video.setClickable(clicked? true : false);
        upload_video.setClickable(clicked? true : false);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (take_photo.getVisibility() == View.VISIBLE
                && keyCode == KeyEvent.KEYCODE_BACK) {
            changeAnimation();
            return true;
        }
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            exitBy2Click();		//调用双击退出函数
        }
        return false;
    }
    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this,"再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }

    public  void checkPermission(Activity object) {


        int READ_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(object, Manifest.permission.READ_EXTERNAL_STORAGE);
        int WRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(object, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int CAMERA = ContextCompat.checkSelfPermission(object, Manifest.permission.CAMERA);


        int PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED;

        if (READ_EXTERNAL_STORAGE != PERMISSION_GRANTED || WRITE_EXTERNAL_STORAGE != PERMISSION_GRANTED|| CAMERA != PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(object,
                    permissions,
                    1);

        } else {
            reconverIntent();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    reconverIntent();
                } else {
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(getApplicationContext(), "获取权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;


        }
    }

    /**
     * 启用系统相机录制
     *
     * @param
     */
    public void reconverIntent() {
        Uri fileUri=null;
        fileUri = Uri.fromFile(getOutputMediaFile());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
//                    imageUri = FileProvider.getUriForFile(Personal_mine_info_Activity.this, "leaflet.miaoa.qmsh.leaflet.fileProvider", fileUri);

//            fileUri = FileProvider.getUriForFile(this.getApplicationContext(), "leaflet.miaoa.qmxh.mycameratimevideo.fileprovider",getOutputMediaFile());
        } else {
            fileUri = Uri.fromFile(getOutputMediaFile());
        }

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60);     //限制的录制时长 以秒为单位
//        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1024);        //限制视频文件大小 以字节为单位
//        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);      //设置拍摄的质量0~1
//        intent.putExtra(MediaStore.EXTRA_FULL_SCREEN, false);        // 全屏设置
        startActivityForResult(intent,RECORD_SYSTEM_VIDEO);

    }
    private File getOutputMediaFile() {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Toast.makeText(this, "请检查SDCard！", Toast.LENGTH_SHORT).show();
            return null;
        }

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "MyCameraApp");
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
        return mediaFile;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case RECORD_SYSTEM_VIDEO:
                String videoPath = PhotoUtils.getPath1(this, data.getData());
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(videoPath);
                videoTime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                videoTime1=Integer.parseInt(videoTime)/1000;
                final String destPath = outputDir + File.separator + "VID_" + new SimpleDateFormat("yyyyMMdd_HHmmss", getLocale()).format(new Date()) + ".mp4";


                VideoCompress.compressVideoLow(videoPath, destPath, new VideoCompress.CompressListener() {
                    @Override
                    public void onStart() {

                        LondingDialog = new QMUITipDialog.Builder(SellerHomePageActivity.this)
                                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                                .setTipWord("视频处理中···")
                                .create();

                        LondingDialog.show();
                    }

                    @Override
                    public void onSuccess() {
                        LondingDialog.dismiss();
                        Intent intent =new Intent(SellerHomePageActivity.this,EditVideoActivity.class);
                        intent.putExtra("videoPath",destPath);
                        intent.putExtra("videoTime",videoTime1+"");
                        intent.putExtra("takeVideo","true");
                        startActivity(intent);
                    }

                    @Override
                    public void onFail() {
                        LondingDialog.dismiss();
                        ToastUtils.showShort(SellerHomePageActivity.this,"视频处理失败，请重新选择");
                    }

                    @Override
                    public void onProgress(float percent) {
                        Log.i("tag",String.valueOf(percent) + "%");
                    }
                });




                break;


        }
    }

    private Locale getLocale() {
        Configuration config = getResources().getConfiguration();
        Locale sysLocale = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sysLocale = getSystemLocale(config);
        } else {
            sysLocale = getSystemLocaleLegacy(config);
        }

        return sysLocale;
    }

    @SuppressWarnings("deprecation")
    public static Locale getSystemLocaleLegacy(Configuration config){
        return config.locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getSystemLocale(Configuration config){
        return config.getLocales().get(0);
    }



    /**
     * 初始化定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void initLocation(){
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }
    /**
     * 开始定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void startLocation(){
//        //根据控件的选择，重新设置定位参数
//        resetOption();
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }
    /**
     * 停止定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void stopLocation(){
        // 停止定位
        locationClient.stopLocation();
    }
    /**
     * 销毁定位
     *
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void destroyLocation(){
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    /**
     * 默认的定位参数
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }
    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {

                StringBuffer sb = new StringBuffer();
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if(location.getErrorCode() == 0){
//                    sb.append("定位成功" + "\n");
//                    sb.append("定位类型: " + location.getLocationType() + "\n");
//                    sb.append("经    度    : " + location.getLongitude() + "\n");
//                    sb.append("纬    度    : " + location.getLatitude() + "\n");
//                    sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
//                    sb.append("提供者    : " + location.getProvider() + "\n");
//
//                    sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
//                    sb.append("角    度    : " + location.getBearing() + "\n");
//                    // 获取当前提供定位服务的卫星个数
//                    sb.append("星    数    : " + location.getSatellites() + "\n");
//                    sb.append("国    家    : " + location.getCountry() + "\n");
//                    sb.append("省            : " + location.getProvince() + "\n");
//                    sb.append("市            : " + location.getCity() + "\n");
//                    sb.append("城市编码 : " + location.getCityCode() + "\n");
//                    sb.append("区            : " + location.getDistrict() + "\n");
//                    sb.append("区域 码   : " + location.getAdCode() + "\n");
//                    sb.append("地    址    : " + location.getAddress() + "\n");
//                    sb.append("兴趣点    : " + location.getPoiName() + "\n");
//                    //定位完成的时间
//                    sb.append("定位时间: " + DateUtils.formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");

                } else {
                    //定位失败
                    sb.append("定位失败" + "\n");
                    sb.append("错误码:" + location.getErrorCode() + "\n");
                    sb.append("错误信息:" + location.getErrorInfo() + "\n");
                    sb.append("错误描述:" + location.getLocationDetail() + "\n");
                }
//                sb.append("***定位质量报告***").append("\n");
//                sb.append("* WIFI开关：").append(location.getLocationQualityReport().isWifiAble() ? "开启":"关闭").append("\n");
//                sb.append("* GPS状态：").append(getGPSStatusString(location.getLocationQualityReport().getGPSStatus())).append("\n");
//                sb.append("* GPS星数：").append(location.getLocationQualityReport().getGPSSatellites()).append("\n");
//                sb.append("****************").append("\n");
                //定位之后的回调时间
//                sb.append("回调时间: " + DateUtils.formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");

                //解析定位结果，
//                String result = sb.toString();

//                Toast.makeText(PersonalUserHomePageActivity.this,"定位结果："+result,Toast.LENGTH_SHORT).show();
                start=location.getLongitude()+","+location.getLatitude();
                stopLocation();

//                tvResult.setText(result);
            } else {

                Toast.makeText(getApplicationContext(),"定位失败，loc is null",Toast.LENGTH_SHORT).show();
//                tvResult.setText("定位失败，loc is null");
            }
        }
    };

    private void overdueReminder(){

        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                overdueReminder+"?aNum=" + Usertel+"&uIp="+getIpAdress(this)+"&uMac="+getAdresseMAC(this),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                        } catch (Exception e) {

                            Toast.makeText(SellerHomePageActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeOutMs,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
}
