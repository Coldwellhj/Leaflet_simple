package leaflet.miaoa.qmxh.leaflet_simple.ui.merchantHomePage.home;



import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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
import leaflet.miaoa.qmxh.leaflet_simple.bean.ListActivityBean;
import leaflet.miaoa.qmxh.leaflet_simple.ui.adapter.SellerHomeFinishedAdapter;
import leaflet.miaoa.qmxh.leaflet_simple.ui.merchantHomePage.publish.RePublishscopeActivity;
import leaflet.miaoa.qmxh.leaflet_simple.utils.Common;
import leaflet.miaoa.qmxh.leaflet_simple.utils.ToastUtils;

import static leaflet.miaoa.qmxh.leaflet_simple.Login.WelcomeActivity.Usertel;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.getQueryPagedAdverWu;


/**
 * 个人用户主页 页面
 */

public class SellerHomeFinishedFragment extends Fragment {
    View view;
    static Context mContext;
    public  SellerHomeFinishedAdapter sellerHomeFinishedAdapter;

    public  List<ListActivityBean.Adv> advList = new ArrayList<ListActivityBean.Adv>();
    public  int totalPage;

    public  int page =1;
    private PullToRefreshListView lv_main;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(getActivity(), "广告到底了", Toast.LENGTH_SHORT).show();
                    break;

            }
            super.handleMessage(msg);
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_seller_home_show, null, false);
        mContext=getActivity();
        advList.clear();
        getQueryPagedAdverWu(1);
        initView(view);
        init();
        return view;
    }


    private void initView(View view) {
        lv_main = (PullToRefreshListView)view.findViewById(R.id.lv_main);

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
                new LoadDataAsyncTaskDown(SellerHomeFinishedFragment.this).execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                new LoadDataAsyncTaskUp(SellerHomeFinishedFragment.this).execute();
            }
        });
        sellerHomeFinishedAdapter = new SellerHomeFinishedAdapter(getActivity(), advList);

        lv_main.setAdapter(sellerHomeFinishedAdapter);
        sellerHomeFinishedAdapter.setOnrepublishClickListener(new SellerHomeFinishedAdapter.OnrepublishClickListener() {
            @Override
            public void onrepublishClick(View view, int position) {
                Intent intent=new Intent(getActivity(),RePublishscopeActivity.class);
                intent.putExtra("content",advList.get(position).getaContent());
                intent.putExtra("aType",advList.get(position).getaType());
                intent.putExtra("aCover",advList.get(position).getaCover());
                intent.putExtra("aMatter",advList.get(position).getaMatter());
                intent.putExtra("videoTime",advList.get(position).getTimeCount()+"");
                intent.putExtra("aAdress",advList.get(position).getaAdress());
                intent.putExtra("coveRage",advList.get(position).getCoveRage());
                intent.putExtra("aPrice",advList.get(position).getaPrice());
                intent.putExtra("aSum",advList.get(position).getaSum());
                intent.putExtra("sumMoney",advList.get(position).getSumMoney());

                intent.putStringArrayListExtra("imagePathList", (ArrayList<String>) advList.get(position).getImagelist());
                startActivity(intent);
            }
        });
        sellerHomeFinishedAdapter.notifyDataSetChanged();

