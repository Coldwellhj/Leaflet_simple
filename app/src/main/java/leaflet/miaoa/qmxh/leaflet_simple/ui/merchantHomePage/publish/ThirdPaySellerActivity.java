package leaflet.miaoa.qmxh.leaflet_simple.ui.merchantHomePage.publish;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
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
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Map;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.base.BaseOtherActivity;
import leaflet.miaoa.qmxh.leaflet_simple.bean.Constants;
import leaflet.miaoa.qmxh.leaflet_simple.ui.Interface.OnPasswordPayClickListener;
import leaflet.miaoa.qmxh.leaflet_simple.ui.merchantHomePage.SellerHomePageActivity;
import leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mall.payfor.ConfirmOrderActivity;
import leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mall.payfor.ThirdPayActivity;
import leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mall.payutil.AuthResult;
import leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mall.payutil.PayResult;
import leaflet.miaoa.qmxh.leaflet_simple.ui.widget.PopEnterPassword;

import static leaflet.miaoa.qmxh.leaflet_simple.Login.WelcomeActivity.Body;
import static leaflet.miaoa.qmxh.leaflet_simple.Login.WelcomeActivity.Usertel;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.WeChatPay;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.addAdver;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.addAdverAlipay;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.alipay;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.alipayRefund;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.checkUserPayWord;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.deleteNewAdverByNum;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.initialTimeOutMs;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.useraction_balance;

public class ThirdPaySellerActivity extends BaseOtherActivity implements OnPasswordPayClickListener,View.OnClickListener{

    private RelativeLayout iv_back;
    private TextView pay_totalmoney;
    private TextView tv_payOrder;
    private TextView tv_Balance_deduction;
    private RelativeLayout rl_button;
    private ImageView iv_bl_checked;
    private ImageView iv_wx_checked;
    private RelativeLayout rl_balance;
    private RelativeLayout rl_weixin;
    private ImageView iv_zhifubao_unchecked;
    private RelativeLayout rl_zhifubao;
    private boolean balance=true;
    private boolean weixinPay=false;
    private boolean zhifubaoPay=false;
    private  String trade_no="";
    private  String out_trade_no="";
    private  String out_trade_no_wx="";

   private String totalprice="";
   private String currentTime="";
   private String cover_charge="";



