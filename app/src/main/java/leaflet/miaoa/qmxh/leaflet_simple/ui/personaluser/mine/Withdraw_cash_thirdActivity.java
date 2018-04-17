package leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.base.BaseOtherActivity;
import leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mall.payutil.AuthResult;
import leaflet.miaoa.qmxh.leaflet_simple.utils.ToastUtils;

import static leaflet.miaoa.qmxh.leaflet_simple.Login.WelcomeActivity.Usertel;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.alipayTransToaccount;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.initialTimeOutMs;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.tiXian;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.transToaccount;

public class Withdraw_cash_thirdActivity extends BaseOtherActivity implements View.OnClickListener{

    private RelativeLayout iv_back;
    private TextView my_balance;
    private EditText et_withdraw;
    private TextView withdraw_money;
    private TextView withdraw_all;
    private TextView withdraw_weixin;
    private TextView withdraw_zhifubao;
    private TextView tv2;

    private  String userId=""; //支付宝账户识别
    private  String target_Id=""; //支付宝订单编号
    private BigDecimal balance;
    private BigDecimal    withdraw;//提现的金额
    private BigDecimal actual_amount;//实际到账金额
    private static final int SDK_AUTH_FLAG = 2;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
//                        Toast.makeText(getActivity(),
//                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
//                                .show();
                       tiXian();//提现
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(Withdraw_cash_thirdActivity.this,
                                "授权失败" , Toast.LENGTH_SHORT).show();

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
        setContentView(R.layout.activity_withdraw_cash_third);
        Intent intent=getIntent();
        balance=new BigDecimal(intent.getStringExtra("my_balance"));
        initView();
        init();
    }

    private void init() {

        withdraw_weixin.setClickable(false);
        withdraw_zhifubao.setClickable(false);
        my_balance.setText(""+balance);
        et_withdraw.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
//                Log.e("输入过程中执行该方法", "文字变化");
                //只能输入两位小数
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        et_withdraw.setText(s);
                        et_withdraw.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    et_withdraw.setText(s);
                    et_withdraw.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        et_withdraw.setText(s.subSequence(0, 1));
                        et_withdraw.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // 输入前的监听
//                Log.e("输入前确认执行该方法", "开始输入");

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听
//                Log.e("输入结束执行该方法", "输入结束");

                    if(et_withdraw.getText().toString().trim().length()>0){
                        if(Double.parseDouble(et_withdraw.getText().toString().trim())>0.1){

                        withdraw = new BigDecimal(et_withdraw.getText().toString().trim());
                        BigDecimal ten=new BigDecimal("10.00");
                        BigDecimal a = new BigDecimal("0.1");
                        BigDecimal b = new BigDecimal("0.99");
                        DecimalFormat    df   = new DecimalFormat("######0.00");
                        if(withdraw.compareTo(ten) == -1||withdraw.compareTo(ten) == 0){
                            actual_amount=withdraw.subtract(a);//提现金额小于等于10
                        }else {
                            actual_amount=withdraw.multiply(b);
                        }
                        BigDecimal bl_banlance=balance;
                        if( withdraw.compareTo(bl_banlance) == -1||withdraw.compareTo(bl_banlance) == 0){
                            withdraw_all.setVisibility(View.VISIBLE);
                            tv2.setText("提现到支付宝,");
                            tv2.setTextColor(getResources().getColor(R.color.color_tv_cFormerPrice));
                            withdraw_weixin.setClickable(true);
                            withdraw_zhifubao.setClickable(true);
                            withdraw_weixin.setBackground(getResources().getDrawable(R.drawable.weixin_withdraw_bg));
                            withdraw_zhifubao.setBackground(getResources().getDrawable(R.drawable.zhifubao_withdraw_bg));
                            withdraw_money.setText(actual_amount.setScale(2, BigDecimal.ROUND_DOWN)+"元");
                            my_balance.setText((bl_banlance.subtract(withdraw)).setScale(2, BigDecimal.ROUND_DOWN)+"");

                        }else {
                            tv2.setText("输入金额超过余额");
                            tv2.setTextColor(getResources().getColor(R.color.goods_red));
                            withdraw_all.setVisibility(View.GONE);
                            withdraw_weixin.setClickable(false);
                            withdraw_zhifubao.setClickable(false);
                            withdraw_weixin.setBackground(getResources().getDrawable(R.drawable.weixin_unwithdraw_bg));
                            withdraw_zhifubao.setBackground(getResources().getDrawable(R.drawable.zhifubao_unwithdraw_bg));
                            withdraw_money.setText(actual_amount.setScale(2, BigDecimal.ROUND_DOWN)+"元");
                            my_balance.setText("0.00");
                        }
                        }else {
                            ToastUtils.showShort(Withdraw_cash_thirdActivity.this,"提现金额必须大于0.1元");
                        }
                    }else {
                        withdraw_weixin.setClickable(false);
                        withdraw_zhifubao.setClickable(false);
                        withdraw_weixin.setBackground(getResources().getDrawable(R.drawable.weixin_unwithdraw_bg));
                        withdraw_zhifubao.setBackground(getResources().getDrawable(R.drawable.zhifubao_unwithdraw_bg));
                        withdraw_money.setText("0.00元");
                        actual_amount=new BigDecimal(0.00);
                        my_balance.setText(balance+"");
                    }




            }
        });

    }

    private void initView() {
        iv_back = (RelativeLayout) findViewById(R.id.iv_back);
        my_balance = (TextView) findViewById(R.id.my_balance);
        et_withdraw = (EditText) findViewById(R.id.et_withdraw);
        withdraw_money = (TextView) findViewById(R.id.withdraw_money);
        withdraw_all = (TextView) findViewById(R.id.withdraw_all);
        withdraw_weixin = (TextView) findViewById(R.id.withdraw_weixin);
        withdraw_zhifubao = (TextView) findViewById(R.id.withdraw_zhifubao);
        tv2 = (TextView) findViewById(R.id.tv2);
        iv_back.setOnClickListener(this);
        withdraw_all.setOnClickListener(this);
        withdraw_weixin.setOnClickListener(this);
        withdraw_zhifubao.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_back:
                    finish();
                    break;
                case R.id.withdraw_all:
                    my_balance.setText("0.00");
                    et_withdraw.setText(balance+"");
                    break;
                case R.id.withdraw_weixin:

                    break;
                case R.id.withdraw_zhifubao:
                    alipayTransToaccount();
                    break;
            }
    }
    /**
     * 支付宝账户授权业务
     *
     * @param
     */
    public void withdraw_cash_zhifubao(final String orderInfo) {

        Runnable authRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(Withdraw_cash_thirdActivity.this);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(orderInfo, true);
//                System.out.println("===="+result);
                try{
                    String s=result.get("result");
                    String s1[]=s.split("user_id=");
                    String s2[]=s.split("target_id=");
                    userId=s1[1].substring(0,16);
                    target_Id=s2[1].substring(0);
                }catch (Exception e){

                }

                Message msg = new Message();
                msg.what = SDK_AUTH_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }

    private void alipayTransToaccount(){
        RequestQueue mQueue = Volley.newRequestQueue(Withdraw_cash_thirdActivity.this);
        final StringRequest stringRequest = new StringRequest(
                alipayTransToaccount,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                            withdraw_cash_zhifubao(response);

                        } catch (Exception e) {

                            Toast.makeText(Withdraw_cash_thirdActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Withdraw_cash_thirdActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeOutMs,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
    private void transToaccount(){
        RequestQueue mQueue = Volley.newRequestQueue(Withdraw_cash_thirdActivity.this);
        final StringRequest stringRequest = new StringRequest(
                transToaccount+"?out_biz_no="+target_Id+"&payee_account="+userId+"&amount="+actual_amount+"&uNum="+Usertel+"&money="+withdraw,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                            if("true".equals(response)){
                                setResult(2);
                                finish();
                                ToastUtils.showShort(Withdraw_cash_thirdActivity.this,"提现成功");
                            }else if("false".equals(response)){
                                setResult(2);
                                finish();
                                ToastUtils.showShort(Withdraw_cash_thirdActivity.this,"提现失败");
                            }

                        } catch (Exception e) {
                            finish();
                            Toast.makeText(Withdraw_cash_thirdActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Withdraw_cash_thirdActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeOutMs,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
    private void tiXian(){
        RequestQueue mQueue = Volley.newRequestQueue(Withdraw_cash_thirdActivity.this);
        final StringRequest stringRequest = new StringRequest(
                tiXian+"?uNum="+Usertel+"&money="+withdraw,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                            if("true".equals(response)){
                                transToaccount();
                            }else if("false".equals(response)){
                                ToastUtils.showShort(Withdraw_cash_thirdActivity.this,"提现失败");

                            }

                        } catch (Exception e) {

                            Toast.makeText(Withdraw_cash_thirdActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Withdraw_cash_thirdActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeOutMs,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }


}
