package leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mall.payfor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.base.BaseOtherActivity;
import leaflet.miaoa.qmxh.leaflet_simple.bean.Constants;
import leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mall.payutil.AuthResult;
import leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mall.payutil.PayResult;

import static leaflet.miaoa.qmxh.leaflet_simple.Login.WelcomeActivity.Body;
import static leaflet.miaoa.qmxh.leaflet_simple.Login.WelcomeActivity.Usertel;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.WeChatPay;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.alipay;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.alipayRefund;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.buyCoinGoodsAlipay;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.initialTimeOutMs;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.insertCoinBuy;

public class ThirdPayActivity extends BaseOtherActivity implements View.OnClickListener{

    private RelativeLayout iv_back;
    private TextView pay_totalmoney;
    private TextView tv_payOrder;
    private TextView tv_Balance_deduction;
    private RelativeLayout rl_button;
    private ImageView iv_wx_checked;
    private RelativeLayout rl_weixin;
    private ImageView iv_zhifubao_unchecked;
    private RelativeLayout rl_zhifubao;
    private boolean weixinPay=true;
    private boolean zhifubaoPay=false;
    private  String trade_no="";
    private  String out_trade_no="";
   private String cName="";
   private String specification="";
   private String attributeId="";
   private String addressId="";
   private int buyCount=0;
   private String leaveWord="";
   private String totalprice;
   private String mybalance="0";
   private Boolean isUseBalance=true;

    private IWXAPI msgApi;
    private PayReq req;
    public String partnerId;
    public String prepayId;
    public String nonceStr;
    public String timeStamp;
    public String packageValue;
    public String sign;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。

