package leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mall.payfor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.base.BaseOtherActivity;
import leaflet.miaoa.qmxh.leaflet_simple.bean.ListActivityBean;
import leaflet.miaoa.qmxh.leaflet_simple.ui.adapter.ManagerAddressAdapter;
import leaflet.miaoa.qmxh.leaflet_simple.ui.widget.MyDividerItemDecoration;
import leaflet.miaoa.qmxh.leaflet_simple.utils.Common;

import static leaflet.miaoa.qmxh.leaflet_simple.Login.WelcomeActivity.Usertel;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.deleteConsigneeAddress;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.getQueryPagedAddress;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.initialTimeOutMs;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.updateDefaultAddress;

public class ManagerAddressActivity extends BaseOtherActivity {

    private RelativeLayout iv_back;
    private RelativeLayout rl_newAddress;
    private RecyclerView ry_address;

    private ManagerAddressAdapter manageAddressAdapter;
    private List<ListActivityBean.Address> addressesList = new ArrayList<ListActivityBean.Address>();
    private QMUITipDialog successDialog;
    private QMUITipDialog LoadingDialog;
    private QMUITipDialog failedDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_address);

        getQueryPagedAddress();
        initView();
        init();
    }

    private void initView() {
        iv_back = (RelativeLayout) findViewById(R.id.iv_back);
        rl_newAddress = (RelativeLayout) findViewById(R.id.rl_newAddress);
        ry_address = (RecyclerView) findViewById(R.id.ry_address);

    }
    private void init(){
        ry_address.setLayoutManager(new GridLayoutManager(this,1));
        ry_address.addItemDecoration(new MyDividerItemDecoration(this));
        manageAddressAdapter = new ManagerAddressAdapter(this,addressesList);
        ry_address.setAdapter(manageAddressAdapter);
        manageAddressAdapter.notifyDataSetChanged();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        //设为默认
        manageAddressAdapter.setOnChooseDefaultClickListener(new ManagerAddressAdapter.OnChooseDefaultClickListener() {
            @Override
            public void onChooseDefaultClick(View view, int position) {
                LoadingDialog = new QMUITipDialog.Builder(ManagerAddressActivity.this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                        .setTipWord("正在保存")
                        .create();
                LoadingDialog.show();
                updateDefaultAddress(addressesList.get(position).addressId);
            }
        });
        //编辑地址
        manageAddressAdapter.setmOnEditClickListener(new ManagerAddressAdapter.OnEditClickListener() {
            @Override
            public void onEditClick(View view, int position) {
                try{
                    String [] address=addressesList.get(position).getConsigneeAddress().split(" ",2);
                    Intent intent=new Intent(ManagerAddressActivity.this,EditAddressActivity.class);
                    intent.putExtra("addressId", addressesList.get(position).getAddressId());
                    intent.putExtra("consigneeName", addressesList.get(position).getConsigneeName());
                    intent.putExtra("consigneeNum", addressesList.get(position).getConsigneeNum());
                    intent.putExtra("defaultIf", addressesList.get(position).getDefaultIf());
                    intent.putExtra("address_area", address[0]);
                    intent.putExtra("address_detail", address[1]);
                    startActivityForResult(intent,1);
                }catch (Exception e){

                }

            }
        } );
        //删除地址
        manageAddressAdapter.setOnDeleteClickListener(new ManagerAddressAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(View view, int position) {
                //删除地址
                LoadingDialog = new QMUITipDialog.Builder(ManagerAddressActivity.this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                        .setTipWord("正在删除")
                        .create();
                LoadingDialog.show();
                deleteConsigneeAddres(addressesList.get(position).addressId);
            }
        });
        //新建地址
        rl_newAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ManagerAddressActivity.this,NewAddressActivity.class);
                startActivityForResult(intent,1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    addressesList.clear();
                    getQueryPagedAddress();

                }
                break;
        }

    }

    private void getQueryPagedAddress(){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                getQueryPagedAddress+"?addressNum=" + Usertel,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            if(Common.isNOT_Null(response)){
                                JSONObject jsonObject1 =new JSONObject(response);
                                String data=jsonObject1.getString("data");
                                JSONArray jsonArray =new JSONArray(data);
                                ListActivityBean listActivityBean=new ListActivityBean();
                                List<ListActivityBean.Address> addressesList_temp = new ArrayList<ListActivityBean.Address>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String addressId=jsonObject.getString("addressId");
                                    String consigneeName=jsonObject.getString("consigneeName");
                                    String consigneeNum=jsonObject.getString("consigneeNum");
                                    String consigneeAddress=jsonObject.getString("addressSre");
                                    String defaultIf=jsonObject.getString("defaultIf");
                                    ListActivityBean.Address address = listActivityBean.new Address();

                                    address.setAddressId(addressId);
                                    address.setConsigneeAddress(consigneeAddress);
                                    address.setConsigneeName(consigneeName);
                                    address.setConsigneeNum(consigneeNum);
                                    address.setDefaultIf(defaultIf);
                                    addressesList_temp.add(i,address);

                                }
                                for(int i =0;i<addressesList_temp.size();i++){
                                    if("true".equals(addressesList_temp.get(i).getDefaultIf())){
                                        addressesList.add(0,addressesList_temp.get(i));
                                        addressesList_temp.remove(i);
                                    }
                                }
                                addressesList.addAll(addressesList_temp);
                                manageAddressAdapter.notifyDataSetChanged();


                            }

                        } catch (Exception e) {

                            Toast.makeText(ManagerAddressActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ManagerAddressActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
    private void updateDefaultAddress(String addressId){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                updateDefaultAddress+"?addressId=" + addressId+"&addressNum="+Usertel,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                            if("true".equals(response)){
                                addressesList.clear();
                                getQueryPagedAddress();
                                LoadingDialog.dismiss();
                                successDialog = new QMUITipDialog.Builder(ManagerAddressActivity.this)
                                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                                        .setTipWord("设置成功")
                                        .create();
                                successDialog.show();

                                rl_newAddress.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        successDialog.dismiss();

                                    }
                                }, 1000);
                            }else {
                                LoadingDialog.dismiss();
                                failedDialog = new QMUITipDialog.Builder(ManagerAddressActivity.this)
                                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                                        .setTipWord("设置失败")
                                        .create();
                                failedDialog.show();
                                rl_newAddress.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        failedDialog.dismiss();

                                    }
                                }, 1000);
                            }



                        } catch (Exception e) {
                            LoadingDialog.dismiss();
                            Toast.makeText(ManagerAddressActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LoadingDialog.dismiss();
                Toast.makeText(ManagerAddressActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeOutMs,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
    private void deleteConsigneeAddres(String addressId){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                deleteConsigneeAddress+"?addressId=" + addressId,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                            if("true".equals(response)){
                                addressesList.clear();
                                getQueryPagedAddress();
                                LoadingDialog.dismiss();
                                successDialog = new QMUITipDialog.Builder(ManagerAddressActivity.this)
                                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                                        .setTipWord("删除成功")
                                        .create();
                                successDialog.show();
                                rl_newAddress.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        successDialog.dismiss();

                                    }
                                }, 1500);
                            }else {

                                failedDialog = new QMUITipDialog.Builder(ManagerAddressActivity.this)
                                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                                        .setTipWord("删除失败")
                                        .create();
                                failedDialog.show();
                                rl_newAddress.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        failedDialog.dismiss();

                                    }
                                }, 1500);
                            }



                        } catch (Exception e) {
                            LoadingDialog.dismiss();
                            Toast.makeText(ManagerAddressActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LoadingDialog.dismiss();
                Toast.makeText(ManagerAddressActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeOutMs,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
}
