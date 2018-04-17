package leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mall.payfor;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.base.BaseOtherActivity;
import leaflet.miaoa.qmxh.leaflet_simple.ui.widget.MyPopupWindow;

import static leaflet.miaoa.qmxh.leaflet_simple.Login.WelcomeActivity.Usertel;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.insertConsigneeAddress;

public class NewAddressActivity extends BaseOtherActivity implements View.OnClickListener {

    private RelativeLayout iv_back;
    private RelativeLayout rl_save_Address;
    private EditText et_address_name;
    private EditText et_address_Number;
    private TextView tv_address_area;
    private RelativeLayout rl_address_area;
    private EditText et_address_detail;
    private ToggleButton mBalanceTogBtn;
    private boolean isdefaultaddress=false;
    private MyPopupWindow myPopupWindow;
    private QMUITipDialog LoadingDialog;
    private QMUITipDialog successDialog;
    private QMUITipDialog failedDialog;
    private String address_detail;
    private String address_name;
    private String address_Number;
    private String address_area;
    private Handler handler=new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:



                    setResult(RESULT_OK);
                    finish();

                    break;


            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);
        initView();
        init();
    }

    private void initView() {
        iv_back = (RelativeLayout) findViewById(R.id.iv_back);
        rl_save_Address = (RelativeLayout) findViewById(R.id.rl_save_Address);
        et_address_name = (EditText) findViewById(R.id.et_address_name);
        et_address_Number = (EditText) findViewById(R.id.et_address_Number);
        tv_address_area = (TextView) findViewById(R.id.tv_address_area);
        rl_address_area = (RelativeLayout) findViewById(R.id.rl_address_area);
        et_address_detail = (EditText) findViewById(R.id.et_address_detail);
        mBalanceTogBtn = (ToggleButton) findViewById(R.id.mBalanceTogBtn);

        rl_address_area.setOnClickListener(this);
        rl_save_Address.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }
    private void init(){
        mBalanceTogBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    //选中
                    isdefaultaddress=true;
                }else{
                    //初始状态
                   isdefaultaddress=false;

                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_address_area:
                myPopupWindow = new MyPopupWindow(NewAddressActivity.this, itemsOnClick);
                myPopupWindow.showAtLocation(this.findViewById(R.id.rl_address_area),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.rl_save_Address:
                if(submit()){
                    LoadingDialog = new QMUITipDialog.Builder(this)
                            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                            .setTipWord("正在保存")
                            .create();
                    LoadingDialog.show();
                    insertConsigneeAddress();
                }

                break;
            case R.id.iv_back:
               finish();

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
                    tv_address_area.setText(myPopupWindow.getSelectedResult());
                    break;

            }

        }

    };
    private boolean submit() {
        // validate
        address_detail = et_address_detail.getText().toString().trim();
        address_name = et_address_name.getText().toString().trim();
        address_Number = et_address_Number.getText().toString().trim();
        address_area = tv_address_area.getText().toString().trim();
        if (TextUtils.isEmpty(address_name)) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }else


        if (TextUtils.isEmpty(address_Number)||address_Number.length()!=11) {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return false;
        }else


        if (TextUtils.isEmpty(address_detail)||address_detail.length()<5) {
            Toast.makeText(this, "街道、楼盘等,不少于5个字", Toast.LENGTH_SHORT).show();
            return false;
        }else if(TextUtils.isEmpty(address_area)){
            Toast.makeText(this, "请选择地址", Toast.LENGTH_SHORT).show();
        }
        return true;

    }
    private void insertConsigneeAddress(){
        String s=address_area+" "+address_detail;
        String addressSre="";
        String address_name_Encoder="";
        try {
            addressSre= URLEncoder.encode(s,"UTF-8");
            address_name_Encoder= URLEncoder.encode(address_name,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(

                insertConsigneeAddress+"?addressNum=" + Usertel+"&consigneeName="+address_name_Encoder+"&consigneeNum="+address_Number +"&addressSre="+addressSre+"&defaultIf="+isdefaultaddress,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                            if("true".equals(response)){
                                LoadingDialog.dismiss();
                                successDialog = new QMUITipDialog.Builder(NewAddressActivity.this)
                                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                                        .setTipWord("保存成功")
                                        .create();
                                successDialog.show();
                                rl_save_Address.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        successDialog.dismiss();
                                        Message message =new Message();
                                        message.what=1;
                                        handler.sendMessage(message);

                                    }
                                }, 1500);
                            }else {
                                LoadingDialog.dismiss();
                                failedDialog = new QMUITipDialog.Builder(NewAddressActivity.this)
                                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                                        .setTipWord("保存失败")
                                        .create();
                                failedDialog.show();
                                rl_save_Address.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        failedDialog.dismiss();

                                    }
                                }, 1500);
                            }



                        } catch (Exception e) {
                            LoadingDialog.dismiss();
                            Toast.makeText(NewAddressActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                LoadingDialog.dismiss();
                Toast.makeText(NewAddressActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }

        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
}
