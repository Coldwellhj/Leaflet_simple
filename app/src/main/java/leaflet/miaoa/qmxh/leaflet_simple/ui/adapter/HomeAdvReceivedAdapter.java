package leaflet.miaoa.qmxh.leaflet_simple.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class HomeAdvReceivedAdapter extends RecyclerView.Adapter<HomeAdvReceivedAdapter.ViewHolder> implements View.OnClickListener{
    private List<ListActivityBean.Adv> advList = new ArrayList<ListActivityBean.Adv>();
    Context mContext;

    private  OnItemClickListener mOnItemClickListener = null;
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public HomeAdvReceivedAdapter(Context mContext, List<ListActivityBean.Adv> advList) {
        this.mContext=mContext;
        this.advList = advList;

    }
    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_home_receivedadv,viewGroup,false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }
    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if(advList.get(position).isRedpacket()){
            viewHolder.iv_red_packet.setVisibility(View.VISIBLE);
        }else {
            viewHolder.iv_red_packet.setVisibility(View.GONE);
        }
        if("true".equals(advList.get(position).getaType())){
            viewHolder.iv_isVideo.setVisibility(View.VISIBLE);
        }else {
            viewHolder.iv_isVideo.setVisibility(View.GONE);
        }
        Glide.with(mContext)
                .load(advList.get(position).getaCover())
                .into(viewHolder.iv_cover);
        viewHolder.position = position;
        viewHolder.tv_content.setText(advList.get(position).getaContent());
        viewHolder.tv_playback.setText(advList.get(position).getWatchCount());
        viewHolder.tv_time.setText(getStr3FromDate(getTimeFromLong(advList.get(position).getUploadBegin()))+"-"+getStr3FromDate(getTimeFromLong(advList.get(position).getUploadEnd())));
        viewHolder.itemView.setTag(position);
    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        return advList.size();
    }
    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_cover;
        public TextView tv_content;
        public TextView tv_playback;
        public TextView tv_time;
        public ImageView iv_red_packet;
        public ImageView iv_isVideo;

        int position;
        public ViewHolder(View view){
            super(view);
            iv_cover = (ImageView) view.findViewById(R.id.iv_cover);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            tv_playback = (TextView) view.findViewById(R.id.tv_playback);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            iv_red_packet = (ImageView) view.findViewById(R.id.iv_red_packet);
            iv_isVideo = (ImageView) view.findViewById(R.id.iv_isVideo);

        }


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
}
