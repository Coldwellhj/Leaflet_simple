package leaflet.miaoa.qmxh.leaflet_simple.ui.merchantHomePage.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
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
import leaflet.miaoa.qmxh.leaflet_simple.base.BaseOtherActivity;
import leaflet.miaoa.qmxh.leaflet_simple.bean.ListActivityBean;
import leaflet.miaoa.qmxh.leaflet_simple.ui.adapter.RedPacketViewHolder;
import leaflet.miaoa.qmxh.leaflet_simple.ui.widget.CustomDialog;
import leaflet.miaoa.qmxh.leaflet_simple.ui.widget.GlideImageLoader;

import static leaflet.miaoa.qmxh.leaflet_simple.Login.WelcomeActivity.Usertel;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.getAdverById;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.initialTimeOutMs;

public class SellerHomeAdvPictureDetailActivity extends BaseOtherActivity {
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

                        isPlayA = false;
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
                            String aCover=jsonObject.getString("aCover");
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

                            Toast.makeText(SellerHomeAdvPictureDetailActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(SellerHomeAdvPictureDetailActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeOutMs,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }


}
