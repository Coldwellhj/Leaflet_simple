package leaflet.miaoa.qmxh.leaflet_simple.ui.merchantHomePage.mine;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.PersonalUserHomePageActivity;
import leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mine.ContactCustomerServiceActivity;
import leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mine.FeedBackActivity;
import leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mine.PersonalNewsList;
import leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mine.Personal_mine_info_Activity;
import leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mine.SellerSettingActivity;
import leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mine.Withdraw_cash_thirdActivity;
import leaflet.miaoa.qmxh.leaflet_simple.ui.widget.ZQImageViewRoundOval;

import static leaflet.miaoa.qmxh.leaflet_simple.Login.WelcomeActivity.Usertel;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.useraction_balance;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.useraction_tomodifyUser;


public class Seller_mine_Fragment extends Fragment implements View.OnClickListener{

    View view;
    private ZQImageViewRoundOval iv_head;
    private RelativeLayout rl_message;
    private RelativeLayout rl_feed_back;
    private RelativeLayout rl_switch_seller;
    private RelativeLayout rl_help;
    private RelativeLayout rl_setting;
    private RelativeLayout rl_adv_design;
    private TextView userName;
    private TextView value_of_loan;
    private LinearLayout withdraw_cash;
    private String uNick="";
    private String uSex="";
    private String uBirthday="1520870400000";
    private String uImg="";
    private String mybalance="";
    private QMUITipDialog LondingDialog;
    private QMUITipDialog LondingDialog1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);//沙箱
        view = inflater.inflate(R.layout.activity_seller_mine_, null, false);
        initView();

        getPersonalinfo();
        getPersonalBalance();
        LondingDialog = new QMUITipDialog.Builder(getActivity())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("加载中")
                .create();

        LondingDialog.show();
        LondingDialog1 = new QMUITipDialog.Builder(getActivity())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("加载中")
                .create();
        LondingDialog1.show();
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    private void initView() {
        iv_head=(ZQImageViewRoundOval)view.findViewById(R.id.iv_head);
        userName=(TextView)view.findViewById(R.id.UserName);
        value_of_loan=(TextView)view.findViewById(R.id.value_of_loan);
        withdraw_cash=(LinearLayout)view.findViewById(R.id.withdraw_cash);
        rl_setting=(RelativeLayout)view.findViewById(R.id.rl_setting);
        rl_message=(RelativeLayout)view.findViewById(R.id.rl_message);
        rl_feed_back=(RelativeLayout)view.findViewById(R.id.rl_feed_back);
        rl_adv_design=(RelativeLayout)view.findViewById(R.id.rl_adv_design);
        rl_switch_seller=(RelativeLayout)view.findViewById(R.id.rl_switch_seller);
        rl_help=(RelativeLayout)view.findViewById(R.id.rl_help);
        iv_head.setOnClickListener(this);
        rl_message.setOnClickListener(this);
        rl_feed_back.setOnClickListener(this);
        withdraw_cash.setOnClickListener(this);
        rl_switch_seller.setOnClickListener(this);
        rl_help.setOnClickListener(this);
        rl_setting.setOnClickListener(this);
        rl_adv_design.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_head:
                Intent intent =new Intent(getActivity(),Personal_mine_info_Activity.class);
                intent.putExtra("uNick",uNick);
                intent.putExtra("uSex",uSex);
                intent.putExtra("uBirthday",uBirthday);
                intent.putExtra("uImg",uImg);
                startActivityForResult(intent,0);
            break;
            case R.id.rl_message:
                Intent intent1=new Intent(getActivity(),PersonalNewsList.class);
                startActivity(intent1);
                break;
            case R.id.rl_feed_back:
                Intent intent2 =new Intent(getActivity(),FeedBackActivity.class);
                startActivity(intent2);
                break;
          case R.id.rl_switch_seller:
                getActivity().finish();
              SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
              SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
              editor.putInt("umark", 1);
              editor.commit();
                Intent intent3 =new Intent(getActivity(),PersonalUserHomePageActivity.class);
                startActivity(intent3);
                break;
            case R.id.rl_help:
                Intent intent4 =new Intent(getActivity(),ContactCustomerServiceActivity.class);
                startActivity(intent4);
                break;
            case R.id.rl_setting:
                Intent intent5 =new Intent(getActivity(),SellerSettingActivity.class);
                intent5.putExtra("flag","seller");
                startActivity(intent5);
                break;
            case R.id.rl_adv_design:

                break;

            case R.id.withdraw_cash:
               Intent intent8=new Intent(getActivity(),Withdraw_cash_thirdActivity.class);
                intent8.putExtra("my_balance",mybalance);
                startActivityForResult(intent8,0);
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Bundle bundle = data.getExtras();
                    String photoName= bundle.getString("photoName");
                    Picasso.with(getActivity()).load( photoName).into(iv_head);
                    getPersonalinfo();
                    getPersonalBalance();
                    LondingDialog = new QMUITipDialog.Builder(getActivity())
                            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                            .setTipWord("加载中")
                            .create();
                    LondingDialog.show();
                    LondingDialog1 = new QMUITipDialog.Builder(getActivity())
                            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                            .setTipWord("加载中")
                            .create();
                    LondingDialog1.show();
                    break;
                case 2:
                    getPersonalBalance();
                    break;
            }

        }
    }



    //获取用户信息
    private void getPersonalinfo() {
        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(
                useraction_tomodifyUser + "?uNum=" + Usertel  ,
//
                new Response.Listener<String>() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject =new JSONObject(response);

                            uNick=jsonObject.getString("uNick");
                            uImg=jsonObject.getString("uImg");
                            uSex=jsonObject.getString("uSex");
                            uBirthday=jsonObject.getString("uBirthday");
                            userName.setText(uNick);
                            if("lulu.jpg".equalsIgnoreCase(uImg)){

                            }else {
                                Picasso.with(getActivity()).load( uImg).into(iv_head);
                            }

                        } catch (Exception e) {

                            Toast.makeText(getActivity(), "数据异常", Toast.LENGTH_SHORT).show();
                        }
                        LondingDialog.dismiss();
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
    public void getPersonalBalance() {

        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(
                useraction_balance + "?uNum=" + Usertel  ,
//
                new Response.Listener<String>() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onResponse(String response) {

                        try {
                            mybalance=response;
                            value_of_loan.setText(response+"元");

                        } catch (Exception e) {

                            Toast.makeText(getActivity(), "数据异常", Toast.LENGTH_SHORT).show();
                        }
                        LondingDialog1.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LondingDialog1.dismiss();
                Toast.makeText(getActivity(), "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }



}
