package leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import leaflet.miaoa.qmxh.leaflet_simple.Login.LoginActivity;
import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.base.BaseFragmentActivity;
import leaflet.miaoa.qmxh.leaflet_simple.ui.adapter.ViewPagerAdapter;
import leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.home.PersonalHomeFragment;
import leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mall.PersonalMallFragment;
import leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mine.Personal_mine_Fragment;
import leaflet.miaoa.qmxh.leaflet_simple.utils.StatusBarCompat;

import static leaflet.miaoa.qmxh.leaflet_simple.Login.WelcomeActivity.Usertel;
import static leaflet.miaoa.qmxh.leaflet_simple.Login.WelcomeActivity.start;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.initialTimeOutMs;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.overdueReminder;
import static leaflet.miaoa.qmxh.leaflet_simple.utils.GetMacAddress.getAdresseMAC;
import static leaflet.miaoa.qmxh.leaflet_simple.utils.IpAdressUtils.getIpAdress;


/**
 * 个人用户主界面
 */
public class PersonalUserHomePageActivity extends BaseFragmentActivity implements View.OnClickListener{

//    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private ImageView ivRedMoney,ivHome,ivMy;
    private TextView tvRedMoney,tvHome,tvMy;
    private LinearLayout llRedMoney,llHome,llMy;
    private List<ImageView> ivList = new ArrayList<>();
    private List<TextView> tvList = new ArrayList<>();
    private List<Fragment> listFragment = new ArrayList<>();
    public static int topHeight;
    int oldIndex;//用户看到的item
    int newIndex;//用户即将看到的item
    int count;//
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    PersonalHomeFragment personalHomeFragment;
    PersonalMallFragment personalMallFragment;
    Personal_mine_Fragment personal_mine_fragment;
    //声明一个静态常量，用作退出BaseActivity的Tag
    public static final String EXIST = "exist";
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {//判断其他Activity启动本Activity时传递来的intent是否为空
            //获取intent中对应Tag的布尔值
            boolean isExist = intent.getBooleanExtra(EXIST, false);
            //如果为真则退出本Activity
            if (isExist) {
                this.finish();
                //设置别名
                JPushInterface.setAlias(this,1,"");
                this.finish();
                Intent intent1=new Intent(PersonalUserHomePageActivity.this, LoginActivity.class);
                startActivity(intent1);
            }
        }
    }
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_personal_user_home_page);
        StatusBarCompat.compat(this, Color.parseColor("#ffffffff"));
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
        ivRedMoney = (ImageView) findViewById(R.id.iv_personal_user_red_money);
        tvRedMoney = (TextView) findViewById(R.id.tv_personal_user_red_money);
        ivHome = (ImageView) findViewById(R.id.iv_personal_user_home);
        tvHome = (TextView) findViewById(R.id.tv_personal_user_home);
        ivMy = (ImageView) findViewById(R.id.iv_personal_user_my);
        tvMy = (TextView) findViewById(R.id.tv_personal_user_my);
        llRedMoney = (LinearLayout) findViewById(R.id.ll_personal_user_red_money);
        llHome = (LinearLayout) findViewById(R.id.ll_personal_user_home);
        llMy = (LinearLayout) findViewById(R.id.ll_personal_user_my);
//        viewPager = (ViewPager) findViewById(R.id.vp_personal_user_home_viewpager);
//        viewPager.setOffscreenPageLimit(0);
        personalHomeFragment=new PersonalHomeFragment();
        personalMallFragment=new PersonalMallFragment();
        personal_mine_fragment=new Personal_mine_Fragment();

        listFragment.add(personalHomeFragment);
        listFragment.add(personalMallFragment);
        listFragment.add(personal_mine_fragment);
        getSupportFragmentManager().beginTransaction().add(R.id.frame_content, listFragment.get(0)).commit();
