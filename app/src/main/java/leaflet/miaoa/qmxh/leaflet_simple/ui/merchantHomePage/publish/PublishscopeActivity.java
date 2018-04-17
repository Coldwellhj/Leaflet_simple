package leaflet.miaoa.qmxh.leaflet_simple.ui.merchantHomePage.publish;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dueeeke.videoplayer.controller.StandardVideoController;
import com.qmuiteam.qmui.widget.QMUIProgressBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.base.BaseOtherActivity;
import leaflet.miaoa.qmxh.leaflet_simple.receiver.NetWorkStatusReceiver;
import leaflet.miaoa.qmxh.leaflet_simple.ui.Interface.UploadCallBack;
import leaflet.miaoa.qmxh.leaflet_simple.ui.widget.GlideImageLoader;
import leaflet.miaoa.qmxh.leaflet_simple.ui.widget.MyPopupWindow;
import leaflet.miaoa.qmxh.leaflet_simple.ui.widget.UploadQNY;
import leaflet.miaoa.qmxh.leaflet_simple.utils.Common;
import leaflet.miaoa.qmxh.leaflet_simple.utils.DateUtils;
import leaflet.miaoa.qmxh.leaflet_simple.utils.ToastUtils;

import static leaflet.miaoa.qmxh.leaflet_simple.Login.WelcomeActivity.Usertel;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.adverAdd;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.getToken;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.initialTimeOutMs;


public class PublishscopeActivity extends BaseOtherActivity implements View.OnClickListener,UploadCallBack {

    private RelativeLayout iv_back;
    private Banner iv_cover;
    private StandardVideoController controller;
    private JCVideoPlayer jcVideoPlayer;
    private TextView tv_content;
    private TextView tv_center_area;
    private EditText et_center_address;
    private RelativeLayout rl_center_area;
    private TextView tv_cover_area;
    private EditText et_adv_totalNum;
    private EditText et_adv_price;
    private TextView tv_time_start;
    private TextView payMoney;
    private TextView tv_time_finish;
    private RelativeLayout rl_pay_publish;
    private String content="";
    private String currentTime="";
    private String aType="";
//    private String aMatter="";//所有图片名称
    private List<String> imagePathList=new ArrayList<String>();
    private MyPopupWindow myPopupWindow;
    private BigDecimal payPrice ;//需要支付的钱
    private BigDecimal cover_charge ;//服务费
    private String starttime ;
    private String finishtime ;
    private String address  ;
    private String center_area  ;
    private String cover_area  ;
    private Double cover_area_num  ;
    private String time_start  ;
    private String time_finish  ;
    private String totalNum  ;
    private String price  ;
    private String videoTime=""  ;
    private String token=""  ;//七牛云token

    private QMUITipDialog uploadDialog;
    private QMUITipDialog payDialog;
    private QMUITipDialog payfailedDialog;


