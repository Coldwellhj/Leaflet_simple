package leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
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
import com.dueeeke.videoplayer.AdvancedCountdownTimer;
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
import leaflet.miaoa.qmxh.leaflet_simple.bean.RedPacketEntity;
import leaflet.miaoa.qmxh.leaflet_simple.ui.Interface.OnRedPacketDialogClickListener;
import leaflet.miaoa.qmxh.leaflet_simple.ui.adapter.RedPacketViewHolder;
import leaflet.miaoa.qmxh.leaflet_simple.ui.widget.CustomDialog;
import leaflet.miaoa.qmxh.leaflet_simple.utils.ToastUtils;

import static leaflet.miaoa.qmxh.leaflet_simple.Login.WelcomeActivity.Usertel;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.adverNoResiude;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.getAdverById;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.getAdverResidueById;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.initialTimeOutMs;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.updateOfReduceReplacee;
import static leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mine.SellerSettingActivity.auto;
import static leaflet.miaoa.qmxh.leaflet_simple.utils.DateUtils.getStr3FromDate;
import static leaflet.miaoa.qmxh.leaflet_simple.utils.DateUtils.getTimeFromLong;

public class PersonalHomeAdvDetailActivity extends BaseOtherActivity {
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
    private AdvancedCountdownTimer countDownTimer;
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
                    totalTime= advList.get(0).getTimeCount();
                    if(advList.get(0).isRedpacket()){
                        isPlayA=true;
                        rl1.setVisibility(View.VISIBLE);
                    }else {
                        isPlayA=false;
                        rl1.setVisibility(View.GONE);
                    }
                    bindEvents();
                    break;
                case 2:

                    isread="true";
                    if(isPlayA){

                        redpacket_count_down();
                    }else {
                        adverNoResiude();
                        rl1.setVisibility(View.GONE);
                    }
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
                Intent intent=new Intent();
                intent.putExtra("isplay", isPlayA);
                intent.putExtra("position", position);
                intent.putExtra("isread", isread);
                setResult(RESULT_OK, intent);//回传数据到主Activity

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



        Runnable runnable= new Runnable() {
            @Override
            public void run() {
//
                boolean playVideo=false;
                while (!playVideo){
                    playVideo=controller.getPlayVideo();
                }

                Message message=new Message();
                message.what=2;

                handler.sendMessage(message);

            }
        };
        Thread thread=new Thread(runnable);
        thread.start();
    }
//    public GetDurationCallBack MyCallBack(){
//        getDurationCallBack=this;
//        return getDurationCallBack;
//    }
    //红包倒计时
    public  void redpacket_count_down(){
        redtime.setMovieResource(R.drawable.loading);
        if (countDownTimer == null){
            countDownTimer = new AdvancedCountdownTimer(totalTime*1000,1000) {


                @Override
                public void onTick(long millisUntilFinished, int percent) {
                    if(!activity.isFinishing()){
                        String time = millisUntilFinished/1000+"s";
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
                    if(isPlayA){

                        getAdverResidueById(true);
                        redtime.setPaused(true);

                        tv_red_packet_time.setText("0s");
                        tv_red_packet_time.setTextColor(Color.parseColor("#333333"));
                        countDownTimer = null;
                    }else {

                    }

                }

            }.start();
            controller.setCountDownTimer(countDownTimer,redtime);
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
            intent.putExtra("isread", isread);
            setResult(RESULT_OK, intent);//回传数据到主Activity
            finish();
        }
        return false;
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
                                int timeCount=jsonObject.getInt("timeCount");
                                Long uploadBegin=jsonObject.getLong("uploadBegin");
                                Long uploadEnd=jsonObject.getLong("uploadEnd");
                                ListActivityBean.Adv adv=listActivityBean.new Adv();

                                adv.setaCover(aCover);
                                adv.setaContent(aContent);
                                adv.setaMatter(aMatter);
                                adv.setWatchCount(watchCount);
                                adv.setaPrice(aPrice);
                                adv.setuImg(uImg);
                                adv.setTimeCount(timeCount);
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

                            Toast.makeText(PersonalHomeAdvDetailActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(PersonalHomeAdvDetailActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

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
                getAdverResidueById+"?aId="+aId +"&uNum="+Usertel  ,
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
                                Toast.makeText(PersonalHomeAdvDetailActivity.this, "红包已经被领完了", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(PersonalHomeAdvDetailActivity.this, "红包已经被你领过了", Toast.LENGTH_SHORT).show();
                            }



                        } catch (Exception e) {

                            Toast.makeText(PersonalHomeAdvDetailActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(PersonalHomeAdvDetailActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

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
                            if("true".equals(response)){
                                isPlayA=false;

                                rl1.setVisibility(View.GONE);
                               ToastUtils.showShort(PersonalHomeAdvDetailActivity.this,"已存入余额");
                            }else {
                                ToastUtils.showShort(PersonalHomeAdvDetailActivity.this,"领取失败");
                            }



                        } catch (Exception e) {

                            Toast.makeText(PersonalHomeAdvDetailActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(PersonalHomeAdvDetailActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

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

                            Toast.makeText(PersonalHomeAdvDetailActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(PersonalHomeAdvDetailActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeOutMs,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
}
