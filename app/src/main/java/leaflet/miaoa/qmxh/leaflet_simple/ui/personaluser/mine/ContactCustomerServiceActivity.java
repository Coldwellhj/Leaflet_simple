package leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.base.BaseOtherActivity;


public class ContactCustomerServiceActivity extends BaseOtherActivity {

    private ImageView iv_contact_customer_service_back;
    private ImageView iv_contact_customer_service_home;
    private EditText problems;
    private TextView tv_contact_customer_service_call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_customer_service);
        initView();
    }

    private void initView() {
        iv_contact_customer_service_back = (ImageView) findViewById(R.id.iv_contact_customer_service_back);
        iv_contact_customer_service_home = (ImageView) findViewById(R.id.iv_contact_customer_service_home);
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
    }

    private void submit() {
        // validate
        String problemsString = problems.getText().toString().trim();
        if (TextUtils.isEmpty(problemsString)) {
            Toast.makeText(this, "请再次输入您在使用中遇到的问题，尽量描述详细，我们将尽快解决问题，竭诚感谢您对我们产品的支持！", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


    }
}
