package leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mall.payfor;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.base.BaseOtherActivity;
import leaflet.miaoa.qmxh.leaflet_simple.bean.ListActivityBean;
import leaflet.miaoa.qmxh.leaflet_simple.ui.adapter.BuyAfterGoodAdapter;
import leaflet.miaoa.qmxh.leaflet_simple.utils.ToastUtils;

import static leaflet.miaoa.qmxh.leaflet_simple.Login.WelcomeActivity.Usertel;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.applyForRefund;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.deleteCoinBuyBy;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.getQueryPagedCoinBuy;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.updateShippingStatusUser;

public class BuyAfterActivity extends BaseOtherActivity {


    private BuyAfterGoodAdapter buyAfterGoodsAdapter;
    private List<ListActivityBean.Pay_goods> goodsList = new ArrayList<ListActivityBean.Pay_goods>();
    private int page = 1;
    private int totalPage;
    private PullToRefreshListView lv_main;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(BuyAfterActivity.this, "已无更多商品", Toast.LENGTH_SHORT).show();
                    break;

            }
            super.handleMessage(msg);
        }
    };
    private RelativeLayout iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_after);
        getQueryPagedCoinBuy(1);
        initView();
        init();

    }

    private void initView() {
        lv_main = (PullToRefreshListView)findViewById(R.id.lv_main);

        iv_back = (RelativeLayout) findViewById(R.id.iv_back);
    }

    private void init() {
        //设置可上拉刷新和下拉刷新
        lv_main.setMode(PullToRefreshBase.Mode.BOTH);
        //设置刷新时显示的文本
        ILoadingLayout startLayout = lv_main.getLoadingLayoutProxy(true, false);
        startLayout.setPullLabel("正在下拉刷新...");
        startLayout.setRefreshingLabel("正在玩命加载中...");
        startLayout.setReleaseLabel("放开以刷新");


        ILoadingLayout endLayout = lv_main.getLoadingLayoutProxy(false, true);
        endLayout.setPullLabel("正在上拉刷新...");
        endLayout.setRefreshingLabel("正在玩命加载中...");
        endLayout.setReleaseLabel("放开以刷新");

        lv_main.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                new LoadDataAsyncTaskDown(BuyAfterActivity.this).execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                new LoadDataAsyncTaskUp(BuyAfterActivity.this).execute();
            }
        });
        buyAfterGoodsAdapter = new BuyAfterGoodAdapter(this, goodsList);
        lv_main.setAdapter(buyAfterGoodsAdapter);

        buyAfterGoodsAdapter.notifyDataSetChanged();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//


