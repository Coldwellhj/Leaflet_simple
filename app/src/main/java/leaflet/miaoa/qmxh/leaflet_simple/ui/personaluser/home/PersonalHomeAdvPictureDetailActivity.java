package leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dueeeke.videoplayer.GifView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.bean.ListActivityBean;
import leaflet.miaoa.qmxh.leaflet_simple.bean.RedPacketEntity;
import leaflet.miaoa.qmxh.leaflet_simple.ui.Interface.OnRedPacketDialogClickListener;
import leaflet.miaoa.qmxh.leaflet_simple.ui.adapter.RedPacketViewHolder;
import leaflet.miaoa.qmxh.leaflet_simple.ui.widget.CustomDialog;
import leaflet.miaoa.qmxh.leaflet_simple.ui.widget.GlideImageLoader;
import leaflet.miaoa.qmxh.leaflet_simple.utils.ToastUtils;

import static leaflet.miaoa.qmxh.leaflet_simple.Login.WelcomeActivity.Usertel;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.adverNoResiude;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.getAdverById;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.getAdverResidueById;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.initialTimeOutMs;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.updateOfReduceReplacee;

public class PersonalHomeAdvPictureDetailActivity extends AppCompatActivity {
    private Activity activity;
    private RelativeLayout iv_back;
    private Banner banner;
    private TextView tv_content;
    private List<ListActivityBean.Adv> advList = new ArrayList<ListActivityBean.Adv>();
    private List<String > advList_picture=new ArrayList<String>();
    private GifView iv_redtime;
    private TextView tv_red_packet_time;
    private RelativeLayout rl1;
    private  String aId="";
    private int position=0;
    private View mRedPacketDialogView;
    private RedPacketViewHolder mRedPacketViewHolder;
    private CustomDialog mRedPacketDialog;
    private Boolean isPlayA=true;
    private CountDownTimer countDownTimer;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (advList.get(0).isRedpacket()) {
                        isPlayA = true;
                        rl1.setVisibility(View.VISIBLE);
                        redpacket_count_down(advList_picture.size()*3000);
                    } else {
                        isPlayA = false;
                        rl1.setVisibility(View.GONE);
                        adverNoResiude();
                    }
                    bindEvents();
                    break;