    protected static final int STOP = 0x10000;
    protected static final int NEXT = 0x10001;

//    private ProgressHandler myHandler = new ProgressHandler();
    private BigDecimal a = new BigDecimal("1.02");
    private BigDecimal b = new BigDecimal("0.02");
    private BigDecimal c = new BigDecimal("1");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//防止键盘出来，底部控件上移
        setContentView(R.layout.activity_publishscope);
        Intent intent=getIntent();
        content=intent.getStringExtra("content");
        aType=intent.getStringExtra("aType");
        videoTime=intent.getStringExtra("videoTime");
        imagePathList =intent.getStringArrayListExtra("imagePathList");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new NetWorkStatusReceiver(), intentFilter);
        getToken();
        initView();
        init();
    }

    private void initView() {
        iv_back = (RelativeLayout) findViewById(R.id.iv_back);
        iv_cover = (Banner) findViewById(R.id.iv_cover);
        jcVideoPlayer = (JCVideoPlayer) findViewById(R.id.videocontroller1);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_center_area = (TextView) findViewById(R.id.tv_center_area);
        et_center_address = (EditText) findViewById(R.id.et_center_address);
        rl_center_area = (RelativeLayout) findViewById(R.id.rl_center_area);
        tv_cover_area = (TextView) findViewById(R.id.tv_cover_area);
        et_adv_totalNum = (EditText) findViewById(R.id.et_adv_totalNum);
        et_adv_price = (EditText) findViewById(R.id.et_adv_price);
        tv_time_start = (TextView) findViewById(R.id.tv_time_start);
        tv_time_finish = (TextView) findViewById(R.id.tv_time_finish);
        rl_pay_publish = (RelativeLayout) findViewById(R.id.rl_pay_publish);
        payMoney = (TextView) findViewById(R.id.payMoney);
        tv_center_area.setOnClickListener(this);
        tv_cover_area.setOnClickListener(this);
        tv_time_start.setOnClickListener(this);
        tv_time_finish.setOnClickListener(this);
        rl_pay_publish.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }
    private void init(){


     tv_content.setText(content);
        if(aType.equals("false")){
            jcVideoPlayer.setVisibility(View.GONE);
            iv_cover.setVisibility(View.VISIBLE);

            iv_cover.setImages(imagePathList)
                    .setImageLoader(new GlideImageLoader())
                    .start();
            iv_cover.updateBannerStyle(BannerConfig.NUM_INDICATOR);
        }else if(aType.equals("true")) {
            jcVideoPlayer.setVisibility(View.VISIBLE);
            iv_cover.setVisibility(View.GONE);

            jcVideoPlayer.setUp(imagePathList.get(1),
                    imagePathList.get(0),
                    "");


        }

        et_adv_totalNum.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

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

                if(Common.isNOT_Null(et_adv_price.getText().toString().trim())&&Common.isNOT_Null(et_adv_totalNum.getText().toString())){
                    BigDecimal totalprice= new BigDecimal(et_adv_price.getText().toString().trim())
                            .multiply(new BigDecimal(et_adv_totalNum.getText().toString().trim()));
                    if(totalprice .compareTo(c)==-1){
                        payPrice=totalprice.add(b).setScale(2, BigDecimal.ROUND_UP);
                        cover_charge=b.setScale(2, BigDecimal.ROUND_UP);
                    }else {
                        payPrice= (new BigDecimal(et_adv_price.getText().toString().trim())
                                .multiply(new BigDecimal(et_adv_totalNum.getText().toString().trim())).multiply(a)).setScale(2, BigDecimal.ROUND_UP);

                        cover_charge= (new BigDecimal(et_adv_price.getText().toString().trim())
                                .multiply(new BigDecimal(et_adv_totalNum.getText().toString().trim())).multiply(b)).setScale(2, BigDecimal.ROUND_UP);
                    }


                    payMoney.setText("需支付"+payPrice+"元");
                }


            }
        });
        et_adv_price.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
