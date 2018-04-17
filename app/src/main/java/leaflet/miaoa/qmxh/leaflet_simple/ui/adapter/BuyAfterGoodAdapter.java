package leaflet.miaoa.qmxh.leaflet_simple.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.bean.ListActivityBean;


/**

 */

public class BuyAfterGoodAdapter extends BaseAdapter implements View.OnClickListener{
    private List<ListActivityBean.Pay_goods> goodsList = new ArrayList<ListActivityBean.Pay_goods>();
    Context mContext;
    Map<Integer,Boolean> isremind=new HashMap<Integer,Boolean>();

    private  OnItemClickListener mOnItemClickListener = null;
    private static OnifRefundClickListener mOnifRefundClickListener = null;
    private static OnremindClickListener mOnremindClickListener = null;
    private static OnlogisticsClickListener mOnlogisticsClickListener = null;
    private static OnconfirmClickListener mOnconfirmClickListener = null;
    private static OndeleteorderClickListener mDeleteorderClickListener = null;
    public  interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public  interface OnifRefundClickListener {
        void onifRefundClick(View view, int position, String reason);
    }
    public  interface OnremindClickListener {
        void onremindClick(View view, int position);
    }
    public  interface OnlogisticsClickListener {
        void onlogisticsClick(View view, int position);
    }
    public  interface OnconfirmClickListener {
        void onconfirmClick(View view, int position);
    }
    public  interface OndeleteorderClickListener {
        void ondeleteorderClick(View view, int position);
    }
    public BuyAfterGoodAdapter(Context mContext, List<ListActivityBean.Pay_goods> goodsList) {
        this.mContext=mContext;
        this.goodsList = goodsList;

    }



    @Override
    public int getCount() {
        for(int i=0;i<goodsList.size();i++){
            isremind.put(i,false);
        }
        return goodsList.size();
    }

