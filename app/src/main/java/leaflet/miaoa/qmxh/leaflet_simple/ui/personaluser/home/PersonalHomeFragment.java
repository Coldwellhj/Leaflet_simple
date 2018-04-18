package leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.home;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.base.BaseFragment;
import leaflet.miaoa.qmxh.leaflet_simple.bean.HomePageAdvDataBean;
import leaflet.miaoa.qmxh.leaflet_simple.ui.adapter.ViewPagerAdapter;
import leaflet.miaoa.qmxh.leaflet_simple.ui.widget.CustomViewPager;

import static leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.home.PersonalHomeReceivedFragment.imgs;


/**
 * 个人用户主页 页面
 */
public class PersonalHomeFragment extends BaseFragment implements View.OnClickListener{
    View view;
    Context mContext;


    private List<HomePageAdvDataBean> getAdvList = new ArrayList<HomePageAdvDataBean>();
    private QMUITabSegment tabSegment_top;
    private CustomViewPager vp_personal_home_viewpager;
    private ViewPagerAdapter mPagerAdapter;
    private List<Fragment> listFragment = new ArrayList<>();
    private ContentPage mDestPage = ContentPage.Item1;

    public PullToRefreshScrollView main_scrollView;
    public static PersonalHomeReceivedFragment personalHomeReceivedFragment;
    public static PersonalHomeReceivedFragment2 personalHomeReceivedFragment2;
    Banner banner;
    private static int currentPage=0;
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
    protected View setContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal_home,null);
        mContext = this.getContext();

        return view;
    }
    /**
     * 初始化View
     */
    @Override
    protected void setFindViewById(View view) {
        main_scrollView= (PullToRefreshScrollView) view.findViewById(R.id.main_scrollView);
//        banner = (Banner) view.findViewById(R.id.banner);


//        tabSegment = (QMUITabSegment) view.findViewById(R.id.tabSegment);
        tabSegment_top = (QMUITabSegment) view.findViewById(R.id.tabSegment_top);
        vp_personal_home_viewpager = (CustomViewPager)view.findViewById(R.id.vp_personal_home_viewpager);
//        main_scrollView.setV1(tabSegment_top);
//        main_scrollView.setV2(tabSegment);
        //设置可上拉刷新和下拉刷新
        main_scrollView.setMode(PullToRefreshBase.Mode.BOTH);
        //设置刷新时显示的文本
        ILoadingLayout startLayout = main_scrollView.getLoadingLayoutProxy(true, false);
        startLayout.setPullLabel("正在下拉刷新...");
        startLayout.setRefreshingLabel("正在加载中...");
        startLayout.setReleaseLabel("放开以刷新");


        ILoadingLayout endLayout = main_scrollView.getLoadingLayoutProxy(false, true);
        endLayout.setPullLabel("正在上拉刷新...");
        endLayout.setRefreshingLabel("正在加载中...");
        endLayout.setReleaseLabel("放开以刷新");

        main_scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                new LoadDataAsyncTaskDown(PersonalHomeFragment.this).execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                new LoadDataAsyncTaskUp(PersonalHomeFragment.this).execute();
            }
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//
//               new LoadDataAsyncTaskDown(PersonalHomeFragment.this).execute();
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//
//
////                lv_main_foot.setVisibility(View.GONE);
//                new LoadDataAsyncTaskUp(PersonalHomeFragment.this).execute();
//            }
        });
    }

    @Override
    protected void setListener() {
//            near_discount.setOnClickListener(this);
//            online_shop.setOnClickListener(this);
    }

    @Override
    protected void setControl() {
        initTabAndPager();
//        httpAvd();



    }
//    @Override
//    public void onStart() {
//        super.onStart();
//        //开始轮播
//        banner.startAutoPlay();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        //结束轮播
//        banner.stopAutoPlay();
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.near_discount:
//                Intent intent = new Intent(getContext(), NearDiscountActivity.class);
//                getActivity().startActivity(intent);
//                break;
//            case R.id.online_shop:
//                Intent intent2 = new Intent(getContext(), OnlineShopActivity.class);
//                getActivity().startActivity(intent2);
//                break;
        }

    }
    private void initTabAndPager() {

        listFragment.add(new PersonalHomeReceivedFragment());

        listFragment.add(new PersonalHomeReceivedFragment2());
        mPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),listFragment);
        vp_personal_home_viewpager.setAdapter(mPagerAdapter);

        vp_personal_home_viewpager.setCurrentItem(mDestPage.getPosition(), false);