//        listFragment.get(0);
    }

    @Override
    protected void setListener() {
        llRedMoney.setOnClickListener(this);
        llHome.setOnClickListener(this);
        llMy.setOnClickListener(this);

    }

    @Override
    protected void setControl() {



//        ivList.add(ivHome);
//        ivList.add(ivRedMoney);
//        ivList.add(ivMy);
//        tvList.add(tvHome);
//        tvList.add(tvRedMoney);
//        tvList.add(tvMy);
//        clearAllStyle();
//        ivHome.setImageResource(R.mipmap.seller_home_checked);
//        tvHome.setTextColor(Color.parseColor("#f23030"));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyLocation();
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;//状态栏高度
        int titleBarHeight = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();//标题栏高度
        topHeight = titleBarHeight + statusBarHeight;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_personal_user_home:
//                clearAllStyle();
//                ivHome.setImageResource(R.mipmap.seller_home_checked);
//                tvHome.setTextColor(Color.parseColor("#f23030"));
////                viewPager.setCurrentItem(0);
//                getSupportFragmentManager().beginTransaction().add(R.id.frame_content, listFragment.get(0)).commit();
                newIndex = 0;//选中第一项
                break;
            case R.id.ll_personal_user_red_money:
//                clearAllStyle();
//                ivRedMoney.setImageResource(R.mipmap.bribery_money_bright);
//                tvRedMoney.setTextColor(Color.parseColor("#f23030"));
                newIndex = 1;//选中第二项
//                viewPager.setCurrentItem(1);
//                getSupportFragmentManager().beginTransaction().add(R.id.frame_content, listFragment.get(1)).commit();
                break;
            case R.id.ll_personal_user_my:
//                clearAllStyle();
//                ivMy.setImageResource(R.mipmap.seller_my_checked);
//                tvMy.setTextColor(Color.parseColor("#f23030"));
////                viewPager.setCurrentItem(2);
//                listFragment.get(2);
                newIndex = 2;//选中第三项
                if(count>0){
                    personal_mine_fragment.getPersonalBalance();
                }else {
                    count+=1;
                }

                break;
        }
        switchFragment();
    }
//
//    //清除所有样式
//    private void clearAllStyle() {
//        ivRedMoney.setImageResource(R.mipmap.shopmall_unchecked);
//        ivHome.setImageResource(R.mipmap.seller_home_uncheck);
//        ivMy.setImageResource(R.mipmap.seller_my_unchecked);
//        tvRedMoney.setTextColor(Color.parseColor("#5d5f6a"));
//        tvHome.setTextColor(Color.parseColor("#5d5f6a"));
//        tvMy.setTextColor(Color.parseColor("#5d5f6a"));
//    }
    public void switchFragment() {
        FragmentTransaction transaction;
        //如果选择的项不是当前选中项，则替换；否则，不做操作
        if (newIndex != oldIndex) {

            transaction = getSupportFragmentManager().beginTransaction();

            transaction.hide(listFragment.get(oldIndex));//隐藏当前显示项

            switch (oldIndex) {
                case 0:


                    ivHome.setImageResource(R.mipmap.seller_home_uncheck);
                    tvHome.setTextColor(Color.parseColor("#5d5f6a"));
                    break;
                case 1:

                    ivRedMoney.setImageResource(R.mipmap.shopmall_unchecked);
                    tvRedMoney.setTextColor(Color.parseColor("#5d5f6a"));
                    break;
                case 2:
                    ivMy.setImageResource(R.mipmap.seller_my_unchecked);
                    tvMy.setTextColor(Color.parseColor("#5d5f6a"));
                    break;

            }

            //如果选中项没有加过，则添加
            if (!listFragment.get(newIndex).isAdded()) {
                //添加fragment
                transaction.add(R.id.frame_content, listFragment.get(newIndex));
            }
            //显示当前选择项
            transaction.show(listFragment.get(newIndex)).commit();

            switch (newIndex) {
                case 0:


                    ivHome.setImageResource(R.mipmap.seller_home_checked);
                    tvHome.setTextColor(Color.parseColor("#f23030"));

                    break;
                case 1:
                    ivRedMoney.setImageResource(R.mipmap.shopmall);
                    tvRedMoney.setTextColor(Color.parseColor("#f23030"));


                    break;
                case 2:
                    ivMy.setImageResource(R.mipmap.seller_my_checked);
                    tvMy.setTextColor(Color.parseColor("#f23030"));

                    break;

            }

        }


        //当前选择项变为选中项
        oldIndex = newIndex;

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
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

                            Toast.makeText(PersonalUserHomePageActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
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
