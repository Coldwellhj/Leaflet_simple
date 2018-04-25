package leaflet.miaoa.qmxh.leaflet_simple.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.bean.ListActivityBean;
import leaflet.miaoa.qmxh.leaflet_simple.ui.adapter.GetGoodAttributeNameAdapter;
import leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mall.payfor.ConfirmOrderActivity;
import leaflet.miaoa.qmxh.leaflet_simple.utils.ToastUtils;

import static leaflet.miaoa.qmxh.leaflet_simple.utils.Common.backgroundAlpha;

public class MyPayPopupWindow extends PopupWindow {

    private static final String TAG = "FinishProjectPopupWindows";

    private View mView;
    PopupWindow popupWindow;
    Activity activity;
    QMUIRadiusImageView radiusImageView;
    private RelativeLayout confirm_pay;
    private TextView goods_tvNum;
    private TextView goods_Num;
    private TextView choose_attribute;
    private TextView attribute_type;
    private MaxRecyclerView rv_describe;
    private AmountView amount_view;
    private TextView totalmoney;
    private ImageView iv_close;
    private TextView price;
    private double unit_price=0;
    private double MaxPrice;
    private double minPrice;
    GetGoodAttributeNameAdapter getGoodAttributeNameAdapter;
    List<ListActivityBean.Attribute> attribute=new ArrayList<ListActivityBean.Attribute>();
    Map<Integer,Boolean> map=new HashMap<Integer, Boolean>();
    Boolean isclicked=false;
    Double totalprice=0.00;//总金额


    public MyPayPopupWindow(Activity context) {
        super(context);
        popupWindow = this;
        activity = context;
//        Log.i(TAG, "FinishProjectPopupWindow 方法已被调用");

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.pay_popupwindow, null);
        radiusImageView = (QMUIRadiusImageView) mView.findViewById(R.id.radiusImageView);
        confirm_pay = (RelativeLayout) mView.findViewById(R.id.confirm_pay);
        goods_tvNum = (TextView) mView.findViewById(R.id.goods_tvNum);
        goods_Num = (TextView) mView.findViewById(R.id.goods_Num);
        totalmoney = (TextView) mView.findViewById(R.id.totalmoney);
        amount_view = (AmountView) mView.findViewById(R.id.amount_view);
        iv_close = (ImageView) mView.findViewById(R.id.iv_close);
        rv_describe = (MaxRecyclerView) mView.findViewById(R.id.rv_describe);
        radiusImageView.setCornerRadius(QMUIDisplayHelper.dp2px(activity, 10));
        radiusImageView.setCircle(false);
        radiusImageView.setOval(false);
        radiusImageView.setTouchSelectModeEnabled(false);
//        // 设置按钮监听
//        btnCancelProject.setOnClickListener(new OnClickListener(){
//            @Override
//            public void onClick(View v) {
////                Log.i(TAG, "取消项目");
//                dismiss();
//            }
//        });
//        btnSaveProject.setOnClickListener(itemsOnClick);
//        btnAbandonProject.setOnClickListener(itemsOnClick);

        backgroundAlpha(0.5f,activity);
        //设置PopupWindow的View
        popupWindow.setContentView(mView);
        //设置PopupWindow弹出窗体的宽
        popupWindow.setWidth(LayoutParams.MATCH_PARENT);
//        //设置PopupWindow弹出窗体的高
        popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
        //设置PopupWindow弹出窗体可点击
        popupWindow.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        popupWindow.setAnimationStyle(R.style.Animation);

