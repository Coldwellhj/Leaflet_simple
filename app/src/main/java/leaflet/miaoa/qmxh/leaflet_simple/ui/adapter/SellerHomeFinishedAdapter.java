package leaflet.miaoa.qmxh.leaflet_simple.ui.adapter;

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

import static leaflet.miaoa.qmxh.leaflet_simple.utils.DateUtils.getStr3FromDate;
import static leaflet.miaoa.qmxh.leaflet_simple.utils.DateUtils.getTimeFromLong;


/**

 */

public class SellerHomeFinishedAdapter extends BaseAdapter {
    private List<ListActivityBean.Adv> advsList = new ArrayList<ListActivityBean.Adv>();
    Context mContext;


    private static OnrepublishClickListener onrepublishClickListener = null;

    public SellerHomeFinishedAdapter(Context mContext, List<ListActivityBean.Adv> advsList) {
        this.mContext=mContext;
        this.advsList = advsList;

    }
    public  interface OnrepublishClickListener {
        void onrepublishClick(View view, int position);
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



            view = View.inflate(mContext, R.layout.item_seller_home_finishedadv, null);
            viewHolder.iv_cover = (ImageView) view.findViewById(R.id.iv_cover);
            viewHolder.tv_content = (TextView) view.findViewById(R.id.tv_content);
            viewHolder.tv_money = (TextView) view.findViewById(R.id.tv_money);
            viewHolder.tv_playback = (TextView) view.findViewById(R.id.tv_playback);

            viewHolder.tv_time = (TextView) view.findViewById(R.id.tv_time);
            viewHolder.republish_adv = (TextView) view.findViewById(R.id.republish_adv);
            viewHolder.iv_isVideo = (ImageView) view.findViewById(R.id.iv_isVideo);



//


            view.setTag(viewHolder);//缓存对象
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.republish_adv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onrepublishClickListener != null) {
                    onrepublishClickListener.onrepublishClick(v,position);
                }
            }
        });
        try {
            if("true".equals(advsList.get(position).getaType())){
                viewHolder.iv_isVideo.setVisibility(View.VISIBLE);
            }else {
                viewHolder.iv_isVideo.setVisibility(View.GONE);
            }
            Glide.with(mContext)
                    .load(advsList.get(position).getaCover())
                    .into(viewHolder.iv_cover);
            viewHolder.position = position;
            viewHolder.tv_content.setText(advsList.get(position).getaContent());
            viewHolder.tv_playback.setText(advsList.get(position).getWatchCount());
            viewHolder.tv_money.setText(advsList.get(position).getSumMoney()+"  剩余："+advsList.get(position).getResidueMoney());
            viewHolder.tv_time.setText(getStr3FromDate(getTimeFromLong(advsList.get(position).getUploadBegin()))+"-"+getStr3FromDate(getTimeFromLong(advsList.get(position).getUploadEnd())));






//        viewHolder.itemView.setTag(position);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;
    }

    private class ViewHolder {
        public ImageView iv_cover;
        public TextView tv_content;
        public TextView tv_money;
        public TextView tv_playback;
        public TextView tv_time;
        public TextView republish_adv;
        public ImageView iv_isVideo;

        int position;

    }
    public void setOnrepublishClickListener(OnrepublishClickListener listener) {
        this.onrepublishClickListener = listener;
    }

}

