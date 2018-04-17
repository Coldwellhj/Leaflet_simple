package leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mall;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.scu.miomin.shswiperefresh.core.SHSwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.bean.ListActivityBean;
import leaflet.miaoa.qmxh.leaflet_simple.ui.adapter.HomeProductAdapter;

import static leaflet.miaoa.qmxh.leaflet_simple.Login.WelcomeActivity.Usertel;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.getQueryPagedCoin;


/**
 * 优选商品 页面
 */
public class PersonalMallPreferred_commodityFragment extends Fragment  {
    View view;
    static Context mContext;

    HomeProductAdapter homeProductAdapter;
    private List<ListActivityBean.Product> productList = new ArrayList<ListActivityBean.Product>();

    private RecyclerView recyclerview_mall_pc;
    private SHSwipeRefreshLayout layout_swipe_refresh_mall_pc;
    private int page=1;
    private int totalPage;




    Handler handler=new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(getActivity(),"已无更多数据",Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal_mall_pc_receiver, null, false);
        mContext = getActivity();
//      main_bottom=getActivity().findViewById(R.id.main_bottom);
        initView(view);
        getQueryPagedCoin(1);

        final GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),2);
        recyclerview_mall_pc.setLayoutManager(gridLayoutManager);
        homeProductAdapter = new HomeProductAdapter(getActivity(),productList);
        recyclerview_mall_pc.setAdapter(homeProductAdapter);

        homeProductAdapter.notifyDataSetChanged();








//        layout_swipe_refresh_mall_pc.setOnRefreshListener(this);
//
////        recyclerview_mall_pc.addOnScrollListener(new EndLessOnScrollListener(gridLayoutManager,totalPage) {
////            @Override
////            public void onLoadMore(int currentPage) {
////                getQueryPagedCoin(currentPage);
////            }
////        });
//
//        recyclerview_mall_pc.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener(){
//            @Override
//            public void onLoadMore(){
//                recyclerview_mall_pc.postDelayed(new Runnable(){
//                    @Override
//                    public void run(){
//                        layout_swipe_refresh_mall_pc.setRefreshing(false);
//
//                        if (page<totalPage){
//                            getQueryPagedCoin(++page);
//                        }else {
//                            recyclerview_mall_pc.autoCallback();
//                            Message message=new Message();
//                            message.what=1;
//                            handler.sendMessage(message);
//                        }
//
//
//
//                    }
//                },1000);
//
//            }
//        });
//
        homeProductAdapter.setOnItemClickListener(new HomeProductAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view , int position){
                Intent intent =new Intent(getActivity(),PersonalHomegetGoodsByIdActivity.class);
                intent.putExtra("cId",productList.get(position).getcId());
                startActivity(intent);
            }
        });
        initSwipeRefreshLayout() ;
        return view;
    }
    private void initSwipeRefreshLayout() {


        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View view = inflater.inflate(R.layout.list_foot_loading, null);

        layout_swipe_refresh_mall_pc.setFooterView(view);
        layout_swipe_refresh_mall_pc.setOnRefreshListener(new SHSwipeRefreshLayout.SHSOnRefreshListener() {
            @Override
            public void onRefresh() {
                productList.clear();
                page=1;
                getQueryPagedCoin(1);
                layout_swipe_refresh_mall_pc.finishRefresh();
//                layout_swipe_refresh_mall_pc.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
////                        Toast.makeText(RvActivity.this, "刷新完成", Toast.LENGTH_SHORT).show();
//                    }
//                }, 1600);
            }

            @Override
            public void onLoading() {
                if (page<totalPage){
                    getQueryPagedCoin(++page);
                }else {

                    Message message=new Message();
                    message.what=1;
                    handler.sendMessage(message);
                }
                layout_swipe_refresh_mall_pc.finishLoadmore();
//                layout_swipe_refresh_mall_pc.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
////                        Toast.makeText(RvActivity.this, "加载完成", Toast.LENGTH_SHORT).show();
//                    }
//                }, 1600);
            }

            /**
             * 监听下拉刷新过程中的状态改变
             * @param percent 当前下拉距离的百分比（0-1）
             * @param state 分三种状态{NOT_OVER_TRIGGER_POINT：还未到触发下拉刷新的距离；OVER_TRIGGER_POINT：已经到触发下拉刷新的距离；START：正在下拉刷新}
             */
            @Override
            public void onRefreshPulStateChange(float percent, int state) {
                switch (state) {
                    case SHSwipeRefreshLayout.NOT_OVER_TRIGGER_POINT:
//                        swipeRefreshLayout.setRefreshViewText("下拉刷新");
                        break;
                    case SHSwipeRefreshLayout.OVER_TRIGGER_POINT:
//                        swipeRefreshLayout.setRefreshViewText("松开刷新");
                        break;
                    case SHSwipeRefreshLayout.START:
//                        swipeRefreshLayout.setRefreshViewText("正在刷新");
                        break;
                }
            }

            @Override
            public void onLoadmorePullStateChange(float percent, int state) {
                switch (state) {
                    case SHSwipeRefreshLayout.NOT_OVER_TRIGGER_POINT:
//                        textView.setText("上拉加载");
                        break;
                    case SHSwipeRefreshLayout.OVER_TRIGGER_POINT:
//                        textView.setText("松开加载");
                        break;
                    case SHSwipeRefreshLayout.START:
//                        textView.setText("正在加载...");
                        break;
                }
            }
        });
    }

    private void initView(View view) {
        recyclerview_mall_pc = (RecyclerView) view.findViewById(R.id.recyclerview_mall_pc);
        layout_swipe_refresh_mall_pc = (SHSwipeRefreshLayout) view.findViewById(R.id.layout_swipe_refresh_mall_pc);

    }
