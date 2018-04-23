package leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mall.payfor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.json.JSONObject;

import java.math.BigDecimal;

import cn.jpush.android.api.JPushInterface;
import leaflet.miaoa.qmxh.leaflet_simple.Login.LoginActivity;
import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.base.BaseOtherActivity;
import leaflet.miaoa.qmxh.leaflet_simple.bean.ListActivityBean;
import leaflet.miaoa.qmxh.leaflet_simple.ui.Interface.OnPasswordPayClickListener;
import leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.PersonalUserHomePageActivity;
import leaflet.miaoa.qmxh.leaflet_simple.ui.widget.AmountView;
import leaflet.miaoa.qmxh.leaflet_simple.ui.widget.PopEnterPassword;
import leaflet.miaoa.qmxh.leaflet_simple.utils.Common;
import leaflet.miaoa.qmxh.leaflet_simple.utils.ToastUtils;
import leaflet.miaoa.qmxh.leaflet_simple.wxapi.WXPayEntryActivity;

import static leaflet.miaoa.qmxh.leaflet_simple.Login.WelcomeActivity.Usertel;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.buyCoinGoods;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.checkUserPayWord;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.getDefaultAddress;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.initialTimeOutMs;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.useraction_balance;

public class ConfirmOrderActivity extends BaseOtherActivity implements OnPasswordPayClickListener,View.OnClickListener {
    private RelativeLayout iv_back;
    private TextView pay_totalmoney;
    private TextView tv_payOrder;
    private TextView tv_Balance_deduction;
    private ImageView iv1;
    private ImageView location;
    private ImageView turn_right;
    private TextView tv_address;
    private TextView user_name;
    private RelativeLayout rl_change_address;
    private QMUIRadiusImageView radiusImageView;
    private TextView tv_name;
    private TextView tv_type;
    private TextView tv_price;
    private TextView totalmoney;
    private EditText problem;
    private ToggleButton mBalanceTogBtn;
    private TextView useraction_;
    private String cName;
    private String attributeId;
    private String attribute;
    private String specification;
    private int goodNumber;
    private int totalNumber;
    private String attributeImg;
    private BigDecimal totalprice;
    private Double attributePrice;
    private Boolean isuse_balance=true;
    private BigDecimal mybalance;
    private String addressId="";
    private AmountView amount_view;
    private QMUITipDialog LondingDialog;
    private QMUITipDialog LondingDialog1;
    private QMUITipDialog LondingDialog2;
    ListActivityBean listActivityBean=new ListActivityBean();
    ListActivityBean.Address address=listActivityBean.new Address();



    Handler handler=new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if(isuse_balance){
                        mybalance=new BigDecimal((String)msg.obj);
                        useraction_.setText(""+mybalance);
                        //判断余额是否充足
                        if(totalprice.compareTo(mybalance) == 1){
                            tv_Balance_deduction.setText("余额已抵扣 ¥"+mybalance.setScale(2, BigDecimal.ROUND_HALF_UP));
                            pay_totalmoney.setText("¥"+totalprice.subtract(mybalance).setScale(2, BigDecimal.ROUND_HALF_UP));
                        }else {
                            tv_Balance_deduction.setText("余额已抵扣 ¥"+totalprice.setScale(2, BigDecimal.ROUND_HALF_UP));
                            pay_totalmoney.setText("¥"+totalprice.subtract(totalprice).setScale(2, BigDecimal.ROUND_HALF_UP));
                        }
                    }else {
                        tv_Balance_deduction.setText("");
                        pay_totalmoney.setText("¥"+totalprice.setScale(2, BigDecimal.ROUND_HALF_UP));
                    }

