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
import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.bean.ListActivityBean;
import leaflet.miaoa.qmxh.leaflet_simple.ui.adapter.HomeAdvReceivedAdapter;
import leaflet.miaoa.qmxh.leaflet_simple.ui.widget.GlideImageLoader;
import leaflet.miaoa.qmxh.leaflet_simple.ui.widget.MaxRecyclerView;
import leaflet.miaoa.qmxh.leaflet_simple.utils.Common;
import leaflet.miaoa.qmxh.leaflet_simple.utils.ToastUtils;

import static leaflet.miaoa.qmxh.leaflet_simple.Login.WelcomeActivity.Usertel;
import static leaflet.miaoa.qmxh.leaflet_simple.Login.WelcomeActivity.start;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.getQueryPagedAdverUlike;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.slideShow;


/**
 * 个人用户主页 页面
 */

public class PersonalHomeReceivedFragment extends Fragment implements OnBannerListener {
    View view;
    static Context mContext;
    private MaxRecyclerView recyclerView_adv;
    public static HomeAdvReceivedAdapter homeAdvReceivedAdapter;

    public static List<ListActivityBean.Adv> advList = new ArrayList<ListActivityBean.Adv>();
    private List<ListActivityBean.Head_adv> head_advList = new ArrayList<ListActivityBean.Head_adv>();
    public static List<String > imgs=new ArrayList<String >();
    static Banner banner;
    public static int totalPage;
    private String ifRead="";
    private Long aResidue=0L;
    public static int page =1;
    public  QMUITipDialog LondingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal_home_receiver, null, false);
        mContext=getActivity();
//        main_bottom=getActivity().findViewById(R.id.main_bottom);
        advList.clear();
        head_advList.clear();
        imgs.clear();
        LondingDialog = new QMUITipDialog.Builder(getActivity())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("加载中···")
                .create();

        LondingDialog.show();
        httpAvd();
        getQueryPagedAdverUlike(1);
        initView(view);
        return view;
    }


    private void initView(View view) {
        recyclerView_adv = (MaxRecyclerView) view.findViewById(R.id.recyclerView_adv);
        recyclerView_adv.setLayoutManager(new GridLayoutManager(getActivity(),1));
        recyclerView_adv.setNestedScrollingEnabled(false);//使滑动不卡
        homeAdvReceivedAdapter = new HomeAdvReceivedAdapter(getActivity(),advList);
        recyclerView_adv.setAdapter(homeAdvReceivedAdapter);

        RecyclerViewHeader header = RecyclerViewHeader.fromXml(mContext, R.layout.item_header_adv);
        banner = (Banner) header.findViewById(R.id.banner);
        header.attachTo(recyclerView_adv);
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
                    advList.get(position).setWatchCount(String.valueOf(Integer.parseInt(advList.get(position).getWatchCount())+1));
                }

                homeAdvReceivedAdapter.notifyDataSetChanged();
                break;

            default:
                break;
        }
    }



    public void getQueryPagedAdverUlike(int page){
        RequestQueue mQueue = Volley.newRequestQueue(mContext);
        final StringRequest stringRequest = new StringRequest(
                getQueryPagedAdverUlike+"?start="+start+"&uNum="+Usertel+"&curPage="+page  ,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            if(LondingDialog!=null){
                                LondingDialog.dismiss();
                            }

                         if("0.0,0.0".equals(start)){


                             ToastUtils.showShort(getActivity(),"请手动开启定位权限，获取广告领红包");
                         }else {
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
                             }else {

                             }
                         }



                        } catch (Exception e) {
                            LondingDialog.dismiss();
                            Toast.makeText(mContext, "数据异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LondingDialog.dismiss();
                Toast.makeText(mContext, "请检查网络设置", Toast.LENGTH_SHORT).show();

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }

    public  void httpAvd(){
        RequestQueue mQueue = Volley.newRequestQueue(mContext);
        final StringRequest stringRequest = new StringRequest(
                slideShow  ,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray =new JSONArray(response);
                            ListActivityBean listActivityBean=new ListActivityBean();
                            //          int length = jsonArray.length();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String hpImgs=jsonObject.getString("hpImgs");
                                String lianjie=jsonObject.getString("lianjie");
                                ListActivityBean.Head_adv head_adv=listActivityBean.new Head_adv();
                                head_adv.setHpImgs(hpImgs);
                                head_adv.setLianjie(lianjie);
                                head_advList.add(i,head_adv);
                                imgs.add(i,hpImgs);
                            }


                            //简单使用
                            banner.setImages(imgs)
                                    .setImageLoader(new GlideImageLoader())
                                    .setOnBannerListener(PersonalHomeReceivedFragment.this)
                                    .start();
                        } catch (Exception e) {

                            Toast.makeText(mContext, "数据异常", Toast.LENGTH_SHORT).show();
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

    @Override
    public void OnBannerClick(int position) {
        Intent intent =new Intent(getActivity(),WB_pUrlActivity.class);
        intent.putExtra("pUrl",head_advList.get(position).getLianjie());
        startActivity(intent);
    }
}
