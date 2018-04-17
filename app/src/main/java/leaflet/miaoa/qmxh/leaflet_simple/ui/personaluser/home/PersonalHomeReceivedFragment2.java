package leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.home;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.bean.ListActivityBean;
import leaflet.miaoa.qmxh.leaflet_simple.ui.adapter.HomeAdvReceivedAdapter;
import leaflet.miaoa.qmxh.leaflet_simple.ui.widget.MaxRecyclerView;

import static leaflet.miaoa.qmxh.leaflet_simple.Login.WelcomeActivity.Usertel;
import static leaflet.miaoa.qmxh.leaflet_simple.Login.WelcomeActivity.start;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.getQueryPagedAdverWacthed;


/**
 * 个人用户主页 页面
 */
public class PersonalHomeReceivedFragment2 extends Fragment {
    View view;
    static Context mContext2;
    private MaxRecyclerView recyclerView_adv;
    public static HomeAdvReceivedAdapter homeAdvReceivedAdapter;

    public static List<ListActivityBean.Adv> advList = new ArrayList<ListActivityBean.Adv>();
    public static int totalPage;
    private String ifRead="";
    private Long aResidue=0L;
    public static int page =1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal_home_receiver2, null, false);
        mContext2=getActivity();
//        main_bottom=getActivity().findViewById(R.id.main_bottom);
        advList.clear();
        getQueryPagedAdverWacthed(1);
        initView(view);

        return view;
    }


    private void initView(View view) {
        recyclerView_adv = (MaxRecyclerView) view.findViewById(R.id.recyclerView_adv);
        recyclerView_adv.setLayoutManager(new GridLayoutManager(getActivity(),1));
        recyclerView_adv.setNestedScrollingEnabled(false);
        homeAdvReceivedAdapter = new HomeAdvReceivedAdapter(getActivity(),advList);
        recyclerView_adv.setAdapter(homeAdvReceivedAdapter);
        homeAdvReceivedAdapter.notifyDataSetChanged();
        homeAdvReceivedAdapter.setOnItemClickListener(new HomeAdvReceivedAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view , int position){

                if("true".equals(advList.get(position).getaType())){
                    Intent intent=new Intent(getActivity(),PersonalHomeAdvDetailActivity.class);
                    intent.putExtra("aId",advList.get(position).getaId());

                    intent.putExtra("position",position);


                    startActivityForResult(intent,1);
                }else if("false".equals(advList.get(position).getaType())){
                    Intent intent=new Intent(getActivity(),PersonalHomeAdvPictureDetailActivity.class);
                    intent.putExtra("aId",advList.get(position).getaId());
                    intent.putExtra("position",position);

                    startActivityForResult(intent,1);
                }

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                Bundle bundle=data.getExtras();
                Boolean isplay=bundle.getBoolean("isplay");
                Integer position=bundle.getInt("position");
                String isread=bundle.getString("isread");
                if(isplay){
                    advList.get(position).setRedpacket(true);
                }else {
                    advList.get(position).setRedpacket(false);
                }
                if("true".equals(isread))
                {
                    advList.get(position).setWatchCount(advList.get(position).getWatchCount()+1);
                }

                homeAdvReceivedAdapter.notifyDataSetChanged();
                break;

            default:
                break;
        }
    }



    public void getQueryPagedAdverWacthed(int page){
        RequestQueue mQueue = Volley.newRequestQueue(mContext2);
        final StringRequest stringRequest = new StringRequest(
                getQueryPagedAdverWacthed+"?start="+start+"&reNum="+Usertel+"&curPage="+page  ,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
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
                                String aType=jsonObject.getString("aType");
                                String watchCount=jsonObject.getString("watchCount");
                                aResidue=jsonObject.getLong("aResidue");
                                ifRead=jsonObject.getString("ifRead");
                                Long uploadBegin=jsonObject.getLong("uploadBegin");
                                Long uploadEnd=jsonObject.getLong("uploadEnd");
                                ListActivityBean.Adv adv=listActivityBean.new Adv();
                                adv.setaId(aId);
                                adv.setaCover(aCover);
                                adv.setaContent(aContent);
                                adv.setWatchCount(watchCount);
                                adv.setaType(aType);
                                adv.setUploadBegin(uploadBegin);
                                adv.setUploadEnd(uploadEnd);
                                if(aResidue>0&&"false".equals(ifRead)){
                                    adv.setRedpacket(true);
                                }else {
                                    adv.setRedpacket(false);
                                }
                                advList_temp.add(i,adv);

                            }
                            advList.addAll(advList_temp);
                            homeAdvReceivedAdapter.notifyDataSetChanged();
                        } catch (Exception e) {

                            Toast.makeText(mContext2, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(mContext2, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }

}