    private IWXAPI msgApi;
    private PayReq req;
    public String partnerId;
    public String prepayId;
    public String nonceStr;
    public String timeStamp;
    public String packageValue;
    public String sign;
    private BigDecimal mybalance;
    private QMUITipDialog LondingDialog;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private QMUITipDialog LondingDialog2;
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
                        Toast.makeText(ThirdPaySellerActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ThirdPaySellerActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(ThirdPaySellerActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        };
    };    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);//沙箱环境
        setContentView(R.layout.activity_seller_third_pay);
        //微信支付
        msgApi = WXAPIFactory.createWXAPI(this, null);
        req=new PayReq();
        useraction_balance();//获取用户余额
        LondingDialog = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("加载中")
                .create();

        LondingDialog.show();
        Intent intent=getIntent();



        totalprice=intent.getStringExtra("payMoney");
        currentTime=intent.getStringExtra("currentTime");
        out_trade_no_wx=intent.getStringExtra("out_trade_no");
        cover_charge=intent.getStringExtra("cover_charge");
        initView();


    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_payOrder.setClickable(true);
    }

    private void initView() {
        iv_back = (RelativeLayout) findViewById(R.id.iv_back);
        pay_totalmoney = (TextView) findViewById(R.id.pay_totalmoney);
        tv_payOrder = (TextView) findViewById(R.id.tv_payOrder);
        tv_Balance_deduction = (TextView) findViewById(R.id.tv_Balance_deduction);
        rl_button = (RelativeLayout) findViewById(R.id.rl_button);
        iv_bl_checked = (ImageView) findViewById(R.id.iv_bl_checked);
        iv_wx_checked = (ImageView) findViewById(R.id.iv_wx_checked);
        rl_balance = (RelativeLayout) findViewById(R.id.rl_balance);
        rl_weixin = (RelativeLayout) findViewById(R.id.rl_weixin);
        iv_zhifubao_unchecked = (ImageView) findViewById(R.id.iv_zhifubao_unchecked);
        rl_zhifubao = (RelativeLayout) findViewById(R.id.rl_zhifubao);
        iv_back.setOnClickListener(this);
        rl_balance.setOnClickListener(this);
        rl_weixin.setOnClickListener(this);
        rl_zhifubao.setOnClickListener(this);
        tv_payOrder.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                deleteNewAdverByNum();
                break;
            case R.id.rl_balance:
                if(mybalance.compareTo(new BigDecimal(totalprice))==-1){
                    tv_payOrder.setBackground(getResources().getDrawable(R.drawable.zhifuuncheckedbutton_bg));
                    tv_payOrder.setClickable(false);
                    balance=true;
                    weixinPay=false;
                    zhifubaoPay=false;
                    iv_bl_checked.setImageDrawable(getResources().getDrawable(R.mipmap.checked));
                    iv_wx_checked.setImageDrawable(getResources().getDrawable(R.mipmap.unchecked));
                    iv_zhifubao_unchecked.setImageDrawable(getResources().getDrawable(R.mipmap.unchecked));
                    pay_totalmoney.setText( new BigDecimal(totalprice).subtract(new BigDecimal(cover_charge))+"元"+"（余额剩余"+mybalance+"元）");
                }else {
                    tv_payOrder.setClickable(true);
                    balance=true;
                    weixinPay=false;
                    zhifubaoPay=false;
                    iv_bl_checked.setImageDrawable(getResources().getDrawable(R.mipmap.checked));
                    iv_wx_checked.setImageDrawable(getResources().getDrawable(R.mipmap.unchecked));
                    iv_zhifubao_unchecked.setImageDrawable(getResources().getDrawable(R.mipmap.unchecked));
                    pay_totalmoney.setText( new BigDecimal(totalprice).subtract(new BigDecimal(cover_charge))+"元");
                }

                break;
            case R.id.rl_weixin:
                tv_payOrder.setBackground(getResources().getDrawable(R.drawable.zhifubutton_bg));
                tv_payOrder.setClickable(true);
                balance=false;
                weixinPay=true;
                zhifubaoPay=false;
                iv_bl_checked.setImageDrawable(getResources().getDrawable(R.mipmap.unchecked));
                iv_wx_checked.setImageDrawable(getResources().getDrawable(R.mipmap.checked));
                iv_zhifubao_unchecked.setImageDrawable(getResources().getDrawable(R.mipmap.unchecked));
                pay_totalmoney.setText(totalprice+"（包含手续费"+cover_charge+"元）");
                break;
            case R.id.rl_zhifubao:
                tv_payOrder.setBackground(getResources().getDrawable(R.drawable.zhifubutton_bg));
                tv_payOrder.setClickable(true);
                weixinPay=false;
                balance=false;
                zhifubaoPay=true;
                iv_bl_checked.setImageDrawable(getResources().getDrawable(R.mipmap.unchecked));
                iv_wx_checked.setImageDrawable(getResources().getDrawable(R.mipmap.unchecked));
                iv_zhifubao_unchecked.setImageDrawable(getResources().getDrawable(R.mipmap.checked));
                pay_totalmoney.setText(totalprice+"（包含手续费"+cover_charge+"元）");
                break;
            case R.id.tv_payOrder:
                if(balance){
                    //调余额支付
                    tv_payOrder.setClickable(false);
                    LondingDialog2= new QMUITipDialog.Builder(this)
                            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                            .setTipWord("提交中···")
                            .create();

                    LondingDialog2.show();
                    checkUserPayWord(false);

                }else if(weixinPay){
                    //调微信支付
                    tv_payOrder.setClickable(false);

                    WxPay();
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
                PayTask alipay = new PayTask(ThirdPaySellerActivity.this);
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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            deleteNewAdverByNum();
           finish();


        }
        return false;
    }
    private void alipay(){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                alipay+"?subject=" + "广告支付"+"&total_amount="+totalprice,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                            pay_zhifubao(response);

                        } catch (Exception e) {

                            Toast.makeText(ThirdPaySellerActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ThirdPaySellerActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

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
                alipayRefund+"?subject=" +""+"&total_amount="+totalprice+"&trade_no="+trade_no+"&out_trade_no="+out_trade_no+"&balancePay=0"+"&uNum=" + Usertel,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {



                        } catch (Exception e) {

//                            Toast.makeText(ThirdPaySellerActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Toast.makeText(ThirdPaySellerActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeOutMs,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }

    private void WxPay(){
        Body="广告支付";
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                WeChatPay+"?weChatMoney="+totalprice+"&body=广告支付"+"&out_trade_no="+out_trade_no_wx  ,
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
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
    //支付宝付款
    private void buyCoinGoodsAlipay(){

        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                addAdverAlipay+"?aNum=" + Usertel+"&balanceMoney=0"+"&alipayMoney="+totalprice+"&timeStamp="+currentTime,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                            if("true".equals(response)){
                                finish();
                                Toast.makeText(ThirdPaySellerActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(ThirdPaySellerActivity.this,SellerHomePageActivity.class);
                                intent.putExtra("pay","true");
                                startActivity(intent);
                            }else {
                                finish();
                                Toast.makeText(ThirdPaySellerActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                                alipayRefund();
                            }

                        } catch (Exception e) {

                            Toast.makeText(ThirdPaySellerActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ThirdPaySellerActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeOutMs,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }

    //余额付款
    private void addAdver(){

        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                addAdver+"?aNum=" + Usertel+"&balanceMoney="+new BigDecimal(totalprice).subtract(new BigDecimal(cover_charge))+"&timeStamp="+currentTime,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                            if("true".equals(response)){
                                finish();
                                Toast.makeText(ThirdPaySellerActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(ThirdPaySellerActivity.this,SellerHomePageActivity.class);
                                intent.putExtra("pay","true");
                                startActivity(intent);
                            }else {
                                Toast.makeText(ThirdPaySellerActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {

                            Toast.makeText(ThirdPaySellerActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ThirdPaySellerActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeOutMs,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
    //取消付款
    private void deleteNewAdverByNum(){

        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                deleteNewAdverByNum+"?aNum=" + Usertel+"&timeStamp="+currentTime,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {



                        } catch (Exception e) {

                            Toast.makeText(ThirdPaySellerActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ThirdPaySellerActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeOutMs,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }

    private void useraction_balance(){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                useraction_balance+"?uNum=" + Usertel,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            mybalance=new BigDecimal(response);
                            if(mybalance.compareTo(new BigDecimal(totalprice))==-1){
                                tv_payOrder.setBackground(getResources().getDrawable(R.drawable.zhifuuncheckedbutton_bg));
                                tv_payOrder.setClickable(false);
                                balance=true;
                                weixinPay=false;
                                zhifubaoPay=false;
                                iv_bl_checked.setImageDrawable(getResources().getDrawable(R.mipmap.checked));
                                iv_wx_checked.setImageDrawable(getResources().getDrawable(R.mipmap.unchecked));
                                iv_zhifubao_unchecked.setImageDrawable(getResources().getDrawable(R.mipmap.unchecked));
                                pay_totalmoney.setText( new BigDecimal(totalprice).subtract(new BigDecimal(cover_charge))+"元"+"（余额剩余"+mybalance+"元）");
                            }else {
                                tv_payOrder.setClickable(true);
                                pay_totalmoney.setText( new BigDecimal(totalprice).subtract(new BigDecimal(cover_charge))+"元");
                            }
                        } catch (Exception e) {
                            Toast.makeText(ThirdPaySellerActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                        LondingDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LondingDialog.dismiss();
                Toast.makeText(ThirdPaySellerActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeOutMs,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
    //获取支付密码
    private void checkUserPayWord(final Boolean flag){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                checkUserPayWord+"?uNum=" + Usertel,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        LondingDialog2.dismiss();

                        PopEnterPassword popEnterPassword = new PopEnterPassword(ThirdPaySellerActivity.this,ThirdPaySellerActivity.this,response,flag);
//                        PopEnterPassword popEnterPassword = new PopEnterPassword(ConfirmOrderActivity.this);
                        // 显示窗口
                        popEnterPassword.showAtLocation(ThirdPaySellerActivity.this.findViewById(R.id.layoutContent),
                                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LondingDialog2.dismiss();
                Toast.makeText(ThirdPaySellerActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeOutMs,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
    /*
       flag:ture 余额支付
       flag:false 混合支付
    */
    @Override
    public void psdSuccess(Boolean flag) {
        if(flag){
            //余额支付
            addAdver();
        }else {

        }
    }
}
