package leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.base.BaseOtherActivity;

public class AboutusActivity extends BaseOtherActivity {

    private RelativeLayout iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        initView();
    }

    private void initView() {
        iv_back = (RelativeLayout) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
