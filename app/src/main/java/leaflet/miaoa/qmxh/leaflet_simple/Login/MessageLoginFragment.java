package leaflet.miaoa.qmxh.leaflet_simple.Login;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.base.BaseFragment;
import leaflet.miaoa.qmxh.leaflet_simple.ui.merchantHomePage.SellerHomePageActivity;
import leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.PersonalUserHomePageActivity;
import leaflet.miaoa.qmxh.leaflet_simple.utils.Common;

import static leaflet.miaoa.qmxh.leaflet_simple.Login.WelcomeActivity.Usertel;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.SMS_127135073;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.getCode;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.isExist;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.loginMsg;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.protocolUrl;
import static leaflet.miaoa.qmxh.leaflet_simple.utils.GetMacAddress.getAdresseMAC;


/**
 * 短信验证码登录页面
 */
public class MessageLoginFragment extends BaseFragment implements View.OnClickListener{

    private TextView tvGetNum,tv_confirm_msg,et_num,et_Code,tv_protocol;
    private CheckBox checkBoxProtocol;
    private CountDownTimer countDownTimer;
    private String strCode="", tel="";
    private int jumpFlag = 1;

    private ImageView iv_img;
    private ImageView iv_choose_type;
    public CheckBox mcheckBtn;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private QMUITipDialog LondingDialog;
    @Override
    protected View setContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_login,null);
        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();//获取编辑器


        tel=sharedPreferences.getString("tel", "");
        return view;
    }

    @Override
    protected void setFindViewById(View view) {
        tvGetNum = (TextView) view.findViewById(R.id.tv_register_getnum);
        et_num = (TextView) view.findViewById(R.id.et_num);
        et_Code = (TextView) view.findViewById(R.id.et_Code);
        tv_confirm_msg = (TextView) view.findViewById(R.id.tv_confirm_msg);
        if(Common.isNOT_Null(tel)==true){
            et_num.setText(tel);
        }

        tv_protocol = (TextView) view.findViewById(R.id.tv_protocol);
        checkBoxProtocol = (CheckBox) view.findViewById(R.id.checkBoxProtocol);
        checkBoxProtocol.setChecked(true);//默认选中状态
        iv_img = (ImageView) view.findViewById(R.id.iv_img);
        iv_choose_type = (ImageView) view.findViewById(R.id.iv_choose_type);
        mcheckBtn = (CheckBox) view.findViewById(R.id.mTogBtn); // 获取到控件
        mcheckBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    //选中
                    jumpFlag = 0;
                    iv_choose_type.setImageDrawable(getResources().getDrawable(R.mipmap.login_switch_on));
                }else{
                    //未选中
                    jumpFlag = 1;
                    iv_choose_type.setImageDrawable(getResources().getDrawable(R.mipmap.login_switch_off));
                }
            }
        });// 添加监听事件

        tv_protocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(),WB_ProtocolActivity.class);
                intent.putExtra("pUrl",protocolUrl);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void setListener() {
        tvGetNum.setOnClickListener(this);
        tv_confirm_msg.setOnClickListener(this);

    }

    @Override
    protected void setControl() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_register_getnum:
                final String telString = et_num.getText().toString().trim();
                final boolean cbProtocol = checkBoxProtocol.isChecked();
                if (TextUtils.isEmpty(telString) || telString.length() != 11) {
                    Toast.makeText(getActivity(), "请输入有效的手机号", Toast.LENGTH_SHORT).show();

                }else {
                   if(!cbProtocol){
                        Toast.makeText(getActivity(), "请先阅读并遵守相关协议", Toast.LENGTH_SHORT).show();
                    } else{
                       isExist(telString);

                   }



                }
                break;
            case R.id.tv_confirm_msg:
                if(et_num.getText().toString().trim().length()!=11){
                    Toast.makeText(getActivity(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();

                }else if (!et_Code.getText().toString().trim().equals(strCode)){
                    Toast.makeText(getActivity(), "请输入正确的验证码", Toast.LENGTH_SHORT).show();

                }else {
                    LondingDialog = new QMUITipDialog.Builder(getActivity())
                            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                            .setTipWord("正在登陆中···")
                            .create();

                    LondingDialog.show();
                    if (jumpFlag == 1){


                        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
                        StringRequest stringRequest = new StringRequest(
                                loginMsg + "?uNum=" + et_num.getText().toString().trim()+"&umark="+jumpFlag +"&uMac="+getAdresseMAC(getActivity()) ,
//
                                new Response.Listener<String>() {
                                    @SuppressLint("WrongConstant")
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            LondingDialog.dismiss();

                                            String result[]=response.split(",");
                                            if ("false".equals(result[0])&&"true".equals(result[1])) {
                                                editor.putString("tel",  et_num.getText().toString().trim());
                                                editor.putInt("umark", jumpFlag);
                                                editor.putString("islogin", "true");
                                                editor.commit();
                                                Usertel= et_num.getText().toString().trim();
                                                getActivity().finish();
                                                Intent intent = new Intent(getActivity(), PersonalUserHomePageActivity.class);
                                                startActivity(intent);

                                            } else if("true".equals(result[0])){
                                                Toast.makeText(getActivity(), "该帐号已被拉黑！", Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(getActivity(), "账号不存在", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (Exception e) {
                                            LondingDialog.dismiss();
                                            Toast.makeText(getActivity(), "数据异常", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                LondingDialog.dismiss();
                                Toast.makeText(getActivity(), "请检查网络设置", Toast.LENGTH_SHORT).show();

                            }
                        });
                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        mQueue.add(stringRequest);
                    }
                    else if (jumpFlag == 0){
                        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
                        StringRequest stringRequest = new StringRequest(
                                loginMsg + "?uNum=" + et_num.getText().toString().trim()+"&umark="+jumpFlag+"&uMac="+getAdresseMAC(getActivity())  ,
//
                                new Response.Listener<String>() {
                                    @SuppressLint("WrongConstant")
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            LondingDialog.dismiss();
                                            String result[]=response.split(",");
                                            if ("false".equals(result[0])&&"true".equals(result[1])) {
                                                editor.putString("tel",  et_num.getText().toString().trim());
                                                editor.putInt("umark", jumpFlag);
                                                editor.putString("islogin", "true");
                                                editor.commit();
                                                Usertel= et_num.getText().toString().trim();
                                                getActivity().finish();
                                                Intent intent = new Intent(getActivity(),SellerHomePageActivity.class);
                                                startActivity(intent);
                                            } else if("true".equals(result[0])){
                                                Toast.makeText(getActivity(), "该帐号已被拉黑！", Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(getActivity(), "账号不存在", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (Exception e) {
                                            LondingDialog.dismiss();
                                            Toast.makeText(getActivity(), "数据异常", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                LondingDialog.dismiss();
                                Toast.makeText(getActivity(), "请检查网络设置", Toast.LENGTH_SHORT).show();

                            }
                        });
                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        mQueue.add(stringRequest);

                    }


                }
                break;

        }
    }
    private void isExist(String tel){
        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(
                isExist + "?uNum=" + tel ,
//
                new Response.Listener<String>() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onResponse(String response) {
                        try {
                            if("2".equals(response)){
                                getCode(tel);
                                if (countDownTimer == null){
                                    countDownTimer = new CountDownTimer(60000,1000) {
                                        @Override
                                        public void onTick(long l) {
                                            String time = l/1000+"s";
                                            tvGetNum.setClickable(false);
                                            tvGetNum.setText(time);
                                            tvGetNum.setTextColor(Color.parseColor("#999999"));


                                        }

                                        @Override
                                        public void onFinish() {
                                            tvGetNum.setText("获取验证码");
                                            tvGetNum.setClickable(true);
                                            tvGetNum.setTextColor(Color.parseColor("#333333"));
                                            countDownTimer = null;
                                        }
                                    }.start();
                                }
                            }else {
                                Toast.makeText(getActivity(), "该账号不存在", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
    private void getCode(String telString){
        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(
                getCode + "?phoneNumber=" + telString + "&templateParam="+SMS_127135073,
//
                new Response.Listener<String>() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onResponse(String response) {
                        try {
                            String str[]=response.split("<-->");
                            String isSuccess=str[0];
                            if("false".equals(isSuccess)){
                                Toast.makeText(getActivity(), "一小时最多5条，一天最多10条", Toast.LENGTH_LONG).show();
                            }else if("ok".equalsIgnoreCase(isSuccess)){
                                strCode=str[1];
                            }
                        } catch (Exception e) {

                            Toast.makeText(getActivity(), "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }

}