//    @Override
//    public void onRefresh() {
//        productList.clear();
//        recyclerview_mall_pc.setLoadingMore(false);
//        recyclerview_mall_pc.setAutoLoadMoreEnable(true);//是否支持自动加载更多
//        recyclerview_mall_pc.autoRefreshCallback();
//        page=1;
//        getQueryPagedCoin(1);
//        //数据重新加载完成后，提示数据发生改变，并且设置现在不在刷新
//        layout_swipe_refresh_mall_pc.setRefreshing(false);
//    }


    private void getQueryPagedCoin(final int page){
        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
        final StringRequest stringRequest = new StringRequest(
                getQueryPagedCoin+"?uNum="+Usertel+"&curPage="+page  ,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            totalPage=jsonObject1.getInt("totalPage");
                            String data=jsonObject1.getString("data");
                            JSONArray jsonArray =new JSONArray(data);
                            List<ListActivityBean.Product> productList_temp = new ArrayList<ListActivityBean.Product>();
                            ListActivityBean listActivityBean=new ListActivityBean();
                            //          int length = jsonArray.length();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String cId=jsonObject.getString("cId");
                                String cName=jsonObject.getString("cName");
                                String cIntro=jsonObject.getString("cIntro");
                                String cNowPrice=jsonObject.getString("cNowPrice");
//                                String cFormerPrice=jsonObject.getString("cFormerPrice");
                                String cCover=jsonObject.getString("cCover");
                                String cSales=jsonObject.getString("cSales");
                                ListActivityBean.Product product=listActivityBean.new Product();
                                product.setcId(cId);
                                product.setcName(cName);
                                product.setcCover(cCover);
//                                product.setcFormerPrice(cFormerPrice);
                                product.setcNowPrice(cNowPrice);
                                product.setcIntro(cIntro);
                                product.setcSales(cSales);
                                productList_temp.add(i,product);

                            }
                            productList.addAll(productList_temp);

                            homeProductAdapter.notifyDataSetChanged();



                        } catch (Exception e) {

                            Toast.makeText(getActivity(), "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
}
