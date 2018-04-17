package leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.view.Gravity;
import android.view.View;
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
import com.youth.banner.Banner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.base.BaseOtherActivity;
import leaflet.miaoa.qmxh.leaflet_simple.bean.ListActivityBean;
import leaflet.miaoa.qmxh.leaflet_simple.ui.adapter.GetGoodsIntroduceAdapter;
import leaflet.miaoa.qmxh.leaflet_simple.ui.adapter.GetGoodsPicturesAdapter;
import leaflet.miaoa.qmxh.leaflet_simple.ui.adapter.GetGoodsRulersAdapter;
import leaflet.miaoa.qmxh.leaflet_simple.ui.widget.GlideImageLoader;
import leaflet.miaoa.qmxh.leaflet_simple.ui.widget.MaxRecyclerView;
import leaflet.miaoa.qmxh.leaflet_simple.ui.widget.MyPayPopupWindow;
import leaflet.miaoa.qmxh.leaflet_simple.utils.ToastUtils;

import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.getCoinAttribute;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.getCoinById;

public class PersonalHomegetGoodsByIdActivity extends BaseOtherActivity {

    private RelativeLayout iv_back;
    private RelativeLayout head;
    private Banner banner;
    private TextView cNowPrice;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private RelativeLayout rl1;
    private TextView tv_describe;
    private MaxRecyclerView rv_describe;
    private MaxRecyclerView rv_picture;
    private TextView pay_regular;
    private TextView pay_text;
    private MaxRecyclerView rv_regular;
    private RelativeLayout pay;
    private String cId="";
    private Activity activity;
    private GetGoodsIntroduceAdapter getGoodsIntroduceAdapter;
    private GetGoodsRulersAdapter getGoodsRulersAdapter;
    private GetGoodsPicturesAdapter getGoodsPicturesAdapter;
    private MyPayPopupWindow myPopupWindow;
    private List<String>advList  = new ArrayList<String>();
    private List<String>introduceList  = new ArrayList<String>();
    private List<String>rulesList  = new ArrayList<String>();
    private List<ListActivityBean.Goods_shopping_mall> goods_shopping_malls_list = new ArrayList<ListActivityBean.Goods_shopping_mall>();
    private List<ListActivityBean.Attribute> attributeList = new ArrayList<ListActivityBean.Attribute>();
    private String attributeStr;
    private double MaxPrice;
    private double minPrice;
    private QMUITipDialog LondingDialog;
    Handler handler=new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    getCoinAttribute();
                    banner.setImages(advList)
                            .setImageLoader(new GlideImageLoader())
                            .start();
//                    cNowPrice.setText(goods_shopping_malls_list.get(0).getcNowPrice());
//                    tv1.setText("¥"+goods_shopping_malls_list.get(0).getcFormerPrice());
//                    tv1.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
//                    callPhone=new CallPhone(activity,goods_shopping_malls_list.get(0).getsLegalPhone());
                    if("true".equals(goods_shopping_malls_list.get(0).getcSoldOut())){
                        pay.setClickable(false);
                        pay.setBackgroundColor(getResources().getColor(R.color.color_tv_cFormerPrice));
                        pay_text.setText("已下架");
                    }else {
                        pay.setClickable(true);
                        pay.setBackgroundColor(getResources().getColor(R.color.goods_red));
                        pay_text.setText("立即购买");
                    }
                    break;
                case 2:
                    //                            //显示价格
                            if(attributeList.size()>0){


                                MaxPrice=attributeList.get(0).getAttributePrice();
                                minPrice=attributeList.get(0).getAttributePrice();
                                for(int i=0;i<attributeList.size();i++){
                                    if(attributeList.get(i).getAttributePrice()>MaxPrice){
                                        MaxPrice=attributeList.get(i).getAttributePrice();
                                    }
                                    if(attributeList.get(i).getAttributePrice()<minPrice){
                                        minPrice=attributeList.get(i).getAttributePrice();
                                    }
                                }

                                if(minPrice==MaxPrice){
                                    cNowPrice.setText(""+minPrice);
                                }else {
                                    cNowPrice.setText(""+minPrice+"-"+MaxPrice);
                                }
                            }else {
                                cNowPrice.setText("0");
                            }

