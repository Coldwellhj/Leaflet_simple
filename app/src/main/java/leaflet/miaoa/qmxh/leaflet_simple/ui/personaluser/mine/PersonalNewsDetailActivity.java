package leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.base.BaseOtherActivity;

import static leaflet.miaoa.qmxh.leaflet_simple.utils.DateUtils.getFormatTimeFromNow;
import static leaflet.miaoa.qmxh.leaflet_simple.utils.DateUtils.getTimeFromLong;

public class PersonalNewsDetailActivity extends BaseOtherActivity {

    private RelativeLayout iv_back;
    private TextView newsName;
    private TextView newsTime;
    private TextView news_content;
    private String theme;
    private Long time;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_news_detail);
        Intent intent=getIntent();
        theme=intent.getStringExtra("news_theme");
        time=intent.getLongExtra("news_time",0);
        content= intent.getStringExtra("news_content");
        initView();
        init();
    }

    private void initView() {
        iv_back = (RelativeLayout) findViewById(R.id.iv_back);
        newsName = (TextView) findViewById(R.id.newsName);
        newsTime = (TextView) findViewById(R.id.newsTime);
        news_content = (TextView) findViewById(R.id.news_content);
    }
    private void init(){
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        newsName.setText(theme);
        newsTime.setText(getFormatTimeFromNow(getTimeFromLong(time)));
        news_content.setText(content);
    }
}