//                Log.e("输入过程中执行该方法", "文字变化");
                //只能输入两位小数
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        et_adv_price.setText(s);
                        et_adv_price.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    et_adv_price.setText(s);
                    et_adv_price.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        et_adv_price.setText(s.subSequence(0, 1));
                        et_adv_price.setSelection(1);
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

                if(Common.isNOT_Null(et_adv_totalNum.getText().toString())&&Common.isNOT_Null(et_adv_price.getText().toString().trim())){
                    BigDecimal totalprice= new BigDecimal(et_adv_price.getText().toString().trim())
                            .multiply(new BigDecimal(et_adv_totalNum.getText().toString().trim()));

                    if(totalprice .compareTo(c)==-1){
                        payPrice=totalprice.add(b).setScale(2, BigDecimal.ROUND_UP);
                        cover_charge=b.setScale(2, BigDecimal.ROUND_UP);
                    }else {
                        payPrice= (new BigDecimal(et_adv_price.getText().toString().trim())
                                .multiply(new BigDecimal(et_adv_totalNum.getText().toString().trim())).multiply(a)).setScale(2, BigDecimal.ROUND_UP);

                        cover_charge= (new BigDecimal(et_adv_price.getText().toString().trim())
                                .multiply(new BigDecimal(et_adv_totalNum.getText().toString().trim())).multiply(b)).setScale(2, BigDecimal.ROUND_UP);
                    }

                    payMoney.setText("需支付"+(payPrice.subtract(cover_charge)).setScale(2, BigDecimal.ROUND_UP)+"元");
                }


            }
        });
    }
    private boolean submit() {
        // validate
        address = et_center_address.getText().toString().trim();
        center_area = tv_center_area.getText().toString().trim();
        cover_area = tv_cover_area.getText().toString().trim();
        time_start = tv_time_start.getText().toString().trim();
        time_finish = tv_time_finish.getText().toString().trim();
        totalNum = et_adv_totalNum.getText().toString().trim();
        price = et_adv_price.getText().toString().trim();
        if (TextUtils.isEmpty(address)||TextUtils.isEmpty(totalNum)||TextUtils.isEmpty(price)||"请选择".equals(center_area)||"地址选择".equals(cover_area)||"开始日期".equals(time_start)||"结束日期".equals(finishtime)) {
            Toast.makeText(this, "请填写完整的信息", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;






    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_center_area:
                myPopupWindow = new MyPopupWindow(PublishscopeActivity.this, itemsOnClick);
                myPopupWindow.showAtLocation(this.findViewById(R.id.tv_center_area),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.tv_cover_area:
//                myPopupWindow = new MyPopupWindow(PublishscopeActivity.this, itemsOnClick1);
//                myPopupWindow.showAtLocation(this.findViewById(R.id.tv_cover_area),
//                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                final Double []coverAddress={10.00,50.00,100.00,500.00,99999.00};
                new QMUIBottomSheet.BottomListSheetBuilder(this)
                        .addItem("10km")
                        .addItem("50km")
                        .addItem("100km")
                        .addItem("500km")
                        .addItem("不限")
                        .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                            @Override
                            public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                dialog.dismiss();
                                if(coverAddress[position]==99999.00){
                                    tv_cover_area.setText("不限");
                                }else {
                                    tv_cover_area.setText(coverAddress[position]+"km");
                                }

                                cover_area_num=coverAddress[position];
                            }
                        })
                        .build()
                        .show();
                break;

            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_pay_publish:
                if(submit()){
                    if(Double.parseDouble(price)<0.1){
                        new QMUIDialog.MessageDialogBuilder(this)
                                .setTitle("提示")
                                .setMessage("不得低于0.1元/条")
                                .addAction("确定", new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }else {

                        if(aType.equals("false")){
                            if(Common.isNOT_Null(token)){
                                UploadQNY uploadQNY=new UploadQNY(this,token,imagePathList);
                                uploadQNY.uploadMore();
                                uploadDialog = new QMUITipDialog.Builder(this)
                                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                                        .setTipWord("正在上传")
                                        .create();
                                uploadDialog.show();
                            }else {
                                getToken();
                                ToastUtils.showShort(this,"token获取失败，请重新上传");
                            }


                        }else if(aType.equals("true")) {
                            if(Common.isNOT_Null(token)){
                                UploadQNY uploadQNY=new UploadQNY(this,token,imagePathList.get(1));
                                uploadQNY.uploadVideo();
                                uploadDialog = new QMUITipDialog.Builder(this)
                                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                                        .setTipWord("正在上传")
                                        .create();
                                uploadDialog.show();
                            }else {
                                getToken();
                                ToastUtils.showShort(this,"token获取失败，请重新上传");
                            }



                        }
                    }
                }
                break;
            case R.id.tv_time_start:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View view = View.inflate(this, R.layout.date_time_dialog, null);
                final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);

                builder.setView(view);

                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());
                datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);


                datePicker.setMinDate(cal.getTimeInMillis()-1000);
//                cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 15);
//                datePicker.setMaxDate(cal.getTimeInMillis());
                if(Common.isNOT_Null(finishtime)){
                    datePicker.setMaxDate(DateUtils.stringToDate1(finishtime).getTime());
                }else {

                }
                final int inType = tv_time_start.getInputType();
                tv_time_start.setInputType(InputType.TYPE_NULL);
//                tv_personal_information_birthday.onTouchEvent(event);
                tv_time_start.setInputType(inType);
                //etStartTime.setSelection(etStartTime.getText().length());

                builder.setTitle("开始时间");
                builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuffer sb = new StringBuffer();
                        sb.append(String.format("%d-%02d-%02d",
                                datePicker.getYear(),
                                datePicker.getMonth() + 1,
                                datePicker.getDayOfMonth()));
                        starttime=sb.toString();
                        tv_time_start.setText(starttime);
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                Dialog dialog_start = builder.create();
                dialog_start.show();
                break;
            case R.id.tv_time_finish:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                View view1 = View.inflate(this, R.layout.date_time_dialog, null);
                final DatePicker datePicker1 = (DatePicker) view1.findViewById(R.id.date_picker);

                builder1.setView(view1);

                Calendar cal1 = Calendar.getInstance();
                cal1.setTimeInMillis(System.currentTimeMillis());
                datePicker1.init(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH), cal1.get(Calendar.DAY_OF_MONTH), null);

                if(Common.isNOT_Null(starttime)){
                    datePicker1.setMinDate(DateUtils.stringToDate1(starttime).getTime());
                }else {
                    datePicker1.setMinDate(cal1.getTimeInMillis()-1000);
                }

//                cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 15);
//                datePicker.setMaxDate(cal.getTimeInMillis());

                final int inType1 = tv_time_start.getInputType();
                tv_time_start.setInputType(InputType.TYPE_NULL);
