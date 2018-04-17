package leaflet.miaoa.qmxh.leaflet_simple.Login;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.base.BaseFragment;
import leaflet.miaoa.qmxh.leaflet_simple.ui.merchantHomePage.SellerHomePageActivity;
import leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.PersonalUserHomePageActivity;
import leaflet.miaoa.qmxh.leaflet_simple.utils.Common;

import static leaflet.miaoa.qmxh.leaflet_simple.Login.WelcomeActivity.Usertel;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.loginpsd;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.protocolUrl;
import static leaflet.miaoa.qmxh.leaflet_simple.utils.GetMacAddress.getAdresseMAC;

/**
 * 密码登录页面
 */
public class PasswordLoginFragment extends BaseFragment implements View.OnClickListener{

    private TextView tvForgetPassword,tvLogin,tv_protocol;
    private EditText et_num,et_psd;
    private CheckBox checkboxButton;
    private CheckBox checkBoxProtocol;

//    private ImageView ivPerson,ivSeller;
    private int jumpFlag = 1;//1是用户，0是商家
    private ImageView iv_choose_type;
    public CheckBox mcheckBtn;
    private String isLoginstr = "n", tel="";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private QMUITipDialog LondingDialog;
    @Override
    protected View setContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password_login,null);
        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();//获取编辑器

        tel=sharedPreferences.getString("tel", "");


        return view;
    }

    @Override
    protected void setFindViewById(View view) {
        tvForgetPassword = (TextView) view.findViewById(R.id.tv_imessage_login_forgetpassword);
        tvLogin = (TextView) view.findViewById(R.id.tv_password_login);
        tv_protocol = (TextView) view.findViewById(R.id.tv_protocol);
        et_num = (EditText) view.findViewById(R.id.et_num);
        et_psd = (EditText) view.findViewById(R.id.et_psd);

//        ivPerson = (ImageView) view.findViewById(R.id.iv_password_login_person);
//        ivSeller = (ImageView) view.findViewById(R.id.iv_password_login_seller);
        checkboxButton = (CheckBox) view.findViewById(R.id.checkBoxLogin);
        checkBoxProtocol = (CheckBox) view.findViewById(R.id.checkBoxProtocol);
        checkBoxProtocol.setChecked(true);
        if(Common.isNOT_Null(tel)==true){
            et_num.setText(tel);
        }
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
        });
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
        tvForgetPassword.setOnClickListener(this);
        tvLogin.setOnClickListener(this);

    }

    @Override
    protected void setControl() {
        clearAllStyle();
    }



    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.tv_imessage_login_forgetpassword:
                getActivity().finish();
                intent.setClass(getActivity(),ResetPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_password_login:
                    final String telString = et_num.getText().toString().trim();
                    String psdString = et_psd.getText().toString().trim();
                final boolean CheckBoxLogin = checkboxButton.isChecked();
                final boolean cbProtocol = checkBoxProtocol.isChecked();
                //记住密码
                if (CheckBoxLogin)
                {
                    isLoginstr="y";
                }else {
                    isLoginstr="n";
                }

                    if (TextUtils.isEmpty(telString) || telString.length() != 11) {
                        Toast.makeText(getActivity(), "请输入有效的手机号", Toast.LENGTH_SHORT).show();

                    } else {



                        if (TextUtils.isEmpty(psdString)) {
                            Toast.makeText(getActivity(), "请输入密码", Toast.LENGTH_SHORT).show();

                        } else if(!cbProtocol){
                            Toast.makeText(getActivity(), "请先阅读并遵守相关协议", Toast.LENGTH_SHORT).show();
                        }  else{
                            LondingDialog = new QMUITipDialog.Builder(getActivity())
                                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                                    .setTipWord("正在登陆中···")
                                    .create();

                            LondingDialog.show();


                            if (jumpFlag == 1){


                            RequestQueue mQueue = Volley.newRequestQueue(getActivity());
                            StringRequest stringRequest = new StringRequest(
                                    loginpsd + "?uNum=" + telString + "&uPassword="+psdString + "&umark=" + jumpFlag + "&isLogin=" + isLoginstr +"&uMac="+getAdresseMAC(getActivity()),
//                        doInUser+"?uname="+tel,
                                    new Response.Listener<String>() {
                                        @SuppressLint("WrongConstant")
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                LondingDialog.dismiss();
                                                String result[]=response.split(",");
                                                if ("false".equals(result[0])&&"true".equals(result[1])) {
                                                    editor.putString("tel", telString);
                                                    editor.putInt("umark", jumpFlag);
                                                    if("y".equals(isLoginstr)){
                                                        editor.putString("islogin", "true");
                                                    }else {
                                                        editor.putString("islogin", "false");
                                                    }

                                                    editor.commit();
                                                    Usertel=telString;
                                                    getActivity().finish();
                                                    Intent intent = new Intent(getActivity(), PersonalUserHomePageActivity.class);
                                                    startActivity(intent);

                                                } else if("true".equals(result[0])){
                                                    Toast.makeText(getActivity(), "该帐号已被拉黑！", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    Toast.makeText(getActivity(), "账号密码不正确！", Toast.LENGTH_SHORT).show();
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
                                        loginpsd + "?uNum=" + telString + "&uPassword="+psdString + "&umark=" + jumpFlag + "&isLogin=" + isLoginstr+"&uMac="+getAdresseMAC(getActivity()) ,
//                        doInUser+"?uname="+tel,
                                        new Response.Listener<String>() {
                                            @SuppressLint("WrongConstant")
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    LondingDialog.dismiss();
                                                    String result[]=response.split(",");
                                                    if ("false".equals(result[0])&&"true".equals(result[1])) {
                                                        editor.putString("tel", telString);
                                                        editor.putInt("umark", jumpFlag);
                                                        if("y".equals(isLoginstr)){
                                                            editor.putString("islogin", "true");
                                                        }else {
                                                            editor.putString("islogin", "false");
                                                        }
                                                        editor.commit();
                                                        Usertel=telString;
                                                        getActivity().finish();
                                                        Intent intent = new Intent(getActivity(), SellerHomePageActivity.class);
                                                        startActivity(intent);

                                                    } else if("true".equals(result[0])){
                                                        Toast.makeText(getActivity(), "该帐号已被拉黑！", Toast.LENGTH_SHORT).show();
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

                            } else {
                                Toast.makeText(getActivity(), "请选择登录类型", Toast.LENGTH_SHORT).show();
                            }
                    }
                }




                break;

        }
    }

    private void clearAllStyle() {

    }
}
