package leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dueeeke.videoplayer.player.VideoCacheManager;

import cn.jpush.android.api.JPushInterface;
import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.base.BaseOtherActivity;
import leaflet.miaoa.qmxh.leaflet_simple.ui.merchantHomePage.SellerHomePageActivity;
import leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.PersonalUserHomePageActivity;

import static leaflet.miaoa.qmxh.leaflet_simple.Login.WelcomeActivity.Usertel;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.updateMacLingByNum;


public class SellerSettingActivity extends BaseOtherActivity implements View.OnClickListener {


    private RelativeLayout rl_seller_my_seeting_back;
    private RelativeLayout rl_seller_my_seeting_call;
    private TextView login_off;
    private TextView tv_Buffer;
    private RelativeLayout rl_clearBuffer;
    private CheckBox screen_notify;
    private CheckBox auto_playVideo;
    private CheckBox ck_autoDownload;
    private String flag;//是商家还是个人的标识
    private boolean newsRemind=true;//消息推送
    private boolean autoVideo=true;//自动播放
    private boolean autoDownload=true;//wifi下自动下载
    public static Boolean auto=true;//自动播放视频
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_seller_setting);
        sharedPreferences = SellerSettingActivity.this.getSharedPreferences("login", Context.MODE_PRIVATE);
        newsRemind =sharedPreferences.getBoolean("newsRemind", true);
        autoVideo =sharedPreferences.getBoolean("autoVideo", true);
        autoDownload =sharedPreferences.getBoolean("autoDownload", true);
        editor = sharedPreferences.edit();//获取编辑器
        Intent intent=getIntent();
        flag=intent.getStringExtra("flag");
        initView();

    }


    private void initView() {
        rl_seller_my_seeting_back = (RelativeLayout) findViewById(R.id.rl_seller_my_seeting_back);
        rl_seller_my_seeting_call = (RelativeLayout) findViewById(R.id.rl_seller_my_seeting_call);
        login_off = (TextView) findViewById(R.id.login_off);
        tv_Buffer = (TextView) findViewById(R.id.tv_Buffer);
        rl_clearBuffer = (RelativeLayout) findViewById(R.id.rl_clearBuffer);
        try {
            tv_Buffer.setText("缓存大小" + VideoCacheManager.getAllCacheSize(this));//获取所有缓存大小
        } catch (Exception e) {
            e.printStackTrace();
        }
        rl_seller_my_seeting_back.setOnClickListener(this);
        rl_seller_my_seeting_call.setOnClickListener(this);
        login_off.setOnClickListener(this);
        rl_clearBuffer.setOnClickListener(this);
        screen_notify = (CheckBox) findViewById(R.id.screen_notify);
        auto_playVideo = (CheckBox) findViewById(R.id.auto_playVideo);
        ck_autoDownload = (CheckBox) findViewById(R.id.ck_autoDownload);
        screen_notify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    //选中
//                    jumpFlag = 0;
//                    iv_choose_type.setImageDrawable(getResources().getDrawable(R.mipmap.login_switch_on));

                    //设置别名
                    editor.putBoolean("newsRemind", false);
                    editor.commit();
                    JPushInterface.setAlias(SellerSettingActivity.this,1,"0");
                }else{
                    //未选中
//                    jumpFlag = 1;
//                    iv_choose_type.setImageDrawable(getResources().getDrawable(R.mipmap.login_switch_off));
                    //设置别名
                    JPushInterface.setAlias(SellerSettingActivity.this,1,Usertel);
                    editor.putBoolean("newsRemind", true);
                    editor.commit();
                }
            }
        });// 添加监听事件

        auto_playVideo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    //选中
                    auto = false;
                    editor.putBoolean("autoVideo", false);
                    editor.commit();
                }else{
                    //未选中
                    auto = true;//自动播放
                    editor.putBoolean("autoVideo", true);
                    editor.commit();
                }
            }
        });// 添加监听事件
        ck_autoDownload.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    //选中

                    editor.putBoolean("autoDownload", false);
                    editor.commit();
                }else{
                    //未选中

                    editor.putBoolean("autoDownload", true);
                    editor.commit();
                }
            }
        });// 添加监听事件
        if(newsRemind){

            screen_notify.setBackground(getResources().getDrawable(R.drawable.button_check));
        }else {

            screen_notify.setBackground(getResources().getDrawable(R.drawable.button_uncheck));
        }
        if(autoVideo){

            auto_playVideo.setBackground(getResources().getDrawable(R.drawable.button_check));
        }else {

            auto_playVideo.setBackground(getResources().getDrawable(R.drawable.button_uncheck));
        }
        if(autoDownload){

            ck_autoDownload.setBackground(getResources().getDrawable(R.drawable.button_check));
        }else {

            ck_autoDownload.setBackground(getResources().getDrawable(R.drawable.button_uncheck));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_seller_my_seeting_back:
                finish();
                break;
            case R.id.rl_clearBuffer:
                if (VideoCacheManager.clearAllCache(this)) {
                    tv_Buffer.setText("缓存大小0M");
                    Toast.makeText(this, "清除缓存成功", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rl_seller_my_seeting_call:
                Intent intent = new Intent(SellerSettingActivity.this, AboutusActivity.class);
                startActivity(intent);
                break;
            case R.id.login_off:

                updateMacLingByNum();


                break;

        }

    }

    //退出登录
    private void updateMacLingByNum() {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                updateMacLingByNum +"?uNum="+Usertel ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {


                            if("true".equals(response)){
                                //清除本地账号缓存
                                SharedPreferences sharedPreferences = SellerSettingActivity.this.getSharedPreferences("login", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                                editor.putString("tel", "");
                                editor.putInt("umark", 2);
                                editor.putString("islogin", "");
                                editor.commit();

                                if("seller".equals(flag)){

                                    Intent intent1 = new Intent(SellerSettingActivity.this, SellerHomePageActivity.class);
                                    intent1.putExtra(SellerHomePageActivity.EXIST, true);
                                    startActivity(intent1);
                                }else {
                                    Intent intent1 = new Intent(SellerSettingActivity.this, PersonalUserHomePageActivity.class);
                                    intent1.putExtra(PersonalUserHomePageActivity.EXIST, true);
                                    startActivity(intent1);
                                }
                            }






                        } catch (Exception e) {
                            Toast.makeText(SellerSettingActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SellerSettingActivity.this, "网络加载失败", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
}