    @Override
    public Object getItem(int position) {

        return goodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {



        //打气筒
        ViewHolder viewHolder = null;


        if (view == null) {
            viewHolder = new ViewHolder();


            view = View.inflate(mContext, R.layout.item_buy_goods, null);
            viewHolder.radiusImageView = (ImageView) view.findViewById(R.id.radiusImageView);
            viewHolder.tv_price = (TextView) view.findViewById(R.id.tv_price);
            viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            viewHolder.tv_type = (TextView) view.findViewById(R.id.tv_type);
            viewHolder.totalmoney = (TextView) view.findViewById(R.id.totalmoney);
            viewHolder.totalNumber = (TextView) view.findViewById(R.id.totalNumber);
            viewHolder.pay_status = (TextView) view.findViewById(R.id.pay_status);
            viewHolder.orderNum = (TextView) view.findViewById(R.id.orderNum);
            viewHolder.remind = (TextView) view.findViewById(R.id.remind);
            viewHolder.logistics = (TextView) view.findViewById(R.id.logistics);
            viewHolder.confirm = (TextView) view.findViewById(R.id.confirm);
            viewHolder.delete_order = (TextView) view.findViewById(R.id.delete_order);
            viewHolder.ifRefund = (TextView) view.findViewById(R.id.ifRefund);
            viewHolder.rl_orderNum = (RelativeLayout) view.findViewById(R.id.rl_orderNum);




//


            view.setTag(viewHolder);//缓存对象
    } else {
        viewHolder = (ViewHolder) view.getTag();
    }
        final ViewHolder finalViewHolder1 = viewHolder;
        class MyTask extends TimerTask{

            @Override
            public void run() {
                Message message=new Message();
                message.what=1;
                finalViewHolder1.handler.sendMessage(message);

            }
        }
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.ifRefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnifRefundClickListener != null) {
                    //注意这里使用getTag方法获取position
                    try{
                        showMenuDialog(finalViewHolder.ifRefund,finalViewHolder.confirm,finalViewHolder.remind,mOnifRefundClickListener,v,position);


                    }catch (Exception e){
                    }

                }
            }
        });
        viewHolder.remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnremindClickListener !=null){
                    try{
                        isremind.put(position,true);
                        finalViewHolder.remind.setText("已催单");
                        finalViewHolder.remind.setClickable(false);
                        //创建定时器对象
                        Timer t=new Timer();
                        //在一小时后执行MyTask类中的run方法
                        t.schedule(new MyTask(), 3600000);
                        mOnremindClickListener.onremindClick(v,position);
                    }catch (Exception e){

                    }
                }
            }
        });
        viewHolder.logistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnlogisticsClickListener !=null){
                    try{

                        mOnlogisticsClickListener.onlogisticsClick(v,position);
                    }catch (Exception e){

                    }
                }
            }
        });
        viewHolder.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnconfirmClickListener !=null){
                    try{
                        finalViewHolder.confirm.setText("已收货");
                        finalViewHolder.confirm.setClickable(false);
                        mOnconfirmClickListener.onconfirmClick(v,position);
                        finalViewHolder.ifRefund.setVisibility(View.GONE);
                        finalViewHolder.delete_order.setVisibility(View.VISIBLE);
                    }catch (Exception e){

                    }
                }
            }
        });
        viewHolder.delete_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDeleteorderClickListener !=null){
                    try{

                        mDeleteorderClickListener.ondeleteorderClick(v,position);

                    }catch (Exception e){

                    }
                }
            }
        });


        try {
            Glide.with(mContext)
                .load(goodsList.get(position).getAttributeImg())
                .into(viewHolder.radiusImageView);
        viewHolder.position = position;
        viewHolder.tv_name.setText(goodsList.get(position).getcName());
        viewHolder.tv_type.setText(goodsList.get(position).getAttribute()+":"+goodsList.get(position).getSpecification());
        viewHolder.tv_price.setText("¥"+goodsList.get(position).getAttributePrice());
        viewHolder.totalmoney.setText("合计：¥"+goodsList.get(position).getPayMoney()+("(含运费¥0.00)"));
        viewHolder.totalNumber.setText("共"+goodsList.get(position).getBuyCount()+"件商品");

        viewHolder.orderNum.setText("中通快递    单号 "+goodsList.get(position).getExpressNumber());
        if(goodsList.get(position).getShippingStatus()==0){
            viewHolder.rl_orderNum.setVisibility(View.GONE);
            viewHolder.ifRefund.setVisibility(View.VISIBLE);
            viewHolder.pay_status.setVisibility(View.VISIBLE);
            viewHolder.remind.setVisibility(View.VISIBLE);
            viewHolder.logistics.setVisibility(View.GONE);
            viewHolder.confirm.setVisibility(View.GONE);
            viewHolder.pay_status.setText("买家已付款");
            if(isremind.get(position)){
                viewHolder.remind.setText("已催单");
                viewHolder.remind.setClickable(false);
            }else {
                viewHolder.remind.setText("我要催单");
                viewHolder.remind.setClickable(true);
            }


        }else  if(goodsList.get(position).getShippingStatus()==1){
            viewHolder.rl_orderNum.setVisibility(View.VISIBLE);
            viewHolder.ifRefund.setVisibility(View.VISIBLE);
            viewHolder.pay_status.setVisibility(View.VISIBLE);
            viewHolder.remind.setVisibility(View.GONE);
            viewHolder.logistics.setVisibility(View.VISIBLE);
            viewHolder.confirm.setVisibility(View.VISIBLE);
            viewHolder.delete_order.setVisibility(View.GONE);
            viewHolder.pay_status.setText("订单已发货");
            viewHolder.logistics.setText("查看物流");
            viewHolder.confirm.setText("确认收货");
        }else  if(goodsList.get(position).getShippingStatus()==2){
            viewHolder.rl_orderNum.setVisibility(View.VISIBLE);
            viewHolder.ifRefund.setVisibility(View.VISIBLE);
            viewHolder.pay_status.setVisibility(View.VISIBLE);
            viewHolder.remind.setVisibility(View.GONE);
            viewHolder.logistics.setVisibility(View.VISIBLE);
            viewHolder.confirm.setVisibility(View.VISIBLE);
            viewHolder.delete_order.setVisibility(View.GONE);
            viewHolder.pay_status.setText("快递已签收");
            viewHolder.logistics.setText("查看物流");
            viewHolder.confirm.setText("确认收货");
        }else {
            viewHolder.remind.setVisibility(View.GONE);
            viewHolder.logistics.setVisibility(View.VISIBLE);
            viewHolder.confirm.setVisibility(View.VISIBLE);
            viewHolder.delete_order.setVisibility(View.VISIBLE);
            viewHolder.logistics.setText("查看物流");
            viewHolder.confirm.setText("已收货");
            viewHolder.delete_order.setText("删除订单");
            viewHolder.rl_orderNum.setVisibility(View.VISIBLE);
            viewHolder.ifRefund.setVisibility(View.GONE);
            viewHolder.pay_status.setVisibility(View.GONE);
        }
        if(goodsList.get(position).getIfRefund()==0){
            viewHolder.ifRefund.setText("申请退换货");
            viewHolder.ifRefund.setClickable(true);

        }else  if(goodsList.get(position).getIfRefund()==1){
            viewHolder.ifRefund.setText("处理中");
            viewHolder.ifRefund.setClickable(false);
            viewHolder.confirm.setVisibility(View.GONE);
            viewHolder.remind.setVisibility(View.GONE);
            viewHolder.delete_order.setVisibility(View.GONE);

        }else if(goodsList.get(position).getIfRefund()==2){
            viewHolder.ifRefund.setText("处理成功");
            viewHolder.ifRefund.setClickable(false);
            viewHolder.confirm.setVisibility(View.GONE);
            viewHolder.remind.setVisibility(View.GONE);
            viewHolder.delete_order.setVisibility(View.VISIBLE);

        }




//        viewHolder.itemView.setTag(position);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;
    }
    private void showMenuDialog(final TextView tv_ifrefund, final TextView tv_confirm, final TextView tv_remind, final OnifRefundClickListener mOnifRefundClickListener, final View v, final int position) {
        final String[] items = new String[]{"尺寸不合适", "与实物有差", "有损坏", "其他"};
        new QMUIDialog.MenuDialogBuilder(mContext)
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv_ifrefund.setText("处理中");

                        tv_ifrefund.setClickable(false);
                        mOnifRefundClickListener.onifRefundClick(v,position,items[which]);
                        tv_confirm.setVisibility(View.GONE);
                        tv_remind.setVisibility(View.GONE);

                        dialog.dismiss();
                    }
                })
                .show();
    }
    private class ViewHolder {
        public ImageView radiusImageView;
        public TextView tv_price;
        public TextView tv_name;
        public TextView tv_type;
        public TextView totalmoney;
        public TextView totalNumber;
        public TextView pay_status;
        public TextView orderNum;
        public TextView ifRefund;
        public TextView remind;
        public TextView logistics;
        public TextView confirm;
        public TextView delete_order;
        public RelativeLayout rl_orderNum;
        int position;
        Handler handler =new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        remind.setText("提醒发货");
                        remind.setClickable(true);
                        break;

                }
                super.handleMessage(msg);
            }
        };
    }
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    public void setOnifRefundClickListener(OnifRefundClickListener listener) {
        this.mOnifRefundClickListener = listener;
    }
    public void setOnremindClickListener(OnremindClickListener listener) {
        this.mOnremindClickListener = listener;
    }
    public void setOnlogisticsClickListener(OnlogisticsClickListener listener) {
        this.mOnlogisticsClickListener = listener;
    }
    public void setOnconfirmClickListener(OnconfirmClickListener listener) {
        this.mOnconfirmClickListener = listener;
    }
    public void setDeleteOrderClickListener(OndeleteorderClickListener listener) {
        this.mDeleteorderClickListener = listener;
    }
}
//    //创建新View，被LayoutManager所调用
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_buy_goods,viewGroup,false);
//        ViewHolder vh = new ViewHolder(view);
//        view.setOnClickListener(this);
//        return vh;
//    }
//    //将数据与界面进行绑定的操作
//    @Override
//    public void onBindViewHolder(ViewHolder viewHolder, int position) {
//        Glide.with(mContext)
//                .load(goodsList.get(position).getAttributeImg())
//                .into(viewHolder.radiusImageView);
//        viewHolder.position = position;
//        viewHolder.tv_name.setText(goodsList.get(position).getcName());
//        viewHolder.tv_type.setText(goodsList.get(position).getAttribute()+":"+goodsList.get(position).getSpecification());
//        viewHolder.tv_price.setText("¥"+goodsList.get(position).getAttributePrice());
//        viewHolder.totalmoney.setText("合计：¥"+goodsList.get(position).getPayMoney()+("含运费¥0.00"));
//        viewHolder.totalNumber.setText("共"+goodsList.get(position).getBuyCount()+"件商品");
//        viewHolder.orderNum.setText("中通快递    单号 "+goodsList.get(position).getExpressNumber());
//        if(goodsList.get(position).getShippingStatus()==0){
//            viewHolder.remind.setVisibility(View.VISIBLE);
//            viewHolder.logistics.setVisibility(View.GONE);
//            viewHolder.confirm.setVisibility(View.GONE);
//            viewHolder.pay_status.setText("卖家已付款");
//            viewHolder.remind.setText("提醒发货");
//
//        }else  if(goodsList.get(position).getShippingStatus()==1){
//            viewHolder.remind.setVisibility(View.GONE);
//            viewHolder.logistics.setVisibility(View.VISIBLE);
//            viewHolder.confirm.setVisibility(View.GONE);
//            viewHolder.pay_status.setText("订单已发货");
//            viewHolder.logistics.setText("查看物流");
//        }else  if(goodsList.get(position).getShippingStatus()==2){
//            viewHolder.remind.setVisibility(View.GONE);
//            viewHolder.logistics.setVisibility(View.GONE);
//            viewHolder.confirm.setVisibility(View.VISIBLE);
//            viewHolder.pay_status.setText("快递已签收");
//            viewHolder.confirm.setText("确认收货");
//        }
//        if(goodsList.get(position).getIfRefund()==0){
//            viewHolder.ifRefund.setText("申请退款");
//        }else  if(goodsList.get(position).getIfRefund()==1){
//            viewHolder.ifRefund.setText("申请成功");
//            viewHolder.ifRefund.setClickable(false);
//        }else if(goodsList.get(position).getIfRefund()==2){
//            viewHolder.ifRefund.setText("退款成功");
//        }
//
//
//
//
//        viewHolder.itemView.setTag(position);
//    }
//    //获取数据的数量
//    @Override
//    public int getItemCount() {
//        return goodsList.size();
//    }
//    //自定义的ViewHolder，持有每个Item的的所有界面元素
//    public  class ViewHolder extends RecyclerView.ViewHolder {
//        public ImageView radiusImageView;
//        public TextView tv_price;
//        public TextView tv_name;
//        public TextView tv_type;
//        public TextView totalmoney;
//        public TextView totalNumber;
//        public TextView pay_status;
//        public TextView orderNum;
//        public TextView ifRefund;
//        public TextView remind;
//        public TextView logistics;
//        public TextView confirm;
//        int position;
//        class MyTask extends TimerTask{
//
//            @Override
//            public void run() {
//                remind.setText("提醒发货");
//                remind.setClickable(true);
//            }
//        }
//        public ViewHolder(final View view){
//            super(view);
//            radiusImageView = (ImageView) view.findViewById(R.id.radiusImageView);
//            tv_price = (TextView) view.findViewById(R.id.tv_price);
//            tv_name = (TextView) view.findViewById(R.id.tv_name);
//            tv_type = (TextView) view.findViewById(R.id.tv_type);
//            totalmoney = (TextView) view.findViewById(R.id.totalmoney);
//            totalNumber = (TextView) view.findViewById(R.id.totalNumber);
//            pay_status = (TextView) view.findViewById(R.id.pay_status);
//            orderNum = (TextView) view.findViewById(R.id.orderNum);
//            remind = (TextView) view.findViewById(R.id.remind);
//            logistics = (TextView) view.findViewById(R.id.logistics);
//            confirm = (TextView) view.findViewById(R.id.confirm);
//            ifRefund = (TextView) view.findViewById(R.id.ifRefund);
//            ifRefund.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mOnifRefundClickListener != null) {
//                        //注意这里使用getTag方法获取position
//                        try{
//                            ifRefund.setText("申请成功");
//
//                            ifRefund.setClickable(false);
//                            mOnifRefundClickListener.onifRefundClick(v,(int)view.getTag());
//                        }catch (Exception e){
//                        }
//
//                    }
//                }
//            });
//            remind.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(mOnremindClickListener !=null){
//                        try{
//                            remind.setText("已提醒");
//                            remind.setClickable(false);
//                            //创建定时器对象
//                            Timer t=new Timer();
//                            //在一小时后执行MyTask类中的run方法
//                            t.schedule(new MyTask(), 3600000);
//                            mOnremindClickListener.onremindClick(v,(int)view.getTag());
//                        }catch (Exception e){
//
//                        }
//                    }
//                }
//            });
//            logistics.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(mOnlogisticsClickListener !=null){
//                        try{
//
//                            mOnlogisticsClickListener.onlogisticsClick(v,(int)view.getTag());
//                        }catch (Exception e){
//
//                        }
//                    }
//                }
//            });
//            confirm.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(mOnconfirmClickListener !=null){
//                        try{
//                            confirm.setText("已收货");
//                            confirm.setClickable(false);
//                            mOnconfirmClickListener.onconfirmClick(v,(int)view.getTag());
//                        }catch (Exception e){
//
//                        }
//                    }
//                }
//            });
//        }
//
//
//    }
//
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        this.mOnItemClickListener = listener;
//    }
//    public void setOnifRefundClickListener(OnifRefundClickListener listener) {
//        this.mOnifRefundClickListener = listener;
//    }
//    public void setOnremindClickListener(OnremindClickListener listener) {
//        this.mOnremindClickListener = listener;
//    }
//    public void setOnlogisticsClickListener(OnlogisticsClickListener listener) {
//        this.mOnlogisticsClickListener = listener;
//    }
//    public void setOnconfirmClickListener(OnconfirmClickListener listener) {
//        this.mOnconfirmClickListener = listener;
//    }
//
//}
