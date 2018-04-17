package leaflet.miaoa.qmxh.leaflet_simple.ui.merchantHomePage.home;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.QMUITabSegment;

import java.util.ArrayList;
import java.util.List;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.base.BaseFragment;
import leaflet.miaoa.qmxh.leaflet_simple.ui.adapter.ViewPagerAdapter;


/**
 * 商家主页 页面
 */
public class SellerHomeFragment extends BaseFragment implements View.OnClickListener {
    View view;
    Context mContext;

    SellerHomeElseFragment sellerHomeElseFragment;

    private QMUITabSegment tabSegment ;
    private ViewPager contentViewPager_seller;
    private ViewPagerAdapter mPagerAdapter;
    private List<Fragment> listFragment = new ArrayList<>();
    private ContentPage mDestPage = ContentPage.Item1;

    @Override
    protected View setContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_seller_home,null);
        mContext = this.getContext();
        return view;
    }
    /**
     * 初始化View
     */
    @Override
    protected void setFindViewById(View view) {
        tabSegment = (QMUITabSegment) view.findViewById(R.id.tabSegment_seller);
        contentViewPager_seller = (ViewPager)view.findViewById(R.id.contentViewPager_seller);
    }

    @Override
    protected void setListener() {

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
        sellerHomeElseFragment=new SellerHomeElseFragment();
        listFragment.add(new SellerHomeShowedFragment());
        listFragment.add(new SellerHomeFinishedFragment());
        listFragment.add(sellerHomeElseFragment);


        mPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),listFragment);
        contentViewPager_seller.setAdapter(mPagerAdapter);

        contentViewPager_seller.setCurrentItem(mDestPage.getPosition(), false);

        tabSegment.setHasIndicator(true);
        tabSegment.setIndicatorPosition(false);
        tabSegment.setIndicatorWidthAdjustContent(true);
        QMUITabSegment.Tab title1=new QMUITabSegment.Tab(getString(R.string.tabSegment_item_5_title));
        QMUITabSegment.Tab title2=new QMUITabSegment.Tab(getString(R.string.tabSegment_item_6_title));
        QMUITabSegment.Tab title3=new QMUITabSegment.Tab(getString(R.string.tabSegment_item_7_title));
        title1.setTextColor(QMUIResHelper.getAttrColor(getContext(), R.attr.qmui_config_color_gray_1), QMUIResHelper.getAttrColor(getContext(), R.attr.qmui_config_color_red));
        title2.setTextColor(QMUIResHelper.getAttrColor(getContext(), R.attr.qmui_config_color_gray_1), QMUIResHelper.getAttrColor(getContext(), R.attr.qmui_config_color_red));
        title3.setTextColor(QMUIResHelper.getAttrColor(getContext(), R.attr.qmui_config_color_gray_1), QMUIResHelper.getAttrColor(getContext(), R.attr.qmui_config_color_red));
        tabSegment.addTab(title1);
        tabSegment.addTab(title2);
        tabSegment.addTab(title3);


        tabSegment.setupWithViewPager(contentViewPager_seller, false);
        tabSegment.setMode(QMUITabSegment.MODE_FIXED);
        tabSegment.addOnTabSelectedListener(new QMUITabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                tabSegment.hideSignCountView(index);
            }

            @Override
            public void onTabUnselected(int index) {

            }

            @Override
            public void onTabReselected(int index) {
                tabSegment.hideSignCountView(index);
            }

            @Override
            public void onDoubleTap(int index) {

            }
        });


    }



    public enum ContentPage {
        Item1(0),
        Item2(1),
        Item3(2);
        public static final int SIZE = 3;
        private  int position;


        ContentPage(int pos) {
            position = pos;
        }

        public static ContentPage getPage(int position) {
            switch (position) {
                case 0:
                    return Item1;
                case 1:
                    return Item2;
                case 2:
                    return Item3;
                default:
                    return Item1;
            }
        }

        public int getPosition() {
//            if(position==0){
//                currentPage=position;
//                personalHomeReceivedFragment=new PersonalHomeReceivedFragment();
//            }else if(position==1){
//                currentPage=position;
//                personalHomeReceivedFragment2=new PersonalHomeReceivedFragment2();
//            }
            return position;
        }
    }



    public void refreshElse(){
        sellerHomeElseFragment.advList.clear();
        sellerHomeElseFragment.page=1;
        sellerHomeElseFragment.getQueryPagedAdverLing(1);
    }



}