//        buyAfterGoodsAdapter.setOnItemClickListener(new BuyAfterGoodAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                //商品详细界面
//                ToastUtils.showShort(BuyAfterActivity.this, ",,,,,");
//
//            }
//        });
        lv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ToastUtils.showShort(BuyAfterActivity.this, "订单详情");
                //
                Intent intent =new Intent(BuyAfterActivity.this,OrderDetailActivity.class);
                intent.putExtra("cName",goodsList.get(position-1).getcName());
                intent.putExtra("cId",goodsList.get(position-1).getcId());
                intent.putExtra("buyCount",goodsList.get(position-1).getBuyCount());
                intent.putExtra("attribute",goodsList.get(position-1).getAttribute());
                intent.putExtra("specification",goodsList.get(position-1).getSpecification());
                intent.putExtra("payMoney",goodsList.get(position-1).getPayMoney());
                intent.putExtra("orderId",goodsList.get(position-1).getOrderId());
                intent.putExtra("buyTime",goodsList.get(position-1).getBuyTime());
                intent.putExtra("shippingStatus",goodsList.get(position-1).getShippingStatus());
                intent.putExtra("attributeImg",goodsList.get(position-1).getAttributeImg());
                intent.putExtra("buyId",goodsList.get(position-1).getBuyId());
                intent.putExtra("expressNumber",goodsList.get(position-1).getExpressNumber());
                intent.putExtra("ifRefund",goodsList.get(position-1).getIfRefund());
                startActivityForResult(intent,1);
            }
        });
        buyAfterGoodsAdapter.setOnifRefundClickListener(new BuyAfterGoodAdapter.OnifRefundClickListener() {
            @Override
            public void onifRefundClick(View view, int position, String reason) {
                //申请退款
                applyForRefund(goodsList.get(position).getBuyId(),reason);
                ToastUtils.showShort(BuyAfterActivity.this, "已提交申请");
            }

        });
        buyAfterGoodsAdapter.setOnremindClickListener(new BuyAfterGoodAdapter.OnremindClickListener() {
            @Override
            public void onremindClick(View view, int position) {
                //提醒发货
                ToastUtils.showShort(BuyAfterActivity.this, "已提醒");
            }
        });
        buyAfterGoodsAdapter.setOnlogisticsClickListener(new BuyAfterGoodAdapter.OnlogisticsClickListener() {

            @Override
            public void onlogisticsClick(View view, int position) {
                //物流信息
                Intent intent=new Intent(BuyAfterActivity.this,Logistics_informationActivity.class);
                intent.putExtra("expressNumber",goodsList.get(position).getExpressNumber());
                startActivity(intent);
            }
        });
        buyAfterGoodsAdapter.setOnconfirmClickListener(new BuyAfterGoodAdapter.OnconfirmClickListener() {
            @Override
            public void onconfirmClick(View view, int position) {
                //确认收货
                updateShippingStatusUser(goodsList.get(position).getBuyId());
                ToastUtils.showShort(BuyAfterActivity.this, "收货成功");
            }
        });
        buyAfterGoodsAdapter.setDeleteOrderClickListener(new BuyAfterGoodAdapter.OndeleteorderClickListener() {

            @Override
            public void ondeleteorderClick(View view, int position) {
                deleteCoinBuyBy(goodsList.get(position).getBuyId());
                ToastUtils.showShort(BuyAfterActivity.this, "订单已删除");
            }
        });
        buyAfterGoodsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                goodsList.clear();
                page=1;
                getQueryPagedCoinBuy(1);


                break;

            default:
                break;
        }

    }

    /**
     * 异步下载任务
     */
    private class LoadDataAsyncTaskDown extends AsyncTask<Void, Void, String> {

        private BuyAfterActivity buyAfterActivity;

        public LoadDataAsyncTaskDown(BuyAfterActivity buyAfterActivity) {
            this.buyAfterActivity = buyAfterActivity;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
                goodsList.clear();
                page=1;
                 getQueryPagedCoinBuy(1);
                return "seccess";
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 完成时的方法
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("seccess")) {
                buyAfterActivity.lv_main.onRefreshComplete();//刷新完成

            }
        }
    }

    private class LoadDataAsyncTaskUp extends AsyncTask<Void, Void, String> {

        private BuyAfterActivity buyAfterActivity;

        public LoadDataAsyncTaskUp(BuyAfterActivity buyAfterActivity) {
            this.buyAfterActivity = buyAfterActivity;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
                if(page<totalPage){

                    getQueryPagedCoinBuy(++page);

                }else {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }

                return "seccess";
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 完成时的方法
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("seccess")) {
                buyAfterActivity.lv_main.onRefreshComplete();//刷新完成
//                lv_main_foot.setVisibility(View.VISIBLE);

                buyAfterGoodsAdapter.notifyDataSetChanged();
            }
        }
    }


    private void getQueryPagedCoinBuy(final int page) {

        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                getQueryPagedCoinBuy + "?buyNum=" + Usertel + "&curPage=" + page,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            totalPage = jsonObject1.getInt("totalPage");
                            String data = jsonObject1.getString("data");
                            JSONArray jsonArray = new JSONArray(data);
                            List<ListActivityBean.Pay_goods> goodsList_temp = new ArrayList<ListActivityBean.Pay_goods>();
                            ListActivityBean listActivityBean = new ListActivityBean();
                            //          int length = jsonArray.length();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String cName = jsonObject.getString("cName");
                                String cId = jsonObject.getString("cId");
                                String attribute = jsonObject.getString("attribute");
                                String buyId = jsonObject.getString("buyId");
                                String buyCount = jsonObject.getString("buyCount");
                                Long buyTime = jsonObject.getLong("buyTime");
                                String orderId = jsonObject.getString("orderId");
                                String expressNumber = jsonObject.getString("expressNumber");
                                int shippingStatus = jsonObject.getInt("shippingStatus");
                                int ifRefund = jsonObject.getInt("ifRefund");
                                String specification = jsonObject.getString("specification");
                                String attributePrice = jsonObject.getString("attributePrice");
                                String attributeImg = jsonObject.getString("attributeImg");
                                String payMoney = jsonObject.getString("payMoney");
                                ListActivityBean.Pay_goods pay_goods = listActivityBean.new Pay_goods();
                                pay_goods.setcId(cId);
                                pay_goods.setBuyId(buyId);
                                pay_goods.setAttribute(attribute);
                                pay_goods.setAttributeImg(attributeImg);
                                pay_goods.setAttributePrice(attributePrice);
                                pay_goods.setBuyCount(buyCount);
                                pay_goods.setcName(cName);
                                pay_goods.setPayMoney(payMoney);
                                pay_goods.setIfRefund(ifRefund);
                                pay_goods.setSpecification(specification);
                                pay_goods.setBuyTime(buyTime);
                                pay_goods.setShippingStatus(shippingStatus);
                                pay_goods.setExpressNumber(expressNumber);
                                pay_goods.setOrderId(orderId);
                                goodsList_temp.add(i, pay_goods);

                            }
                            goodsList.addAll(goodsList_temp);

                            buyAfterGoodsAdapter.notifyDataSetChanged();



                        } catch (Exception e) {
                            Toast.makeText(BuyAfterActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BuyAfterActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }

    private void applyForRefund(String buyId,String reason) {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                applyForRefund + "?buyId=" + buyId+"&returnReason="+reason,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {


                        } catch (Exception e) {
                            Toast.makeText(BuyAfterActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BuyAfterActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }

    private void updateShippingStatusUser(String buyId) {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                updateShippingStatusUser + "?buyId=" + buyId,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {


                        } catch (Exception e) {
                            Toast.makeText(BuyAfterActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BuyAfterActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
    private void deleteCoinBuyBy(String buyId) {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(
                deleteCoinBuyBy + "?buyId=" + buyId,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {


                        } catch (Exception e) {
                            Toast.makeText(BuyAfterActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BuyAfterActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
}
