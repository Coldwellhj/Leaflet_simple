package leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mine;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
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

import java.io.UnsupportedEncodingException;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.base.BaseActivity;
import leaflet.miaoa.qmxh.leaflet_simple.utils.AesCBC;
import leaflet.miaoa.qmxh.leaflet_simple.utils.Base64;
import leaflet.miaoa.qmxh.leaflet_simple.utils.ToastUtils;

import static leaflet.miaoa.qmxh.leaflet_simple.Login.WelcomeActivity.Usertel;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.SMS_130928189;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.getCode;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.modifyUserPayword;


/**
 * 修改支付密码页面
 */
public class ModifyPayPsdActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout iBack;
    private EditText et_psd;
    private EditText newPhoneNum;
    private EditText et_Code;
    private TextView confirm,tv_modify_phonenumber_getnum;
    private String et_psdStr,newPhoneNumStr,et_CodeStr;
    ProgressDialog progressDialog;
    private CountDownTimer countDownTimer;
    private String strCode;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String psd_jiami;
    @Override
    protected void setContentView() {

        setContentView(R.layout.activity_modify_paypsd);
        sharedPreferences = this.getSharedPreferences("login", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();//获取编辑器
    }

    @Override
    protected void setFindViewById() {
        iBack = (RelativeLayout) findViewById(R.id.iv_modify_phonenumber_back);
        confirm = (TextView) findViewById(R.id.confirm);
        tv_modify_phonenumber_getnum = (TextView) findViewById(R.id.tv_modify_phonenumber_getnum);
        et_psd = (EditText) findViewById(R.id.et_psd);
        newPhoneNum = (EditText) findViewById(R.id.newPhoneNum);
        et_Code = (EditText) findViewById(R.id.et_Code);
    }

    @Override
    protected void setListener() {
        iBack.setOnClickListener(this);
        confirm.setOnClickListener(this);
        tv_modify_phonenumber_getnum.setOnClickListener(this);
    }

    @Override
    protected void setControl() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_modify_phonenumber_back:
                finish();
                break;
            case R.id.tv_modify_phonenumber_getnum:
                final String telString = newPhoneNum.getText().toString().trim();
                if (!telString.equals(Usertel)) {
                    Toast.makeText(ModifyPayPsdActivity.this, "请输入当前的手机号", Toast.LENGTH_SHORT).show();

                }else {

                    getCode_android(telString);
                    if (countDownTimer == null){
                        countDownTimer = new CountDownTimer(60000,1000) {
                            @Override
                            public void onTick(long l) {
                                String time = l/1000+"s";
                                tv_modify_phonenumber_getnum.setText(time);
                                tv_modify_phonenumber_getnum.setTextColor(Color.parseColor("#999999"));


                            }

                            @Override
                            public void onFinish() {
                                tv_modify_phonenumber_getnum.setText("获取验证码");
                                tv_modify_phonenumber_getnum.setTextColor(Color.parseColor("#333333"));
                                countDownTimer = null;
                            }
                        }.start();
                    }

                }
                break;
            case R.id.confirm:
                if(submit()){
                    if (!et_Code.getText().toString().trim().equals(strCode)){
                        Toast.makeText(ModifyPayPsdActivity.this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();

                    }else {
                        progressDialog = new ProgressDialog(ModifyPayPsdActivity.this,
                                R.style.AppTheme_Dark_Dialog);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("正在修改中...");
                        progressDialog.show();

                        modifyUserPayword(et_psdStr);
                    }

                }

                break;
        }
    }
    private boolean submit() {
        // validate
        et_psdStr = et_psd.getText().toString().trim();
        newPhoneNumStr = newPhoneNum.getText().toString().trim();
        et_CodeStr = et_Code.getText().toString().trim();
        if (TextUtils.isEmpty(et_psdStr)||TextUtils.isEmpty(newPhoneNumStr)||TextUtils.isEmpty(et_CodeStr)) {
            Toast.makeText(this, "请输入完整的信息", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void modifyUserPayword (final String newPsd){
        try {
            byte[ ] encrypted = AesCBC.AES_CBC_Encrypt(newPsd.getBytes("UTF-8"), AesCBC.key.getBytes("UTF-8"), AesCBC.iv.getBytes("UTF-8"));
            psd_jiami = Base64.encode(encrypted);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                modifyUserPayword + "?uNum=" + Usertel+"&payword="+psd_jiami  ,
//
                new Response.Listener<String>() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onResponse(String response) {

                        try {

                            if("true".equals(response)){
                                progressDialog.dismiss();

                                ToastUtils.showShort(ModifyPayPsdActivity.this,"修改成功");

                                ModifyPayPsdActivity.this.finish();
                            }else {
                                progressDialog.dismiss();
                                ToastUtils.showShort(ModifyPayPsdActivity.this,"修改失败");
                            }

                        } catch (Exception e) {
                            progressDialog.dismiss();
                            ToastUtils.showShort(ModifyPayPsdActivity.this,"数据异常");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ModifyPayPsdActivity.this, "修改失败，请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
    private void getCode_android(String telString){
        RequestQueue mQueue = Volley.newRequestQueue(ModifyPayPsdActivity.this);
        StringRequest stringRequest = new StringRequest(
                getCode + "?phoneNumber=" + telString + "&templateParam="+SMS_130928189,
//
                new Response.Listener<String>() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onResponse(String response) {
                        try {

                                String str[]=response.split("<-->");

                                String isSuccess=str[0];

                                if("false".equals(isSuccess)){
                                    Toast.makeText(ModifyPayPsdActivity.this, "一小时最多5条，一天最多10条", Toast.LENGTH_LONG).show();
                                }else if("ok".equalsIgnoreCase(isSuccess)){
                                    strCode=str[1];
                                }
                        } catch (Exception e) {
                            Toast.makeText(ModifyPayPsdActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ModifyPayPsdActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
}