                    break;

            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_homeget_goods_by_id);
        initView();
        Intent intent =getIntent();
        cId=intent.getStringExtra("cId");
        activity=this;
        LondingDialog = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("加载中···")
                .create();

        LondingDialog.show();
        httpGetCoinById();
    }

    private void initView() {
        iv_back = (RelativeLayout) findViewById(R.id.iv_back);
        head = (RelativeLayout) findViewById(R.id.head);
        banner = (Banner) findViewById(R.id.banner);
        cNowPrice = (TextView) findViewById(R.id.cNowPrice);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        rl1 = (RelativeLayout) findViewById(R.id.rl1);
        tv_describe = (TextView) findViewById(R.id.tv_describe);
        rv_describe = (MaxRecyclerView) findViewById(R.id.rv_describe);
        rv_picture = (MaxRecyclerView) findViewById(R.id.rv_picture);
        pay_regular = (TextView) findViewById(R.id.pay_regular);
        pay_text = (TextView) findViewById(R.id.pay_text);
        rv_regular = (MaxRecyclerView) findViewById(R.id.rv_regular);
        pay = (RelativeLayout) findViewById(R.id.pay);


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //支付
                if(attributeList.size()>0){

                    myPopupWindow = new MyPayPopupWindow(PersonalHomegetGoodsByIdActivity.this,attributeList);
                    myPopupWindow.showAtLocation(PersonalHomegetGoodsByIdActivity.this.findViewById(R.id.head),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }else {
                    ToastUtils.showShort(PersonalHomegetGoodsByIdActivity.this,"暂无可购买的商品");
                }
            }
        });

        rv_describe.setLayoutManager(new GridLayoutManager(this,1));
        rv_describe.setNestedScrollingEnabled(false);
        getGoodsIntroduceAdapter = new GetGoodsIntroduceAdapter(this,introduceList);
        rv_describe.setAdapter(getGoodsIntroduceAdapter);
        getGoodsIntroduceAdapter.notifyDataSetChanged();
        rv_regular.setLayoutManager(new GridLayoutManager(this,1));
        rv_regular.setNestedScrollingEnabled(false);
        getGoodsRulersAdapter = new GetGoodsRulersAdapter(this,rulesList);
        rv_regular.setAdapter(getGoodsRulersAdapter);
        getGoodsRulersAdapter.notifyDataSetChanged();
        rv_picture.setLayoutManager(new GridLayoutManager(this,1));
        rv_picture.setNestedScrollingEnabled(false);
        getGoodsPicturesAdapter = new GetGoodsPicturesAdapter(this,advList);
        rv_picture.setAdapter(getGoodsPicturesAdapter);
        getGoodsPicturesAdapter.notifyDataSetChanged();
    }




    private void httpGetCoinById(){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                getCoinById+"?cId="+cId  ,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject =new JSONObject(response);
                            ListActivityBean listActivityBean=new ListActivityBean();


                            String cIntro=jsonObject.getString("cIntro");
                            String cImgs=jsonObject.getString("cImgs");
                            String cName=jsonObject.getString("cName");

//                            String cNowPrice=jsonObject.getString("cNowPrice");
//                            String cFormerPrice=jsonObject.getString("cFormerPrice");
                            String cSoldOut=jsonObject.getString("cSoldOut");
                            String cDirection=jsonObject.getString("cDirection");
                            attributeStr=jsonObject.getString("attribute");

                            if(cImgs.equals("null")){

                            }else {
                                String[] str=cImgs.split("<-->");
                                for(int j=0;j<str.length;j++){
                                    advList.add(j,str[j]);
                                }
                            }
                            if(cIntro.equals("null")){

                            }else {
                                String[] str=cIntro.split("<-->");
                                for(int j=0;j<str.length;j++){
                                    introduceList.add(j,str[j]);
                                }
                            }
                            //使用规则
                            if (cDirection.equals("null")){

                            }else {
                                String[] rulesstr=cDirection.split("<-->");
                                for(int j=0;j<rulesstr.length;j++){
                                    rulesList.add(j,rulesstr[j]);
                                }
                            }

                            getGoodsIntroduceAdapter.notifyDataSetChanged();
                            getGoodsRulersAdapter.notifyDataSetChanged();
                            getGoodsPicturesAdapter.notifyDataSetChanged();
                            ListActivityBean.Goods_shopping_mall goods_shopping_mall=listActivityBean.new Goods_shopping_mall();

//                            goods_shopping_mall.setcNowPrice(cNowPrice);
//                            goods_shopping_mall.setcFormerPrice(cFormerPrice);
                            goods_shopping_mall.setcName(cName);
                            goods_shopping_mall.setcSoldOut(cSoldOut);



                            goods_shopping_malls_list.add(goods_shopping_mall);


                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        } catch (Exception e) {
                            LondingDialog.dismiss();
                            Toast.makeText(PersonalHomegetGoodsByIdActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LondingDialog.dismiss();
                Toast.makeText(PersonalHomegetGoodsByIdActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
    private void getCoinAttribute(){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                getCoinAttribute+"?coinAttId="+cId ,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        LondingDialog.dismiss();
                        try {
                            JSONArray jsonArray =new JSONArray(response);
                            ListActivityBean listActivityBean=new ListActivityBean();

                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject object =jsonArray.getJSONObject(i);
                                String attributeId=object.getString("attributeId");
                                String specification=object.getString("specification");
                                Double attributePrice=object.getDouble("attributePrice");
                                Long inventory=object.getLong("inventory");
                                String attributeImg=object.getString("attributeImg");
                                String productId=object.getString("productId");
                                ListActivityBean.Attribute attribute=listActivityBean.new Attribute();

                                attribute.setAttributeId(attributeId);
                                attribute.setAttributeImg(attributeImg);
                                attribute.setSpecification(specification);
                                attribute.setAttributePrice(attributePrice);
                                attribute.setInventory(inventory);
                                attribute.setProductId(productId);
                                attribute.setAttribute(attributeStr);
                                attribute.setcName(goods_shopping_malls_list.get(0).getcName());
                                attributeList.add(attribute);
                            }
                            Message message = new Message();
                            message.what = 2;
                            handler.sendMessage(message);








                        } catch (Exception e) {
                            LondingDialog.dismiss();
                            Toast.makeText(PersonalHomegetGoodsByIdActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LondingDialog.dismiss();
                Toast.makeText(PersonalHomegetGoodsByIdActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
}