//        tabSegment.setHasIndicator(true);
//        tabSegment.setIndicatorPosition(false);
//        tabSegment.setIndicatorWidthAdjustContent(true);
//        QMUITabSegment.Tab title1=new QMUITabSegment.Tab(getString(R.string.tabSegment_item_1_title));
//        QMUITabSegment.Tab title2=new QMUITabSegment.Tab(getString(R.string.tabSegment_item_2_title));
//        title1.setTextColor(QMUIResHelper.getAttrColor(getContext(), R.attr.qmui_config_color_gray_1), QMUIResHelper.getAttrColor(getContext(), R.attr.qmui_config_color_red));
//        title2.setTextColor(QMUIResHelper.getAttrColor(getContext(), R.attr.qmui_config_color_gray_1), QMUIResHelper.getAttrColor(getContext(), R.attr.qmui_config_color_red));
//        tabSegment.addTab(title1);
//        tabSegment.addTab(title2);
//
//
//        tabSegment.setupWithViewPager(vp_personal_home_viewpager, false);
//        tabSegment.setMode(QMUITabSegment.MODE_FIXED);
//        tabSegment.addOnTabSelectedListener(new QMUITabSegment.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(int index) {
//                tabSegment.hideSignCountView(index);
//            }
//
//            @Override
//            public void onTabUnselected(int index) {
//
//            }
//
//            @Override
//            public void onTabReselected(int index) {
//                tabSegment.hideSignCountView(index);
//            }
//
//            @Override
//            public void onDoubleTap(int index) {
//
//            }
//        });

        tabSegment_top.setHasIndicator(true);
        tabSegment_top.setIndicatorPosition(false);
        tabSegment_top.setIndicatorWidthAdjustContent(true);
        QMUITabSegment.Tab title1_top=new QMUITabSegment.Tab(getString(R.string.tabSegment_item_1_title));
        QMUITabSegment.Tab title2_top=new QMUITabSegment.Tab(getString(R.string.tabSegment_item_2_title));
        title1_top.setTextColor(QMUIResHelper.getAttrColor(getContext(), R.attr.qmui_config_color_gray_1), QMUIResHelper.getAttrColor(getContext(), R.attr.qmui_config_color_red));
        title2_top.setTextColor(QMUIResHelper.getAttrColor(getContext(), R.attr.qmui_config_color_gray_1), QMUIResHelper.getAttrColor(getContext(), R.attr.qmui_config_color_red));
        tabSegment_top.addTab(title1_top);
        tabSegment_top.addTab(title2_top);


        tabSegment_top.setupWithViewPager(vp_personal_home_viewpager, false);
        tabSegment_top.setMode(QMUITabSegment.MODE_FIXED);
        tabSegment_top.addOnTabSelectedListener(new QMUITabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                tabSegment_top.hideSignCountView(index);
                if(index==0){
                    currentPage=index;
                    personalHomeReceivedFragment=new PersonalHomeReceivedFragment();
                }else if(index==1){
                    currentPage=index;
                    personalHomeReceivedFragment2=new PersonalHomeReceivedFragment2();
                }
            }

            @Override
            public void onTabUnselected(int index) {

            }

            @Override
            public void onTabReselected(int index) {
                if(index==0){
                    currentPage=index;
                    personalHomeReceivedFragment=new PersonalHomeReceivedFragment();
                }else if(index==1){
                    currentPage=index;
                    personalHomeReceivedFragment2=new PersonalHomeReceivedFragment2();
                }

                tabSegment_top.hideSignCountView(index);
            }

            @Override
            public void onDoubleTap(int index) {

            }
        });
    }



    public enum ContentPage {
        Item1(0),
        Item2(1);
        public static final int SIZE = 2;
        private final int position;


        ContentPage(int pos) {
            position = pos;
        }

        public static ContentPage getPage(int position) {
            switch (position) {
                case 0:
                    return Item1;
                case 1:
                    return Item2;
                default:
                    return Item1;
            }
        }

        public int getPosition() {
            if(position==0){
                currentPage=position;
                personalHomeReceivedFragment=new PersonalHomeReceivedFragment();
            }else if(position==1){
                currentPage=position;
                personalHomeReceivedFragment2=new PersonalHomeReceivedFragment2();
            }
            return position;
        }
    }


    /**
     * 异步下载任务
     */
    public class LoadDataAsyncTaskDown extends AsyncTask<Void, Void, String> {

        private PersonalHomeFragment personalHomeFragment;

        public LoadDataAsyncTaskDown(PersonalHomeFragment personalHomeFragment) {
            this.personalHomeFragment = personalHomeFragment;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
                if(currentPage==0){
                    personalHomeReceivedFragment.advList.clear();
                    imgs.clear();
                    personalHomeReceivedFragment.page=1;

                    personalHomeReceivedFragment.getQueryBeanAdverUlikeByNum(personalHomeReceivedFragment.page);//下拉刷新第一页数据
                    personalHomeReceivedFragment.httpAvd();//
                }else if(currentPage==1){
                    personalHomeReceivedFragment2.advList.clear();
                    personalHomeReceivedFragment2.page=1;
                    personalHomeReceivedFragment2.getQueryPagedAdverWacthed(personalHomeReceivedFragment2.page);//下拉刷新第一页数据

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
                personalHomeFragment.main_scrollView.onRefreshComplete();//刷新完成

            }
        }
    }

    public  class LoadDataAsyncTaskUp extends AsyncTask<Void, Void, String> {

        private PersonalHomeFragment personalHomeFragment;

        public LoadDataAsyncTaskUp(PersonalHomeFragment personalHomeFragment) {
            this.personalHomeFragment = personalHomeFragment;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
                if(currentPage==0){
                    if(personalHomeReceivedFragment.page<personalHomeReceivedFragment.totalPage){
                        personalHomeReceivedFragment.page++;

                        personalHomeReceivedFragment.getQueryPagedAdverUlike(personalHomeReceivedFragment.page);//上拉加载第一页数据

                    }else {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                }else if(currentPage==1){
                    if(personalHomeReceivedFragment2.page<personalHomeReceivedFragment2.totalPage){
                        personalHomeReceivedFragment2.page++;
                        personalHomeReceivedFragment2.getQueryPagedAdverWacthed(personalHomeReceivedFragment2.page);//上拉加载第一页数据

                    }else {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
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
                personalHomeFragment.main_scrollView.onRefreshComplete();//刷新完成
//                lv_main_foot.setVisibility(View.VISIBLE);
//                listView.addFooterView(lv_main_foot_);
                personalHomeReceivedFragment.homeAdvReceivedAdapter.notifyDataSetChanged();
            }
        }
    }



//    private void httpAvd(){
//        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
//        final StringRequest stringRequest = new StringRequest(
//                slideShow  ,
//                new Response.Listener<String>() {
//
//                    @Override
//                    public void onResponse(String response) {
//
//                        try {
//                            JSONArray jsonArray =new JSONArray(response);
//                            ListActivityBean listActivityBean=new ListActivityBean();
//                            //          int length = jsonArray.length();
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                String hpImgs=jsonObject.getString("hpImgs");
//                                String lianjie=jsonObject.getString("lianjie");
//                                ListActivityBean.Head_adv head_adv=listActivityBean.new Head_adv();
//                                head_adv.setHpImgs(hpImgs);
//                                head_adv.setLianjie(lianjie);
//                                head_advList.add(i,head_adv);
//                                imgs.add(i,hpImgs);
//                            }
//
//
//                            //简单使用
//                            banner.setImages(imgs)
//                                    .setImageLoader(new GlideImageLoader())
//                                    .setOnBannerListener(PersonalHomeFragment.this)
//                                    .start();
//                        } catch (Exception e) {
//
//                            Toast.makeText(getActivity(), "数据异常", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Toast.makeText(getActivity(), "请检查网络设置", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        mQueue.add(stringRequest);
//    }
}
