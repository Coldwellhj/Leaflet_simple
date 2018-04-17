package leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mine;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
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

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.base.BaseOtherActivity;

import static leaflet.miaoa.qmxh.leaflet_simple.Login.WelcomeActivity.Usertel;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.addUserFeedBack;


public class FeedBackActivity extends BaseOtherActivity implements View.OnClickListener{

    private ImageView iv_seller_my_feed_back_back;
    private EditText problem;
    private EditText advice;
    private TextView commit;
    String problemString,adviceString;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_feed_back);
        initView();
    }

    private void initView() {
        iv_seller_my_feed_back_back = (ImageView) findViewById(R.id.iv_seller_my_feed_back_back);
        problem = (EditText) findViewById(R.id.problem);
        advice = (EditText) findViewById(R.id.advice);
        commit = (TextView) findViewById(R.id.commit);
        iv_seller_my_feed_back_back.setOnClickListener(this);
        commit.setOnClickListener(this);
    }

    private boolean submit() {
        // validate
         problemString = problem.getText().toString().trim();
        adviceString = advice.getText().toString().trim();
        if (TextUtils.isEmpty(problemString)&&TextUtils.isEmpty(adviceString)) {
            Toast.makeText(this, "请在此输入您在使用中遇到的问题", Toast.LENGTH_SHORT).show();
            return false;
        }



        return true;
        // TODO validate success, do something


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_seller_my_feed_back_back:
                finish();
                break;
            case R.id.commit:
                if(submit()){
                    progressDialog = new ProgressDialog(FeedBackActivity.this,
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("正在修改中...");
                    progressDialog.show();
                    updateuserinfo_birthday();

                }
                break;
        }

    }
    private void updateuserinfo_birthday() {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                addUserFeedBack + "?fbNum=" + Usertel +"&fbQuestion="+problemString+"&fbAdvise="+ adviceString,
//
                new Response.Listener<String>() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            if("true".equals(response)){
                                finish();
                                Toast.makeText(FeedBackActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(FeedBackActivity.this, "提交失败，数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(FeedBackActivity.this, "提交失败，请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
}