                    break;
                case 2:
                    tv_address.setText(address.getConsigneeAddress());
                    user_name.setText(address.getConsigneeName()+"  "+address.getConsigneeNum());
                    break;
                case 3:
                    if(isuse_balance){

                        //判断余额是否充足
                        if(totalprice.compareTo(mybalance) == 1){
                            tv_Balance_deduction.setText("余额已抵扣 ¥"+mybalance.setScale(2, BigDecimal.ROUND_HALF_UP));
                            pay_totalmoney.setText("¥"+totalprice.subtract(mybalance).setScale(2, BigDecimal.ROUND_HALF_UP));
                        }else {
                            tv_Balance_deduction.setText("余额已抵扣 ¥"+totalprice.setScale(2, BigDecimal.ROUND_HALF_UP));
                            pay_totalmoney.setText("¥"+totalprice.subtract(totalprice).setScale(2, BigDecimal.ROUND_HALF_UP));
                        }
                    }else {
                        tv_Balance_deduction.setText("");
                        pay_totalmoney.setText("¥"+totalprice.setScale(2, BigDecimal.ROUND_HALF_UP));
                    }

                    break;
                case 4:


                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {//判断其他Activity启动本Activity时传递来的intent是否为空
            //获取intent中对应Tag的布尔值
            boolean isFinish = intent.getBooleanExtra("finish", false);
            //如果为真则退出本Activity
            if (isFinish) {
                this.finish();

                Intent intent1=new Intent(ConfirmOrderActivity.this,BuyAfterActivity.class);
                startActivity(intent1);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_confirm_order);
        Intent intent=this.getIntent();
        cName=intent.getStringExtra("cName");
        attributeId=intent.getStringExtra("attributeId");
        attribute=intent.getStringExtra("attribute");
        specification=intent.getStringExtra("specification");
        attributeImg=intent.getStringExtra("attributeImg");
        totalprice=new BigDecimal(intent.getDoubleExtra("totalprice",0.00));
        attributePrice=intent.getDoubleExtra("attributePrice",0.00);
        goodNumber=intent.getIntExtra("goodNumber",1);
        totalNumber=intent.getIntExtra("totalNumber",1);
        initView();
        init();


    }