                case 3:
                    showDialog();
                    break;
                case 4:
                    updateOfReduceReplacee();
                    break;
                case 5:
                    countDownTimer.cancel();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_home_adv_picture_detail);
        activity=this;
        Intent intent=getIntent();
        aId=intent.getStringExtra("aId");
        position=intent.getIntExtra("position",0);
        initView();
        getAdverById();
    }

    private void initView() {
        iv_back = (RelativeLayout) findViewById(R.id.iv_back);
        banner = (Banner) findViewById(R.id.banner);
        tv_content = (TextView) findViewById(R.id.tv_content);
        iv_redtime = (GifView) findViewById(R.id.iv_redtime);

        tv_red_packet_time = (TextView) findViewById(R.id.tv_red_packet_time);
        rl1 = (RelativeLayout) findViewById(R.id.rl1);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("isplay", isPlayA);
                intent.putExtra("position", position);
                intent.putExtra(" isread", "true");
                setResult(RESULT_OK, intent);//回传数据到主Activity
                finish();
            }
        });
    }
    private void bindEvents(){

        tv_content.setText(advList.get(0).getaContent());
        banner.setImages(advList_picture)
                .setImageLoader(new GlideImageLoader())
                .start();
        banner.updateBannerStyle(BannerConfig.NUM_INDICATOR);


    }

    public  void redpacket_count_down(int total_time){
        iv_redtime.setMovieResource(R.drawable.loading);
        if (countDownTimer == null){
            countDownTimer = new CountDownTimer(total_time,1000) {
                @Override
                public void onTick(long l) {
                    if(!activity.isFinishing()){
                        String time = l/1000+"s";
                        tv_red_packet_time.setText(time);
                        tv_red_packet_time.setTextColor(Color.parseColor("#999999"));
                    }else {
                        Message message=new Message();
                        message.what=5;
                        handler.sendMessage(message);
                    }

                }

                @Override
                public void onFinish() {
                    if(!activity.isFinishing()){
                        getAdverResidueById(true);
                        iv_redtime.setPaused(true);
                        tv_red_packet_time.setText("0s");
                        tv_red_packet_time.setTextColor(Color.parseColor("#333333"));
                        countDownTimer = null;
                    }else {

                    }

                }
            }.start();
        }
    }


    public void showDialog() {
        RedPacketEntity entity = new RedPacketEntity(advList.get(0).getuName(), advList.get(0).getuImg(), advList.get(0).getaPrice());
        showRedPacketDialog(entity);
    }

    public void showRedPacketDialog(RedPacketEntity entity) {
        if (mRedPacketDialogView == null) {
            mRedPacketDialogView = View.inflate(this, R.layout.dialog_red_packet, null);
            mRedPacketViewHolder = new RedPacketViewHolder(this, mRedPacketDialogView);
            mRedPacketDialog = new CustomDialog(this, mRedPacketDialogView, R.style.custom_dialog);
            mRedPacketDialog.setCancelable(false);
        }

        mRedPacketViewHolder.setData(entity);
        mRedPacketViewHolder.setOnRedPacketDialogClickListener(new OnRedPacketDialogClickListener() {
            @Override
            public void onCloseClick() {
                mRedPacketDialog.dismiss();
            }

            @Override
            public void onOpenClick() {
                //领取红包,调用接口
                mRedPacketDialog.dismiss();
                getAdverResidueById(false);

            }
        });

        mRedPacketDialog.show();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent=new Intent();
            intent.putExtra("isplay", isPlayA);
            intent.putExtra("position", position);
            intent.putExtra(" isread", "true");
            setResult(RESULT_OK, intent);//回传数据到主Activity
            finish();
        }
        return false;
    }
    private void getAdverById() {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                getAdverById + "?aId=" + aId+"&uNum="+Usertel,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            ListActivityBean listActivityBean = new ListActivityBean();
                            String aContent = jsonObject.getString("aContent");
                            String aMatter = jsonObject.getString("aMatter");
                            String watchCount = jsonObject.getString("watchCount");
                            String aPrice = jsonObject.getString("aPrice");
                            Long aResidue = jsonObject.getLong("aResidue");
                            String ifRead = jsonObject.getString("ifRead");
                            String uName = jsonObject.getString("uNick");
                            String uImg = jsonObject.getString("uImg");
                            String adv_picture[]=aMatter.split("<-->");
                            for(int i=0;i<adv_picture.length;i++){
                                advList_picture.add(i,adv_picture[i]);
                            }


                            ListActivityBean.Adv adv = listActivityBean.new Adv();

                            adv.setaContent(aContent);
                            adv.setaMatter(aMatter);
                            adv.setWatchCount(watchCount);
                            adv.setaPrice(aPrice);
                            adv.setuImg(uImg);
                            adv.setuName(uName);

                            if (aResidue > 0 && "false".equals(ifRead)) {
                                adv.setRedpacket(true);
                            } else {
                                adv.setRedpacket(false);
                            }
                            advList.add(adv);
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);


                        } catch (Exception e) {

                            Toast.makeText(PersonalHomeAdvPictureDetailActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(PersonalHomeAdvPictureDetailActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeOutMs,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
    private void  getAdverResidueById(final boolean isFirst){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                getAdverResidueById+"?aId="+aId +"&uNum="+Usertel ,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            String s[]=response.split("<-->");
                            if(Integer.parseInt(s[0])>0&&isFirst){
                                Message message=new Message();
                                message.what=3;
                                handler.sendMessage(message);
                            }else if(Integer.parseInt(s[0])>0&&!isFirst&&"false".equals(s[1])){
                                Message message=new Message();
                                message.what=4;
                                handler.sendMessage(message);
                            }else if(Integer.parseInt(s[0])<=0){
                                Toast.makeText(PersonalHomeAdvPictureDetailActivity.this, "红包已经被领完了", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(PersonalHomeAdvPictureDetailActivity.this, "红包已经被你领过了", Toast.LENGTH_SHORT).show();
                            }



                        } catch (Exception e) {

                            Toast.makeText(PersonalHomeAdvPictureDetailActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(PersonalHomeAdvPictureDetailActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeOutMs,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
    private void  updateOfReduceReplacee(){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                updateOfReduceReplacee+"?aId="+aId+"&uNum="+Usertel  ,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            if ("true".equals(response)) {
                                isPlayA = false;
                                rl1.setVisibility(View.GONE);
                                ToastUtils.showShort(PersonalHomeAdvPictureDetailActivity.this, "已存入余额");
                            } else {
                                ToastUtils.showShort(PersonalHomeAdvPictureDetailActivity.this, "领取失败");
                            }


                        } catch (Exception e) {

                            Toast.makeText(PersonalHomeAdvPictureDetailActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(PersonalHomeAdvPictureDetailActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeOutMs,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
    private void  adverNoResiude(){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                adverNoResiude+"?aId="+aId+"&uNum="+Usertel  ,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            if("true".equals(response)){

                            }else {
//                                ToastUtils.showShort(PersonalHomeAdvDetailActivity.this,"修改广告失败");
                            }



                        } catch (Exception e) {

                            Toast.makeText(PersonalHomeAdvPictureDetailActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(PersonalHomeAdvPictureDetailActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeOutMs,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
}
