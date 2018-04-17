package leaflet.miaoa.qmxh.leaflet_simple.ui.adapter.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.bean.ListActivityBean;


/**

 */

public class SellerHomeElseAdapter extends BaseAdapter {
    private List<ListActivityBean.Adv> advsList = new ArrayList<ListActivityBean.Adv>();
    Context mContext;

    private static OnrepayClickListener onrepayClickListener = null;

    public SellerHomeElseAdapter(Context mContext, List<ListActivityBean.Adv> advsList) {
        this.mContext=mContext;
        this.advsList = advsList;

    }
    public  interface OnrepayClickListener {
        void onrepayClick(View view, int position);
    }


    @Override
    public int getCount() {

        return advsList.size();
    }

    @Override
    public Object getItem(int position) {

        return advsList.get(position);
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



            view = View.inflate(mContext, R.layout.item_seller_home_elseadv, null);
            viewHolder.iv_cover = (ImageView) view.findViewById(R.id.iv_cover);
            viewHolder.tv_content = (TextView) view.findViewById(R.id.tv_content);
            viewHolder.iv_choose_type = (ImageView) view.findViewById(R.id.iv_choose_type);
            viewHolder.iv_isVideo = (ImageView) view.findViewById(R.id.iv_isVideo);
            viewHolder.repay_adv = (TextView) view.findViewById(R.id.repay_adv);


//


            view.setTag(viewHolder);//缓存对象
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


        viewHolder.repay_adv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onrepayClickListener != null) {
                    onrepayClickListener.onrepayClick(v,position);
                }
            }
        });
        try {
            if("true".equals(advsList.get(position).getaType())){
                viewHolder.iv_isVideo.setVisibility(View.VISIBLE);
            }else {
                viewHolder.iv_isVideo.setVisibility(View.GONE);
            }
            if("false".equals(advsList.get(position).getIfPay())){
                viewHolder.repay_adv.setVisibility(View.VISIBLE);
                viewHolder.iv_choose_type.setVisibility(View.GONE);
            }else {
                viewHolder.repay_adv.setVisibility(View.GONE);
                viewHolder.iv_choose_type.setVisibility(View.VISIBLE);
                if("0".equals(advsList.get(position).getaStatus())){
                    viewHolder.iv_choose_type.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.examine_verify));
                }else if("5".equals(advsList.get(position).getaStatus())){
                    viewHolder.iv_choose_type.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.unpass));
                }else {
                    viewHolder.iv_choose_type.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.unpass));
                }
            }

            Glide.with(mContext)
                    .load(advsList.get(position).getaCover())
                    .into(viewHolder.iv_cover);
            viewHolder.position = position;
            viewHolder.tv_content.setText(advsList.get(position).getaContent());






//        viewHolder.itemView.setTag(position);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;
    }

    private class ViewHolder {
        public ImageView iv_cover;
        public TextView tv_content;
        public TextView repay_adv;
        public ImageView iv_choose_type;
        public ImageView iv_isVideo;

        int position;

    }

    public void setOnrepayClickListener(OnrepayClickListener listener) {
        this.onrepayClickListener = listener;
    }
}