        popupWindow.setBackgroundDrawable(null);
        //点击其他地方消失
        //添加pop窗口关闭事件
        popupWindow.setOnDismissListener(new poponDismissListener());
        iv_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closePopupWindow();
            }
        });
    }
    public MyPayPopupWindow(Activity context,  List<ListActivityBean.Attribute> attributeList) {
        super(context);
        popupWindow = this;
        activity = context;
        attribute=attributeList;
//        Log.i(TAG, "FinishProjectPopupWindow 方法已被调用");

        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.pay_popupwindow, null);
        radiusImageView = (QMUIRadiusImageView) mView.findViewById(R.id.radiusImageView);
        confirm_pay = (RelativeLayout) mView.findViewById(R.id.confirm_pay);
        attribute_type = (TextView) mView.findViewById(R.id.attribute_type);
        goods_tvNum = (TextView) mView.findViewById(R.id.goods_tvNum);
        goods_Num = (TextView) mView.findViewById(R.id.goods_Num);
        choose_attribute = (TextView) mView.findViewById(R.id.choose_attribute);
        price = (TextView) mView.findViewById(R.id.price);
        totalmoney = (TextView) mView.findViewById(R.id.totalmoney);
        amount_view = (AmountView) mView.findViewById(R.id.amount_view);
        iv_close = (ImageView) mView.findViewById(R.id.iv_close);
        rv_describe = (MaxRecyclerView) mView.findViewById(R.id.rv_describe);
        rv_describe.setLayoutManager(new GridLayoutManager(context,2));
        rv_describe.setNestedScrollingEnabled(false);


        getGoodAttributeNameAdapter = new GetGoodAttributeNameAdapter(context,attributeList);
        rv_describe.setAdapter(getGoodAttributeNameAdapter);
        getGoodAttributeNameAdapter.notifyDataSetChanged();
        getGoodAttributeNameAdapter.setOnItemClickListener(new GetGoodAttributeNameAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(!map.get(position)){
                    Glide.with(activity)
                            .load(attribute.get(position).getAttributeImg())
                            .into(radiusImageView);
                    unit_price=attribute.get(position).getAttributePrice();
                    price.setText("¥"+unit_price);
                    goods_tvNum.setText("商品编号："+attribute.get(position).getProductId());
                    goods_Num.setText("商品库存："+attribute.get(position).getInventory());
                    resetBackground();
                    rv_describe.getChildAt(position).findViewById(R.id.good_attribute).setBackground(activity.getResources().getDrawable(R.drawable.good_attribute_clicked_bg));
                    amount_view.setGoods_storage(Integer.parseInt(String.valueOf(attribute.get(position).getInventory())));
                    amount_view.setGoods_Number(1);
                    amount_view.setCurrentNumber();
                    totalprice=amount_view.getAmount()*unit_price;
                    totalmoney.setText("¥"+totalprice);
                    map.put(position,true);
                    //显示已选套餐
                    choose_attribute.setText("已选"+attribute.get(position).getSpecification());
                    isclicked=true;
                }else {
                    isclicked=false;
                    init();

                    resetBackground();

                }

            }
        });
        //数量变动
        amount_view.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
               totalprice=amount_view.getAmount()*unit_price;
                totalmoney.setText("¥"+totalprice);
            }
        });
        radiusImageView.setCornerRadius(QMUIDisplayHelper.dp2px(activity, 10));
        radiusImageView.setCircle(false);
        radiusImageView.setOval(false);
        radiusImageView.setTouchSelectModeEnabled(false);

        //确认订单
        confirm_pay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!isclicked){
                   ToastUtils.showShort(activity,"请选择合适的"+attribute.get(0).getAttribute());
               }else {
                   closePopupWindow();
                   int position = 0;
                    for(int i=0;i<map.size();i++){
                        if(map.get(i)){
                            position=i;
                        }
                        
                    }
                   Intent intent=new Intent(activity, ConfirmOrderActivity.class);
                   intent.putExtra("cName",attribute.get(0).getcName());
                   intent.putExtra("attributeId",attribute.get(position).getAttributeId());
                   intent.putExtra("attribute",attribute.get(0).getAttribute());
                   intent.putExtra("specification",attribute.get(position).getSpecification());
                   intent.putExtra("attributeImg",attribute.get(position).getAttributeImg());
                   intent.putExtra("totalprice",totalprice);
                   intent.putExtra("attributePrice",attribute.get(position).getAttributePrice());
                   intent.putExtra("goodNumber",amount_view.getAmount());
                   intent.putExtra("totalNumber",Integer.parseInt(String.valueOf(attribute.get(position).getInventory())));
                   activity.startActivity(intent);
               }

            }
        });

        init();




        backgroundAlpha(0.5f,activity);
        //设置PopupWindow的View
        popupWindow.setContentView(mView);
        //设置PopupWindow弹出窗体的宽
        popupWindow.setWidth(LayoutParams.MATCH_PARENT);
//        //设置PopupWindow弹出窗体的高
        popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
        //设置PopupWindow弹出窗体可点击
        popupWindow.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        popupWindow.setAnimationStyle(R.style.Animation);

        popupWindow.setBackgroundDrawable(null);
        //点击其他地方消失
        //添加pop窗口关闭事件
        popupWindow.setOnDismissListener(new poponDismissListener());
        iv_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closePopupWindow();
            }
        });
    }
    /**
     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     * @author cg
     *
     */
    class poponDismissListener implements OnDismissListener{

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            //Log.v("List_noteTypeActivity:", "我是关闭事件");
            backgroundAlpha(1f,activity);
        }

    }
    /**
     * 关闭窗口
     */
    public  void closePopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
            backgroundAlpha(1f,activity);
        }
    }



    //init 初始化
    public void  init(){
        //显示图片
        Glide.with(activity)
                .load(attribute.get(0).getAttributeImg())
                .into(radiusImageView);
        //类型
        attribute_type.setText(attribute.get(0).getAttribute());
        //显示价格
        MaxPrice=attribute.get(0).getAttributePrice();
        minPrice=attribute.get(0).getAttributePrice();
        for(int i=0;i<attribute.size();i++){
            if(attribute.get(i).getAttributePrice()>MaxPrice){
                MaxPrice=attribute.get(i).getAttributePrice();
            }
            if(attribute.get(i).getAttributePrice()<minPrice){
                minPrice=attribute.get(i).getAttributePrice();
            }
        }
        for(int i=0;i<attribute.size();i++){
            map.put(i,false);
        }
        if(minPrice==MaxPrice){
            price.setText("￥"+minPrice);
        }else {
            price.setText("￥"+minPrice+"-"+MaxPrice);
        }
        choose_attribute.setText("");
    }

    //重置属性空间的背景颜色
    private void resetBackground(){
        for(int i=0;i< attribute.size();i++){
            rv_describe.getChildAt(i).findViewById(R.id.good_attribute).setBackground(activity.getResources().getDrawable(R.drawable.good_attribute_bg));
        }
        for(int i=0;i<attribute.size();i++){
            map.put(i,false);
        }

    }
}
