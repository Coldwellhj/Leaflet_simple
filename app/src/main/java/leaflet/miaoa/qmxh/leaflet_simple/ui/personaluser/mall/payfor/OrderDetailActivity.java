package leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mall.payfor;

import android.content.Intent;
import android.os.Bundle;
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
import com.bumptech.glide.Glide;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.base.BaseOtherActivity;
import leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mall.PersonalHomegetGoodsByIdActivity;
import leaflet.miaoa.qmxh.leaflet_simple.utils.Common;
import leaflet.miaoa.qmxh.leaflet_simple.utils.ToastUtils;

import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.applyForRefund;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.getCoinById;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.updateShippingStatusUser;
import static leaflet.miaoa.qmxh.leaflet_simple.utils.DateUtils.getStr4FromDate;
import static leaflet.miaoa.qmxh.leaflet_simple.utils.DateUtils.getTimeFromLong;
import static leaflet.miaoa.qmxh.leaflet_simple.utils.RequestDemo.getTraceInfo;

public class OrderDetailActivity extends BaseOtherActivity implements View.OnClickListener {

    private RelativeLayout iv_back;
    private TextView confirm;
    private TextView remind;
    private TextView refund;
    private TextView repay;
    private static TextView tv_logistics;
    private static TextView tv_logistics_time;
    private RelativeLayout rl_logistics;
    private QMUIRadiusImageView radiusImageView;
    private TextView tv_name;
    private TextView tv_type;
    private TextView tv_price;
    private TextView orderNum;
    private TextView orderTime;
    private RelativeLayout rl_wuliu_style;
    private RelativeLayout rl_order;
    private TextView totalmoney;
    private RelativeLayout rl1;
    private RelativeLayout rl2;
    private TextView totalprice;
    private TextView tv2;
    private String cId;
    private String cName;
    private String buyCount;
    private String attribute;
    private String specification;
    private String payMoney;
    private String orderId;
    private Long buyTime;
    private int shippingStatus;
    private String attributeImg;
    private String buyId;
    private String expressNumber;
    private int ifRefund;
    //    //获取快递信息测试
    public static String response = "";
    public static Handler handler_logistics=new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    response=(String)msg.obj;
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String data=jsonObject.getString("data");
                        JSONArray jsonArray =new JSONArray(data);
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String traces=jsonObject1.getString("traces");
                            JSONObject jsonObject2=new JSONObject(traces);
                            String desc=jsonObject2.getString("desc");
                            String scanDate=jsonObject2.getString("scanDate");
                            tv_logistics.setText(desc);
                            tv_logistics_time.setText(scanDate);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;


            }
            super.handleMessage(msg);
        }
    };
    Handler handler=new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ToastUtils.showShort(OrderDetailActivity.this,"该商品已不存在！");
                    break;


            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Intent intent=getIntent();
        cId=intent.getStringExtra("cId");
        cName=intent.getStringExtra("cName");
        buyCount=intent.getStringExtra("buyCount");
        attribute=intent.getStringExtra("attribute");
        specification=intent.getStringExtra("specification");
        payMoney=intent.getStringExtra("payMoney");
        orderId=intent.getStringExtra("orderId");
        buyTime=intent.getLongExtra("buyTime",0);
        shippingStatus=intent.getIntExtra("shippingStatus",0);
        attributeImg=intent.getStringExtra("attributeImg");
        buyId=intent.getStringExtra("buyId");
        expressNumber=intent.getStringExtra("expressNumber");
        ifRefund=intent.getIntExtra("ifRefund",0);

        initView();
        init();
    }

    private void initView() {
        iv_back = (RelativeLayout) findViewById(R.id.iv_back);
        confirm = (TextView) findViewById(R.id.confirm);
        remind = (TextView) findViewById(R.id.remind);
        refund = (TextView) findViewById(R.id.refund);
        repay = (TextView) findViewById(R.id.repay);
        tv_logistics = (TextView) findViewById(R.id.tv_logistics);
        tv_logistics_time = (TextView) findViewById(R.id.tv_logistics_time);
        rl_logistics = (RelativeLayout) findViewById(R.id.rl_logistics);
        radiusImageView = (QMUIRadiusImageView) findViewById(R.id.radiusImageView);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_price = (TextView) findViewById(R.id.tv_price);
        orderNum = (TextView) findViewById(R.id.orderNum);
        orderTime = (TextView) findViewById(R.id.orderTime);
        rl_wuliu_style = (RelativeLayout) findViewById(R.id.rl_wuliu_style);
        rl_order = (RelativeLayout) findViewById(R.id.rl_order);
        totalmoney = (TextView) findViewById(R.id.totalmoney);
        rl1 = (RelativeLayout) findViewById(R.id.rl1);
        rl2 = (RelativeLayout) findViewById(R.id.rl2);
        totalprice = (TextView) findViewById(R.id.totalprice);
        tv2 = (TextView) findViewById(R.id.tv2);
        iv_back.setOnClickListener(this);
        confirm.setOnClickListener(this);
        remind.setOnClickListener(this);
        refund.setOnClickListener(this);
        repay.setOnClickListener(this);
        rl_logistics.setOnClickListener(this);

    }
    private void init(){
        radiusImageView.setCornerRadius(QMUIDisplayHelper.dp2px(OrderDetailActivity.this, 10));
        radiusImageView.setCircle(false);
        radiusImageView.setOval(false);
        radiusImageView.setTouchSelectModeEnabled(false);

        Glide.with(this)
                .load(attributeImg)
                .into(radiusImageView);
        tv_name.setText(cName);
        tv_type.setText("数量："+buyCount+" "+attribute+":"+specification);
        tv_price.setText("¥"+payMoney);
        totalmoney.setText("¥"+payMoney);
        totalprice.setText("¥"+payMoney);
        orderNum.setText("订单编号："+orderId);
        orderTime.setText("下单时间："+getStr4FromDate(getTimeFromLong(buyTime)));
        if(shippingStatus==0){
            rl_order.setVisibility(View.GONE);
            rl_wuliu_style.setVisibility(View.GONE);
            rl_logistics.setVisibility(View.GONE);
            confirm.setVisibility(View.GONE);
            remind.setVisibility(View.VISIBLE);
            refund.setVisibility(View.VISIBLE);
        }else if(shippingStatus==1){
            rl_order.setVisibility(View.VISIBLE);
            rl_wuliu_style.setVisibility(View.VISIBLE);
            rl_logistics.setVisibility(View.VISIBLE);
            confirm.setVisibility(View.VISIBLE);
            remind.setVisibility(View.GONE);
            refund.setVisibility(View.VISIBLE);
        }else if(shippingStatus==2){
            rl_order.setVisibility(View.VISIBLE);
            rl_wuliu_style.setVisibility(View.VISIBLE);
            rl_logistics.setVisibility(View.VISIBLE);
            confirm.setVisibility(View.VISIBLE);
            remind.setVisibility(View.GONE);
            refund.setVisibility(View.VISIBLE);

        }else {
            rl_order.setVisibility(View.VISIBLE);
            rl_wuliu_style.setVisibility(View.VISIBLE);
            rl_logistics.setVisibility(View.VISIBLE);
            confirm.setVisibility(View.VISIBLE);
            remind.setVisibility(View.GONE);
            refund.setVisibility(View.GONE);
            confirm.setText("已收货");
            confirm.setClickable(false);
        }
        if(ifRefund==0){

        }else if(ifRefund==1){
            refund.setText("处理中");
            refund.setClickable(false);
            confirm.setVisibility(View.GONE);
            remind.setVisibility(View.GONE);
        }else {
            refund.setText("处理成功");
            refund.setClickable(false);
            confirm.setVisibility(View.GONE);
            remind.setVisibility(View.GONE);
        }
//        //获取快递信息

        getTraceInfo("['"+expressNumber+"']","TRACEINTERFACE_LATEST");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:

                finish();
                break;
            case R.id.rl_logistics:

                Intent intent=new Intent(OrderDetailActivity.this,Logistics_informationActivity.class);
                intent.putExtra("expressNumber",expressNumber);
                startActivity(intent);
                break;
            case R.id.confirm:
                confirm.setText("已收货");
                confirm.setClickable(false);
                updateShippingStatusUser(buyId);
                ToastUtils.showShort(OrderDetailActivity.this, "收货成功");
                break;
            case R.id.remind:
                ToastUtils.showShort(OrderDetailActivity.this,"催单成功");
                break;
            case R.id.refund:
                refund.setText("处理中");
                refund.setClickable(false);
                applyForRefund(buyId);
                ToastUtils.showShort(OrderDetailActivity.this, "已提交申请");
                break;
            case R.id.repay:
                httpGetCoinById();

                break;
        }
    }

    private void applyForRefund(String buyId) {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                applyForRefund + "?buyId=" + buyId,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {


                        } catch (Exception e) {
                            Toast.makeText(OrderDetailActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OrderDetailActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }

    private void updateShippingStatusUser(String buyId) {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                updateShippingStatusUser + "?buyId=" + buyId,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {


                        } catch (Exception e) {
                            Toast.makeText(OrderDetailActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OrderDetailActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
    private void httpGetCoinById(){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                getCoinById+"?cId="+cId  ,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                            if(Common.isNOT_Null(response)){
                                Intent intent1=new Intent(OrderDetailActivity.this, PersonalHomegetGoodsByIdActivity.class);
                                intent1.putExtra("cId",cId);
                                startActivity(intent1);
                            }else {
                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);
                            }

                        } catch (Exception e) {

                            Toast.makeText(OrderDetailActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(OrderDetailActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
}
