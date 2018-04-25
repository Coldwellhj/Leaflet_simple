package leaflet.miaoa.qmxh.leaflet_simple.Login;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.base.BaseActivity;
import leaflet.miaoa.qmxh.leaflet_simple.utils.AesCBC;
import leaflet.miaoa.qmxh.leaflet_simple.utils.Base64;

import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.SMS_127135070;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.getCode;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.isExist;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.resetPassword;


/**
 * 重置密码页面
 */
public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iBack;
    private EditText et_num;
    private EditText et_password;
    private EditText et_password_confirm;
    private EditText et_Code;
    private TextView tv_confirm;
    private TextView tv_resetpassword_getnum;
    private CountDownTimer countDownTimer;
    private String strCode;
    private String psd_jiami;
    private CheckBox cbDisplayPassword;
    private CheckBox cbDisplayPassword1;
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_reset_password);
    }

    @Override
    protected void setFindViewById() {
        iBack = (ImageView) findViewById(R.id.iv_reset_back);
        et_num = (EditText) findViewById(R.id.et_num);
        et_password = (EditText) findViewById(R.id.et_password);
        et_password_confirm = (EditText) findViewById(R.id.et_password_confirm);
        et_Code = (EditText) findViewById(R.id.et_Code);
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        tv_resetpassword_getnum = (TextView) findViewById(R.id.tv_resetpassword_getnum);
        tv_resetpassword_getnum.setOnClickListener(this);
        cbDisplayPassword = (CheckBox) findViewById(R.id.cbDisplayPassword);
        cbDisplayPassword1 = (CheckBox) findViewById(R.id.cbDisplayPassword1);
        cbDisplayPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Log.d(TAG, "onCheckedChanged: "+isChecked);
                if(isChecked){
//选择状态 显示明文--设置为可见的密码
                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else {
//默认状态显示密码--设置文本 要一起写才能起作用 InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        cbDisplayPassword1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Log.d(TAG, "onCheckedChanged: "+isChecked);
                if(isChecked){
//选择状态 显示明文--设置为可见的密码
                    et_password_confirm.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else {
//默认状态显示密码--设置文本 要一起写才能起作用 InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    et_password_confirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        et_num.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
                if(et_num.getText().toString().length()==11){
                    RequestQueue mQueue = Volley.newRequestQueue(ResetPasswordActivity.this);
                    StringRequest stringRequest = new StringRequest(
                            isExist + "?uNum=" + et_num.getText().toString().trim()  ,
//
                            new Response.Listener<String>() {
                                @SuppressLint("WrongConstant")
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        if("2".equals(response)){
//                                            Toast.makeText(ResetPasswordActivity.this, "账号已经存在", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        Toast.makeText(ResetPasswordActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ResetPasswordActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

                        }
                    });
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    mQueue.add(stringRequest);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // 输入前的监听

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听

            }
        });
    }


    @Override
    protected void setListener() {
        iBack.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
    }

    @Override
    protected void setControl() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_reset_back:
                finish();
                Intent intent = new Intent(ResetPasswordActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_resetpassword_getnum:
                final String telString = et_num.getText().toString().trim();
                if (TextUtils.isEmpty(telString) || telString.length() != 11) {
                    Toast.makeText(ResetPasswordActivity.this, "请输入有效的手机号", Toast.LENGTH_LONG).show();

                }else {
                    if (countDownTimer == null){
                        countDownTimer = new CountDownTimer(60000,1000) {
                            @Override
                            public void onTick(long l) {
                                String time = l/1000+"s";
                                tv_resetpassword_getnum.setText(time);
                                tv_resetpassword_getnum.setTextColor(Color.parseColor("#999999"));


                            }

                            @Override
                            public void onFinish() {
                                tv_resetpassword_getnum.setText("获取验证码");
                                tv_resetpassword_getnum.setTextColor(Color.parseColor("#333333"));
                                countDownTimer = null;
                            }
                        }.start();
                    }
                    RequestQueue mQueue = Volley.newRequestQueue(ResetPasswordActivity.this);
                    StringRequest stringRequest = new StringRequest(
                            getCode + "?phoneNumber=" + telString + "&templateParam="+SMS_127135070,
//
                            new Response.Listener<String>() {
                                @SuppressLint("WrongConstant")
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        String str[]=response.split("<-->");

                                        String isSuccess=str[0];

                                         if("false".equals(isSuccess)){
                                            Toast.makeText(ResetPasswordActivity.this, "一小时最多5条，一天最多10条", Toast.LENGTH_SHORT).show();
                                          }else if("ok".equalsIgnoreCase(isSuccess)){
                                             strCode=str[1];
                                         }
                                    } catch (Exception e) {
                                        Toast.makeText(ResetPasswordActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ResetPasswordActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

                        }
                    });
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    mQueue.add(stringRequest);

                }


                break;
            case R.id.tv_confirm:
                String password= et_password.getText().toString().trim() ;
                try {
                    byte[ ] encrypted = AesCBC.AES_CBC_Encrypt(password.getBytes("UTF-8"), AesCBC.key.getBytes("UTF-8"), AesCBC.iv.getBytes("UTF-8"));
                    psd_jiami = Base64.encode(encrypted);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if(et_num.getText().toString().trim().length()!=11){
                    Toast.makeText(ResetPasswordActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();

                }else if (!et_Code.getText().toString().trim().equals(strCode)){
                    Toast.makeText(ResetPasswordActivity.this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();

                }else if (et_password.getText().toString().trim().length()<6||et_password.getText().toString().trim().length()>15){
                    Toast.makeText(ResetPasswordActivity.this, "请输入6-15位的密码", Toast.LENGTH_SHORT).show();

                }else if (!et_password.getText().toString().trim().equals(et_password_confirm.getText().toString().trim())){
                    Toast.makeText(ResetPasswordActivity.this, "请输入相同的密码", Toast.LENGTH_SHORT).show();

                }else {
                    RequestQueue mQueue = Volley.newRequestQueue(ResetPasswordActivity.this);
                    StringRequest stringRequest = new StringRequest(
                            resetPassword + "?uNum=" + et_num.getText().toString().trim()+"&uPassword="+ URLEncoder.encode(psd_jiami) ,
//
                            new Response.Listener<String>() {
                                @SuppressLint("WrongConstant")
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        if("true".equals(response)){
                                            finish();
                                            Intent intent = new Intent(ResetPasswordActivity.this,LoginActivity.class);
                                            startActivity(intent);
                                        }

                                    } catch (Exception e) {
                                        Toast.makeText(ResetPasswordActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ResetPasswordActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

                        }
                    });
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    mQueue.add(stringRequest);

                }
                break;
        }
    }


}
