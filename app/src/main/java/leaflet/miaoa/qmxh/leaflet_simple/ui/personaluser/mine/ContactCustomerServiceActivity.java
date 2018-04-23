package leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mine;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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


public class ContactCustomerServiceActivity extends BaseOtherActivity {

    private ImageView iv_contact_customer_service_back;
    private ImageView iv_contact_customer_service_home;
    private EditText problems;
    private TextView tv_contact_customer_service_call;
    private TextView commit;
    String problemString;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_customer_service);
        initView();
    }

    private void initView() {
        iv_contact_customer_service_back = (ImageView) findViewById(R.id.iv_contact_customer_service_back);
        iv_contact_customer_service_home = (ImageView) findViewById(R.id.iv_contact_customer_service_home);
        commit = (TextView) findViewById(R.id.commit);
        problems = (EditText) findViewById(R.id.problems);
        tv_contact_customer_service_call = (TextView) findViewById(R.id.tv_contact_customer_service_call);
        tv_contact_customer_service_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:0512-88869359" );
                intent.setData(data);
                startActivity(intent);
            }
        });
        iv_contact_customer_service_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(submit()){
                    progressDialog = new ProgressDialog(ContactCustomerServiceActivity
                            .this,
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("正在修改中...");
                    progressDialog.show();
                    updateuserinfo_birthday();

                }
            }
        });
    }

    private boolean submit() {
        // validate
        problemString = problems.getText().toString().trim();
        if (TextUtils.isEmpty(problemString)) {
            Toast.makeText(this, "请再次输入您在使用中遇到的问题，尽量描述详细，我们将尽快解决问题，竭诚感谢您对我们产品的支持！", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
        // TODO validate success, do something


    }
    private void updateuserinfo_birthday() {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                addUserFeedBack + "?fbNum=" + Usertel +"&fbQuestion="+problemString+"&fbAdvise=",
//
                new Response.Listener<String>() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            if("true".equals(response)){
                                finish();
                                Toast.makeText(ContactCustomerServiceActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ContactCustomerServiceActivity.this, "提交失败，数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ContactCustomerServiceActivity.this, "提交失败，请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
}
