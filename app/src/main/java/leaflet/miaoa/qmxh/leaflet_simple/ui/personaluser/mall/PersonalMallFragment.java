package leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mall;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.QMUITabSegment;

import java.util.ArrayList;
import java.util.List;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.base.BaseFragment;
import leaflet.miaoa.qmxh.leaflet_simple.bean.HomePageAdvDataBean;
import leaflet.miaoa.qmxh.leaflet_simple.bean.ListActivityBean;
import leaflet.miaoa.qmxh.leaflet_simple.ui.adapter.ViewPagerAdapter;
import leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.home.PersonalHomeReceivedFragment;


/**
 * 个人用户主页 页面
 */
public class PersonalMallFragment extends BaseFragment implements View.OnClickListener{
    View view;
    Context mContext;


    private List<HomePageAdvDataBean> getAdvList = new ArrayList<HomePageAdvDataBean>();
    private QMUITabSegment tabSegment ;
    private ViewPager vp_personal_mall_viewpager;
    private ViewPagerAdapter mPagerAdapter;
    private List<Fragment> listFragment = new ArrayList<>();
    private ContentPage mDestPage = ContentPage.Item1;
    private List<ListActivityBean.Head_adv> head_advList = new ArrayList<ListActivityBean.Head_adv>();
    List<String > imgs=new ArrayList<String >();
    public PullToRefreshScrollView mall_scrollView;
    public static PersonalHomeReceivedFragment personalHomeAdvDetailActivity;

    Handler handler=new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(getActivity(),"已无更多数据",Toast.LENGTH_SHORT).show();
                    break;
//                case 2:
//                    hAdapter = new HomeAdapter(mContext,productList);
//                    mWrapRecyclerView.setAdapter(hAdapter);
//                    hAdapter.notifyDataSetChanged();
//
//                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected View setContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal_mall,null);
        mContext = this.getContext();

        return view;
    }
    /**
     * 初始化View
     */
    @Override
    protected void setFindViewById(View view) {


        tabSegment = (QMUITabSegment) view.findViewById(R.id.tabSegment);
        vp_personal_mall_viewpager = (ViewPager) view.findViewById(R.id.vp_personal_mall_viewpager);

    }

    @Override
    protected void setListener() {
//            near_discount.setOnClickListener(this);
//            online_shop.setOnClickListener(this);
    }

    @Override
    protected void setControl() {
        initTabAndPager();




    }
    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

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

        listFragment.add(new PersonalMallPreferred_commodityFragment());

//        listFragment.add(new PersonalMallTaobaoFragment());
        mPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),listFragment);
        vp_personal_mall_viewpager.setAdapter(mPagerAdapter);
        vp_personal_mall_viewpager.setCurrentItem(mDestPage.getPosition(), false);


        tabSegment.setHasIndicator(true);
        tabSegment.setIndicatorPosition(false);
        tabSegment.setIndicatorWidthAdjustContent(true);
        QMUITabSegment.Tab title1=new QMUITabSegment.Tab(getString(R.string.tabSegment_item_3_title));
//        QMUITabSegment.Tab title2=new QMUITabSegment.Tab(getString(R.string.tabSegment_item_4_title));
        title1.setTextColor(QMUIResHelper.getAttrColor(getContext(), R.attr.qmui_config_color_gray_1), QMUIResHelper.getAttrColor(getContext(), R.attr.qmui_config_color_red));
//        title2.setTextColor(QMUIResHelper.getAttrColor(getContext(), R.attr.qmui_config_color_gray_1), QMUIResHelper.getAttrColor(getContext(), R.attr.qmui_config_color_red));
        tabSegment.addTab(title1);
//        tabSegment.addTab(title2);


        tabSegment.setupWithViewPager(vp_personal_mall_viewpager, false);
        tabSegment.setMode(QMUITabSegment.MODE_FIXED);
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


    }



    public enum ContentPage {
//        Item1(0),
//        Item2(1);
            Item1(0);
        public static final int SIZE = 2;
        private final int position;

        ContentPage(int pos) {
            position = pos;
        }

        public static ContentPage getPage(int position) {
            switch (position) {
                case 0:
                    return Item1;
//                case 1:
//                    return Item2;
                default:
                    return Item1;
            }
        }

        public int getPosition() {
//            if(position==0){
//                personalHomeAdvDetailActivity=new PersonalHomeReceivedFragment();
//            }
            return position;
        }
    }




}