//



        lv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if("true".equals(advList.get(position-1).getaType())){
                    Intent intent=new Intent(getActivity(),SellerHomeAdvDetailActivity.class);
                    intent.putExtra("aId",advList.get(position-1).getaId());

                    intent.putExtra("position",position-1);


                    startActivity(intent);
                }else if("false".equals(advList.get(position-1).getaType())){
                    Intent intent=new Intent(getActivity(),SellerHomeAdvPictureDetailActivity.class);
                    intent.putExtra("aId",advList.get(position-1).getaId());
                    intent.putExtra("position",position-1);

                    startActivity(intent);
                }
            }
        });


    }
    /**
     * 异步下载任务
     */
    private class LoadDataAsyncTaskDown extends AsyncTask<Void, Void, String> {

        private SellerHomeFinishedFragment sellerHomeShowedFragment;

        public LoadDataAsyncTaskDown(SellerHomeFinishedFragment sellerHomeShowedFragment) {
            this.sellerHomeShowedFragment = sellerHomeShowedFragment;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
                advList.clear();
                page=1;
                getQueryPagedAdverWu(1);
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
                sellerHomeShowedFragment.lv_main.onRefreshComplete();//刷新完成

            }
        }
    }

    private class LoadDataAsyncTaskUp extends AsyncTask<Void, Void, String> {

        private SellerHomeFinishedFragment sellerHomeShowedFragment;

        public LoadDataAsyncTaskUp(SellerHomeFinishedFragment sellerHomeShowedFragment) {
            this.sellerHomeShowedFragment = sellerHomeShowedFragment;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
                if(page<totalPage){

                    getQueryPagedAdverWu(++page);

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
                sellerHomeShowedFragment.lv_main.onRefreshComplete();//刷新完成
//                lv_main_foot.setVisibility(View.VISIBLE);

                sellerHomeFinishedAdapter.notifyDataSetChanged();
            }
        }
    }

    public void getQueryPagedAdverWu(int page){
        RequestQueue mQueue = Volley.newRequestQueue(mContext);
        final StringRequest stringRequest = new StringRequest(
                getQueryPagedAdverWu+"?aNum="+Usertel+"&curPage="+page  ,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            if(Common.isNOT_Null(response)){
                                JSONObject jsonObject1 = new JSONObject(response);
                                totalPage=jsonObject1.getInt("totalPage");

                                    String data=jsonObject1.getString("data");
                                    JSONArray jsonArray =new JSONArray(data);
                                    ListActivityBean listActivityBean=new ListActivityBean();
                                    //          int length = jsonArray.length();
                                    List<ListActivityBean.Adv> advList_temp = new ArrayList<ListActivityBean.Adv>();

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String aId=jsonObject.getString("aId");
                                        String aCover=jsonObject.getString("aCover");
                                        String aContent=jsonObject.getString("aContent");
                                        String aMatter=jsonObject.getString("aMatter");
                                        String aType=jsonObject.getString("aType");
                                        String watchCount=jsonObject.getString("watchCount");
                                        String aAdress=jsonObject.getString("aAdress");
                                        String coveRage=jsonObject.getString("coveRage");
                                        String sumMoney=jsonObject.getString("sumMoney");
                                        String aPrice=jsonObject.getString("aPrice");
                                        String aSum=jsonObject.getString("aSum");
                                        String residueMoney=jsonObject.getString("residueMoney");
                                        int timeCount = jsonObject.getInt("timeCount");
                                        Long uploadBegin=jsonObject.getLong("uploadBegin");
                                        Long uploadEnd=jsonObject.getLong("uploadEnd");
                                        List<String > advList_picture=new ArrayList<String>();
                                        if("true".equals(aType)){
                                            advList_picture.add(aCover);
                                            advList_picture.add(aMatter);
                                        }else {
                                            String adv_picture[]=aMatter.split("<-->");

                                            for(int j=0;j<adv_picture.length;j++){
                                                advList_picture.add(j,adv_picture[j]);
                                            }
                                        }

                                        ListActivityBean.Adv adv=listActivityBean.new Adv();
                                        adv.setaId(aId);
                                        adv.setaCover(aCover);
                                        adv.setaMatter(aMatter);
                                        adv.setaPrice(aPrice);
                                        adv.setaAdress(aAdress);
                                        adv.setCoveRage(coveRage);
                                        adv.setaSum(aSum);
                                        adv.setaContent(aContent);
                                        adv.setWatchCount(watchCount);
                                        adv.setSumMoney(sumMoney);
                                        adv.setResidueMoney(residueMoney);
                                        adv.setaType(aType);
                                        adv.setUploadBegin(uploadBegin);
                                        adv.setUploadEnd(uploadEnd);
                                        adv.setTimeCount(timeCount);
                                        adv.setImagelist(advList_picture);
                                        advList_temp.add(i,adv);

                                    }
                                    advList.addAll(advList_temp);
                                    sellerHomeFinishedAdapter.notifyDataSetChanged();


                            }else {
                                ToastUtils.showShort(getActivity(),"");
                            }


                        } catch (Exception e) {

                            Toast.makeText(mContext, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(mContext, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }


}
