package leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mall.payfor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.base.BaseOtherActivity;
import leaflet.miaoa.qmxh.leaflet_simple.bean.ListActivityBean;
import leaflet.miaoa.qmxh.leaflet_simple.ui.widget.CallPhoneDialog;
import leaflet.miaoa.qmxh.leaflet_simple.ui.widget.LogisticsInformationView;

import static leaflet.miaoa.qmxh.leaflet_simple.utils.RequestDemo.getTraceInfo;

public class Logistics_informationActivity extends BaseOtherActivity {

    private RelativeLayout iv_back;
    private TextView tv_expressNumber;
    private TextView btn;
    private TextView logistics_phone;
    private static LogisticsInformationView logistics_InformationView;

    private String expressNumber;
    static List<ListActivityBean.LogisticsData> logisticsDataList=new ArrayList<ListActivityBean.LogisticsData>();

    CallPhoneDialog callPhoneDialog;
    public static String response = "";
    public static Handler handler_logistics_information=new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    response=(String)msg.obj;
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String data=jsonObject.getString("data");
                        JSONArray jsonArray =new JSONArray(data);
                        ListActivityBean listActivityBean = new ListActivityBean();
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String traces=jsonObject1.getString("traces");
                        JSONArray jsonArray1 =new JSONArray(traces);
                        List<ListActivityBean.LogisticsData> logisticsDataList_temp=new ArrayList<ListActivityBean.LogisticsData>();
                        for (int i=0;i<jsonArray1.length();i++){
                            JSONObject jsonObject2=jsonArray1.getJSONObject(i);
                            String desc=jsonObject2.getString("desc");
                            String scanDate=jsonObject2.getString("scanDate");
                            ListActivityBean.LogisticsData logisticsData = listActivityBean.new LogisticsData();
                            logisticsData.setContext(desc);
                            logisticsData.setTime(scanDate);
                            logisticsDataList_temp.add(i,logisticsData);
                        }
                        for(int i=0;i<logisticsDataList_temp.size();i++){
                            logisticsDataList.add(i,logisticsDataList_temp.get(logisticsDataList_temp.size()-1-i));
                        }
                        logistics_InformationView.setLogisticsDataList(logisticsDataList);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;


            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics_information);
        Intent intent=getIntent();
        expressNumber=intent.getStringExtra("expressNumber");
        initView();
        init();
    }

    private void initView() {
        ListActivityBean listActivityBean = new ListActivityBean();
        iv_back = (RelativeLayout) findViewById(R.id.iv_back);
        tv_expressNumber = (TextView) findViewById(R.id.expressNumber);

        logistics_phone = (TextView) findViewById(R.id.logistics_phone);
        logistics_InformationView = (LogisticsInformationView) findViewById(R.id.logistics_InformationView);








    }
    private void init(){
        tv_expressNumber.setText("  "+expressNumber);
        getTraceInfo("['"+expressNumber+"']","TRACEINTERFACE_NEW_TRACES");//获取快件轨迹信息
        logistics_InformationView.setOnPhoneClickListener(new LogisticsInformationView.OnPhoneClickListener() {
            @Override
            public void onPhoneClick(String phoneNumber) {
                dialogCreateCall(phoneNumber).show();
            }
        });
        logistics_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCreateCall(logistics_phone.getText().toString().trim()).show();
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }




    private CallPhoneDialog dialogCreateCall(String phoneNumber) {
        if (callPhoneDialog == null) {
            callPhoneDialog = new CallPhoneDialog(this,phoneNumber);
        }else{
            callPhoneDialog.setPhoneNumber(phoneNumber);
        }
        return callPhoneDialog;
    }
}
