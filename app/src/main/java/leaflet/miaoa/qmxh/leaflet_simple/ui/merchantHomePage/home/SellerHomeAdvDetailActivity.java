package leaflet.miaoa.qmxh.leaflet_simple.ui.merchantHomePage.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.dueeeke.videoplayer.GifView;
import com.dueeeke.videoplayer.controller.StandardVideoController;
import com.dueeeke.videoplayer.player.IjkVideoView;
import com.dueeeke.videoplayer.player.VideoViewManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.base.BaseOtherActivity;
import leaflet.miaoa.qmxh.leaflet_simple.bean.ListActivityBean;
import leaflet.miaoa.qmxh.leaflet_simple.ui.adapter.RedPacketViewHolder;
import leaflet.miaoa.qmxh.leaflet_simple.ui.widget.CustomDialog;

import static leaflet.miaoa.qmxh.leaflet_simple.Login.WelcomeActivity.Usertel;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.getAdverById;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.initialTimeOutMs;
import static leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mine.SellerSettingActivity.auto;
import static leaflet.miaoa.qmxh.leaflet_simple.utils.DateUtils.getStr3FromDate;
import static leaflet.miaoa.qmxh.leaflet_simple.utils.DateUtils.getTimeFromLong;

public class SellerHomeAdvDetailActivity extends BaseOtherActivity {
    private Activity activity;
    private RelativeLayout iv_back;
    private RelativeLayout rl1;
    private StandardVideoController controller;
    private IjkVideoView ijkVideoView;
    private TextView tv_playback;
    private TextView tv_time;
    private TextView tv_red_packet_time;
    private TextView adv_content;
    private GifView redtime;
    private CountDownTimer countDownTimer;
    private  String aId="",isread="";
    private int position=0;
    private List<ListActivityBean.Adv> advList = new ArrayList<ListActivityBean.Adv>();
    private int totalTime=0;
    private View mRedPacketDialogView;
    private RedPacketViewHolder mRedPacketViewHolder;
    private CustomDialog mRedPacketDialog;
    private Boolean isPlayA=true;

    Handler handler=new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                        isPlayA=false;
                        rl1.setVisibility(View.GONE);

                    bindEvents();
                    break;

            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_home_adv_detail);
        Intent intent=getIntent();
        activity=this;
        aId=intent.getStringExtra("aId");
        position=intent.getIntExtra("position",0);
        initView();
        getAdverById();

    }

    @Override
    protected void onPause() {
        super.onPause();
        IjkVideoView currentVideoPlayer = VideoViewManager.instance().getCurrentVideoPlayer();
        if (currentVideoPlayer != null){
            currentVideoPlayer.release();
        }
    }
    @Override
    public void onBackPressed() {
        if (!VideoViewManager.instance().onBackPressed()){
            super.onBackPressed();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        iv_back = (RelativeLayout) findViewById(R.id.iv_back);
        rl1 = (RelativeLayout) findViewById(R.id.rl1);
        ijkVideoView =(IjkVideoView) findViewById(R.id.video_player);
        tv_playback = (TextView) findViewById(R.id.tv_playback);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_red_packet_time = (TextView) findViewById(R.id.tv_red_packet_time);
        adv_content = (TextView) findViewById(R.id.adv_content);
        redtime = (GifView) findViewById(R.id.iv_redtime);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                finish();
            }
        });
    }
    private void bindEvents(){
        int widthPixels = this.getResources().getDisplayMetrics().widthPixels;
        ijkVideoView.setLayoutParams(new LinearLayout.LayoutParams(widthPixels, widthPixels / 16 * 9));
        controller = new StandardVideoController(this);
        ijkVideoView.setVideoController(controller);
        ijkVideoView.enableCache()
                .autoRotate()
                .useAndroidMediaPlayer()
                .addToPlayerManager()
                .setUrl(advList.get(0).getaMatter())
                .setTitle("")
                .setVideoController(controller);
        Glide.with(this)
                .load( advList.get(0).getaCover())
                .into(controller.getThumb());
        if(auto){
            ijkVideoView.start();
        }
        tv_playback.setText(advList.get(0).getWatchCount());
        adv_content.setText(advList.get(0).getaContent());

        tv_time.setText(getStr3FromDate(getTimeFromLong(advList.get(0).getUploadBegin()))+"-"+getStr3FromDate(getTimeFromLong(advList.get(0).getUploadEnd())));
//        if("false".equals(isPlay)){
//            isPlayA=false;
//            rl1.setVisibility(View.GONE);
//        }else {
////            redpacket_count_down(60000);
//
//        }


    }



    private void getAdverById(){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                getAdverById+"?aId="+aId +"&uNum="+Usertel ,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            ListActivityBean listActivityBean=new ListActivityBean();
                                String aCover=jsonObject.getString("aCover");
                                String aContent=jsonObject.getString("aContent");
                                String aMatter=jsonObject.getString("aMatter");
                                String watchCount=jsonObject.getString("watchCount");
                                String aPrice=jsonObject.getString("aPrice");
                                Long aResidue=jsonObject.getLong("aResidue");
                                String ifRead=jsonObject.getString("ifRead");
                                String uName=jsonObject.getString("uNick");
                                String uImg=jsonObject.getString("uImg");
                                Long uploadBegin=jsonObject.getLong("uploadBegin");
                                Long uploadEnd=jsonObject.getLong("uploadEnd");
                                ListActivityBean.Adv adv=listActivityBean.new Adv();

                                adv.setaCover(aCover);
                                adv.setaContent(aContent);
                                adv.setaMatter(aMatter);
                                adv.setWatchCount(watchCount);
                                adv.setaPrice(aPrice);
                                adv.setuImg(uImg);
                                adv.setuName(uName);
                                adv.setUploadBegin(uploadBegin);
                                adv.setUploadEnd(uploadEnd);
                                if(aResidue>0&&"false".equals(ifRead)){
                                    adv.setRedpacket(true);
                                }else {
                                    adv.setRedpacket(false);
                                }
                                advList.add(adv);
                            Message message=new Message();
                            message.what=1;
                            handler.sendMessage(message);


                        } catch (Exception e) {

                            Toast.makeText(SellerHomeAdvDetailActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(SellerHomeAdvDetailActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeOutMs,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }




}