                        buyCoinGoodsAlipay();
//                        Toast.makeText(ConfirmOrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(ThirdPayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(ThirdPayActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(ThirdPayActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);//沙箱环境
        setContentView(R.layout.activity_third_pay);
        //微信支付
        msgApi = WXAPIFactory.createWXAPI(this, null);
        req=new PayReq();
        Intent intent=getIntent();
        cName=intent.getStringExtra("cName");
        specification=intent.getStringExtra("specification");
        attributeId=intent.getStringExtra("attributeId");
        addressId=intent.getStringExtra("addressId");
        buyCount=intent.getIntExtra("buyCount",0);
        leaveWord=intent.getStringExtra("leaveWord");
        totalprice=intent.getStringExtra("totalprice");
        mybalance=intent.getStringExtra("mybalance");
        isUseBalance=intent.getBooleanExtra("isUseBalance",true);
        initView();
        pay_totalmoney.setText(totalprice+"");
    }

    private void initView() {
        iv_back = (RelativeLayout) findViewById(R.id.iv_back);
        pay_totalmoney = (TextView) findViewById(R.id.pay_totalmoney);
        tv_payOrder = (TextView) findViewById(R.id.tv_payOrder);
        tv_Balance_deduction = (TextView) findViewById(R.id.tv_Balance_deduction);
        rl_button = (RelativeLayout) findViewById(R.id.rl_button);
        iv_wx_checked = (ImageView) findViewById(R.id.iv_wx_checked);
        rl_weixin = (RelativeLayout) findViewById(R.id.rl_weixin);
        iv_zhifubao_unchecked = (ImageView) findViewById(R.id.iv_zhifubao_unchecked);
        rl_zhifubao = (RelativeLayout) findViewById(R.id.rl_zhifubao);
        iv_back.setOnClickListener(this);
        rl_weixin.setOnClickListener(this);
        rl_zhifubao.setOnClickListener(this);
        tv_payOrder.setOnClickListener(this);

    }
    @Override
    protected void onResume() {
        super.onResume();
        tv_payOrder.setClickable(true);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_weixin:
                weixinPay=true;
                zhifubaoPay=false;
                iv_wx_checked.setImageDrawable(getResources().getDrawable(R.mipmap.checked));
                iv_zhifubao_unchecked.setImageDrawable(getResources().getDrawable(R.mipmap.unchecked));
                break;
            case R.id.rl_zhifubao:
                weixinPay=false;
                zhifubaoPay=true;
                iv_wx_checked.setImageDrawable(getResources().getDrawable(R.mipmap.unchecked));
                iv_zhifubao_unchecked.setImageDrawable(getResources().getDrawable(R.mipmap.checked));
                break;
            case R.id.tv_payOrder:
                if(weixinPay){
                    //调微信支付
                    tv_payOrder.setClickable(false);
                    insertCoinBuy();
                }else if(zhifubaoPay){
                    //调支付宝支付
                    tv_payOrder.setClickable(false);
                    alipay();
                }
                break;

        }
    }
    /**
     * 支付宝支付业务
     *
     * @param
     */
    public void pay_zhifubao(final String orderInfo) {
//        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
//            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialoginterface, int i) {
//                            //
//                            finish();
//                        }
//                    }).show();
//            return;
//        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
//        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
//        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
//        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
//
//        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
//        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
//        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(ThirdPayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());
                String s=result.get("result");
                try {
                    JSONObject jsonObject =new JSONObject(s);
                    String alipay_trade_app_pay_response=jsonObject.getString("alipay_trade_app_pay_response");
                    JSONObject jsonObject1 =new JSONObject(alipay_trade_app_pay_response);
                    trade_no=jsonObject1.getString("trade_no");
                    out_trade_no=jsonObject1.getString("out_trade_no");
//                    System.out.println("============trade_no:"+trade_no+"======out_trade_no:"+out_trade_no);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
    private void alipay(){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                alipay+"?subject=" + cName+"("+specification+")"+"&total_amount="+totalprice,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                            pay_zhifubao(response);

                        } catch (Exception e) {

                            Toast.makeText(ThirdPayActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ThirdPayActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeOutMs,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
    private void WxPay(String out_trade_no){
        Body= cName+"("+specification+")";
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                WeChatPay+"?weChatMoney="+totalprice+"&body="+ cName+"("+specification+")"+"&out_trade_no="+out_trade_no ,
//
                new Response.Listener<String>() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject1 = new JSONObject(response);

                            partnerId=jsonObject1.getString("mch_id");
                            prepayId=jsonObject1.getString("prepay_id");
                            packageValue=jsonObject1.getString("package");
                            nonceStr=jsonObject1.getString("nonceStr");
                            sign=jsonObject1.getString("paySign");
                            timeStamp=jsonObject1.getString("timeStamp");

                            req.appId = Constants.APP_ID;
                            req.partnerId = partnerId;
                            req.prepayId = prepayId;

                            req.packageValue = packageValue;

                            req.nonceStr = nonceStr;
                            req.timeStamp =timeStamp;
                            req.sign =sign;
                            //调起微信支付
                            msgApi.registerApp(Constants.APP_ID);
                            msgApi.sendReq(req);




                        } catch (Exception e) {
                            Toast.makeText(ThirdPayActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ThirdPayActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
    private void buyCoinGoodsAlipay(){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                buyCoinGoodsAlipay+"?uNum=" + Usertel+"&attributeId="+attributeId+"&buyAddress="+
                        addressId+"&buyCount="+buyCount+"&leaveWord="+leaveWord+"&deductionFlag="+isUseBalance+"&out_trade_no="+out_trade_no,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                            if("true".equals(response)){
                                finish();
                                Toast.makeText(ThirdPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(ThirdPayActivity.this,BuyAfterActivity.class);
                                startActivity(intent);
                            }else {
                                finish();
                                Toast.makeText(ThirdPayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                                alipayRefund();
                            }

                        } catch (Exception e) {

                            Toast.makeText(ThirdPayActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ThirdPayActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeOutMs,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }

    private void insertCoinBuy(){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                insertCoinBuy+"?uNum=" + Usertel+"&attributeId="+attributeId+"&buyAddress="+
                        addressId+"&buyCount="+buyCount+"&leaveWord="+leaveWord+"&deductionFlag="+isUseBalance,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            String s[]=response.split("<-->");
                            System.out.println("=========="+"true".equals(s[0]));
                            if("true".equals(s[0])){
                                WxPay(s[1]);
                            }

                        } catch (Exception e) {

                            Toast.makeText(ThirdPayActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ThirdPayActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeOutMs,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
    private void alipayRefund(){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                alipayRefund+"?subject=" + cName+"("+specification+")"+"&total_amount="+totalprice+"&trade_no="+trade_no+"&out_trade_no="+out_trade_no+"&balancePay="+mybalance+"&uNum=" + Usertel,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {



                        } catch (Exception e) {

//                            Toast.makeText(ThirdPayActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Toast.makeText(ThirdPayActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeOutMs,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
}