    private void initView() {
        iv_back = (RelativeLayout) findViewById(R.id.iv_back);
        pay_totalmoney = (TextView) findViewById(R.id.pay_totalmoney);
        tv_payOrder = (TextView) findViewById(R.id.tv_payOrder);
        amount_view = (AmountView) findViewById(R.id.amount_view);
        tv_Balance_deduction = (TextView) findViewById(R.id.tv_Balance_deduction);
        iv1 = (ImageView) findViewById(R.id.iv1);
        location = (ImageView) findViewById(R.id.location);
        turn_right = (ImageView) findViewById(R.id.turn_right);
        tv_address = (TextView) findViewById(R.id.tv_address);
        user_name = (TextView) findViewById(R.id.user_name);
        rl_change_address = (RelativeLayout) findViewById(R.id.rl_change_address);
        radiusImageView = (QMUIRadiusImageView) findViewById(R.id.radiusImageView);
        tv_name = (TextView) findViewById(R.id.tv_name);

        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_price = (TextView) findViewById(R.id.tv_price);
        totalmoney = (TextView) findViewById(R.id.totalmoney);
        problem = (EditText) findViewById(R.id.problem);
        mBalanceTogBtn = (ToggleButton) findViewById(R.id.mBalanceTogBtn);
        useraction_ = (TextView) findViewById(R.id.useraction_);

        iv_back.setOnClickListener(this);
        rl_change_address.setOnClickListener(this);
        tv_payOrder.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                    finish();
                break;
            case R.id.rl_change_address:
                //添加地址
                Intent intent=new Intent(ConfirmOrderActivity.this,ChooseAddressActivity.class);
                intent.putExtra("addressId",addressId);
                startActivityForResult(intent,1);
                break;
            case R.id.tv_payOrder:
                if(Common.isNOT_Null(addressId)){
                    if(isuse_balance){
                        if(totalprice.compareTo(mybalance) == 1){
                            LondingDialog2= new QMUITipDialog.Builder(this)
                                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                                    .setTipWord("提交中···")
                                    .create();

                            LondingDialog2.show();
                            checkUserPayWord(false);

                        }else {
                            LondingDialog2= new QMUITipDialog.Builder(this)
                                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                                    .setTipWord("提交中···")
                                    .create();

                            LondingDialog2.show();
                            checkUserPayWord(true);

                        }
                    }else {
                        //三方支付
                        Intent intent1=new Intent(ConfirmOrderActivity.this,ThirdPayActivity.class);
                        intent1.putExtra("cName",cName);
                        intent1.putExtra("specification",specification);
                        intent1.putExtra("attributeId",attributeId);
                        intent1.putExtra("addressId",addressId);
                        intent1.putExtra("buyCount",amount_view.getAmount());
                        intent1.putExtra("leaveWord",problem.getText().toString().trim());
                        intent1.putExtra("totalprice",totalprice.setScale(2, BigDecimal.ROUND_HALF_UP)+"");
                        intent1.putExtra("mybalance","0");//未用余额抵扣
                        intent1.putExtra("isUseBalance",false);
                        startActivity(intent1);


                    }
                }else {
                    ToastUtils.showShort(this,"请选择收货地址");
                }

                break;
        }
    }

    private void submit() {
        // validate
        String problemString = problem.getText().toString().trim();
        if (TextUtils.isEmpty(problemString)) {
            Toast.makeText(this, "有什么想对商家说的可以写在这里呦~", Toast.LENGTH_SHORT).show();
            return;
        }




    }

    private void init(){
        LondingDialog = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("加载中···")
                .create();

        LondingDialog.show();
        LondingDialog1 = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("加载中···")
                .create();

        LondingDialog1.show();
        useraction_balance();
        getDefaultAddress();
        amount_view.setGoods_Number(goodNumber);
        amount_view.setGoods_storage(totalNumber);
        amount_view.setCurrentNumber();
        //数量变动
        amount_view.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                totalprice= BigDecimal.valueOf(amount_view.getAmount()).multiply(new BigDecimal(attributePrice));
                totalmoney.setText("¥"+totalprice.setScale(2, BigDecimal.ROUND_HALF_UP));
//                pay_totalmoney.setText("¥"+totalprice.setScale(2, BigDecimal.ROUND_HALF_UP));
                Message message=new Message();
                message.what=3;
                handler.sendMessage(message);
            }
        });
        mBalanceTogBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    //选中
                    isuse_balance=false;
                    tv_Balance_deduction.setText("");
                    pay_totalmoney.setText("¥"+totalprice.setScale(2, BigDecimal.ROUND_HALF_UP));

                }else{
                    //初始状态
                    isuse_balance=true;
                    //判断余额是否充足
                    if(totalprice.compareTo(mybalance) == 1){
                        tv_Balance_deduction.setText("余额已抵扣 ¥"+mybalance.setScale(2, BigDecimal.ROUND_HALF_UP));
                        pay_totalmoney.setText("¥"+totalprice.subtract(mybalance).setScale(2, BigDecimal.ROUND_HALF_UP));
                    }else {
                        tv_Balance_deduction.setText("余额已抵扣 ¥"+totalprice.setScale(2, BigDecimal.ROUND_HALF_UP));
                        pay_totalmoney.setText("¥"+totalprice.subtract(totalprice).setScale(2, BigDecimal.ROUND_HALF_UP));
                    }


                }
            }
        });
        radiusImageView.setCornerRadius(QMUIDisplayHelper.dp2px(this, 10));
        radiusImageView.setCircle(false);
        radiusImageView.setOval(false);
        radiusImageView.setTouchSelectModeEnabled(false);
        Glide.with(this)
                .load(attributeImg)
                .into(radiusImageView);
        tv_name.setText(cName);
        tv_type.setText(attribute+":"+specification+";");
        tv_price.setText("¥"+attributePrice);
        totalmoney.setText("¥"+totalprice.setScale(2, BigDecimal.ROUND_HALF_UP));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    Bundle bundle=data.getExtras();

                    addressId=bundle.getString("addressId");
                    String consigneeName=bundle.getString("consigneeName");
                    String consigneeNum=bundle.getString("consigneeNum");
                    String consigneeAddress=bundle.getString("consigneeAddress");

                    tv_address.setText(consigneeAddress);
                    user_name.setText(consigneeName+"  "+consigneeNum);
                }


                break;

            default:
                break;
        }

    }




    private void useraction_balance(){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                useraction_balance+"?uNum=" + Usertel,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        LondingDialog.dismiss();
                        try {
                            Message message = new Message();
                            message.what = 1;
                            message.obj=response;
                            handler.sendMessage(message);
                        } catch (Exception e) {
                            LondingDialog.dismiss();
                            Toast.makeText(ConfirmOrderActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LondingDialog.dismiss();
                Toast.makeText(ConfirmOrderActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeOutMs,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
    private void getDefaultAddress(){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                getDefaultAddress+"?addressNum=" + Usertel,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        LondingDialog1.dismiss();
                        try {

                            if(Common.isNOT_Null(response)){
                                JSONObject jsonObject =new JSONObject(response);
                                addressId=jsonObject.getString("addressId");
                                String consigneeName=jsonObject.getString("consigneeName");
                                String consigneeNum=jsonObject.getString("consigneeNum");
                                String consigneeAddress=jsonObject.getString("addressSre");

                                address.setConsigneeAddress(addressId);
                                address.setConsigneeAddress(consigneeAddress);
                                address.setConsigneeName(consigneeName);
                                address.setConsigneeNum(consigneeNum);
                                Message message = new Message();
                                message.what = 2;
                                handler.sendMessage(message);
                            }

                        } catch (Exception e) {
                            LondingDialog1.dismiss();
                            Toast.makeText(ConfirmOrderActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LondingDialog1.dismiss();
                Toast.makeText(ConfirmOrderActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

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

                        PopEnterPassword popEnterPassword = new PopEnterPassword(ConfirmOrderActivity.this,ConfirmOrderActivity.this,response,flag);
//                        PopEnterPassword popEnterPassword = new PopEnterPassword(ConfirmOrderActivity.this);
                        // 显示窗口
                        popEnterPassword.showAtLocation(ConfirmOrderActivity.this.findViewById(R.id.layoutContent),
                                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LondingDialog2.dismiss();
                Toast.makeText(ConfirmOrderActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeOutMs,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
    private void buyCoinGoods(){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                buyCoinGoods+"?uNum=" + Usertel+"&attributeId="+attributeId+"&buyAddress="+
                        addressId+"&buyCount="+amount_view.getAmount()+"&leaveWord="+problem.getText().toString().trim(),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            if("true".equals(response)){
                                finish();
                                ToastUtils.showShort(ConfirmOrderActivity.this,"支付成功");
                                Intent intent=new Intent(ConfirmOrderActivity.this,BuyAfterActivity.class);
                                startActivity(intent);
                            }else {
                                ToastUtils.showShort(ConfirmOrderActivity.this,"支付失败");
                            }


                        } catch (Exception e) {

                            Toast.makeText(ConfirmOrderActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ConfirmOrderActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

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
            buyCoinGoods();
        }else {
            //余额和三方支付
            Intent intent1=new Intent(ConfirmOrderActivity.this,ThirdPayActivity.class);
            intent1.putExtra("cName",cName);
            intent1.putExtra("specification",specification);
            intent1.putExtra("attributeId",attributeId);
            intent1.putExtra("addressId",addressId);
            intent1.putExtra("buyCount",amount_view.getAmount());
            intent1.putExtra("leaveWord",problem.getText().toString().trim());
            intent1.putExtra("totalprice",totalprice.subtract(mybalance).setScale(2, BigDecimal.ROUND_HALF_UP)+"");
            intent1.putExtra("mybalance",mybalance.setScale(2, BigDecimal.ROUND_HALF_UP)+"");
            intent1.putExtra("isUseBalance",true);
            startActivity(intent1);
        }
    }
}