//                tv_personal_information_birthday.onTouchEvent(event);
                tv_time_start.setInputType(inType1);
                //etStartTime.setSelection(etStartTime.getText().length());

                builder1.setTitle("结束时间");
                builder1.setPositiveButton("确  定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuffer sb = new StringBuffer();
                        sb.append(String.format("%d-%02d-%02d",
                                datePicker1.getYear(),
                                datePicker1.getMonth() + 1,
                                datePicker1.getDayOfMonth()));
                        finishtime=sb.toString();
                        tv_time_finish.setText(finishtime);
                        dialog.cancel();
                    }
                });
                builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                Dialog dialog_finish = builder1.create();
                dialog_finish.show();
                break;
        }
    }
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            myPopupWindow.closePopupWindow();
            switch (v.getId()) {
                case R.id.cancle:
//                    Log.i(TAG, "保存线路");

                    break;
                case R.id.confirm:
                    tv_center_area.setText(myPopupWindow.getSelectedResult());
                    break;

            }

        }

    };
//    private View.OnClickListener itemsOnClick1 = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            myPopupWindow.closePopupWindow();
//            switch (v.getId()) {
//                case R.id.cancle:
////                    Log.i(TAG, "保存线路");
//
//                    break;
//                case R.id.confirm:
//                    tv_cover_area.setText(myPopupWindow.getSelectedResult());
//                    break;
//
//            }
//
//        }
//
//    };



    private void adverAdd(String cover,String aMatter,int timecount){
        currentTime=System.currentTimeMillis()+"";

        String adress=tv_center_area.getText().toString().trim()+","+et_center_address.getText().toString().trim();

        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                adverAdd+"?aNum=" + Usertel+"&aContent="+content+"&aCover="+
                        cover+"&aMatter="+aMatter+"&aType="+aType+"&aPrice="+price+"&aSum=" +totalNum
                        +"&aAdress="+adress
                        +"&coveRage="+cover_area_num+"&uploadBegin="+starttime+"&uploadEnd="+finishtime
                        +"&timeCount="+timecount +"&timeStamp="+currentTime,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            String s[]=response.split("<-->");
                            if("true".equals(s[0])){

                                uploadDialog.dismiss();

                                //余额和三方支付
                                Intent intent=new Intent(PublishscopeActivity.this,ThirdPaySellerActivity.class);

                                intent.putExtra("payMoney",payPrice+"");
                                intent.putExtra("currentTime",currentTime);
                                intent.putExtra("out_trade_no",s[1]);
                                intent.putExtra("cover_charge",cover_charge+"");
                                startActivity(intent);

                            }else {
                                uploadDialog.dismiss();

                            }


                        } catch (Exception e) {
                            uploadDialog.dismiss();
                            Toast.makeText(PublishscopeActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                uploadDialog.dismiss();
                Toast.makeText(PublishscopeActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeOutMs,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }


    private void getToken(){




        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                getToken,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                        token=response;

                        } catch (Exception e) {

                            Toast.makeText(PublishscopeActivity.this, "获取token异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(PublishscopeActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeOutMs,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
    @Override
    public void UploadImageStr(String imageName) {
        if("".equals(imageName)){
            uploadDialog.dismiss();
            ToastUtils.showShort(this,"上传失败，请保持网络通畅！");
        }else {

            adverAdd(imagePathList.get(0),imageName,Integer.parseInt(videoTime));


        }

    }

    @Override
    public void UploadMoreImageStr(String coverimg, String picture) {
        if("".equals(coverimg)){
            uploadDialog.dismiss();
            ToastUtils.showShort(this,"上传失败，请保持网络通畅！");
        }else {

            adverAdd(coverimg,picture,Integer.parseInt(videoTime));

        }


    }

    @Override
    public void sendUploadprogress(double percent) {
        uploadDialog.dismiss();
        ToastUtils.showShort(this,"网络已断开，请重新上传");
//        int progress = (int)(percent*100);
//        if(progress==100){
//            Message msg = new Message();
//            msg.what = STOP;
//            myHandler.sendMessage(msg);
//        }else {
//            Message msg = new Message();
//            msg.what = NEXT;
//            msg.arg1 = progress;
//            myHandler.sendMessage(msg);
//        }

    }

    private  class ProgressHandler extends Handler {

        private WeakReference<QMUIProgressBar> weakCircleProgressBar;

        void setProgressBar( QMUIProgressBar circleProgressBar) {

            weakCircleProgressBar = new WeakReference<>(circleProgressBar);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case STOP:
                    break;
                case NEXT:
                    if (!Thread.currentThread().isInterrupted()) {
                        if ( weakCircleProgressBar.get() != null) {

                            weakCircleProgressBar.get().setProgress(msg.arg1);
                        }
                    }
            }

        }
    }
}
