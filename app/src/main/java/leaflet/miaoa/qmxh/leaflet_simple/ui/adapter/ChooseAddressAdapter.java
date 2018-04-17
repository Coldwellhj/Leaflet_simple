package leaflet.miaoa.qmxh.leaflet_simple.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.bean.ListActivityBean;


/**

 */
public class ChooseAddressAdapter extends RecyclerView.Adapter<ChooseAddressAdapter.ViewHolder> implements View.OnClickListener{
    private List<ListActivityBean.Address> addressesList = new ArrayList<ListActivityBean.Address>();
    Context mContext;
    String addressId;
    private OnItemClickListener mOnItemClickListener = null;
    private static OnImageViewClickListener mOnImageViewClickListener = null;
    public  interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public  interface OnImageViewClickListener {
        void onImageViewClick(View view, int position);
    }
    public ChooseAddressAdapter(Context mContext, List<ListActivityBean.Address > addressesList, String addressId) {
        this.mContext=mContext;
        this.addressesList = addressesList;
        this.addressId = addressId;
    }
    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_address_choose,viewGroup,false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }
    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        viewHolder.position = position;
        try{
            if(addressesList.get(position).getAddressId().equals(addressId)){
                viewHolder.ischoose.setVisibility(View.VISIBLE);
                viewHolder.tv_name_number.setTextColor(mContext.getResources().getColor(R.color.goods_red));
            }else {
                viewHolder.ischoose.setVisibility(View.GONE);
                viewHolder.tv_name_number.setTextColor(mContext.getResources().getColor(R.color.duck_black));
            }
            if("true".equals(addressesList.get(position).getDefaultIf())){
                viewHolder.tv_name_number.setText(addressesList.get(position).getConsigneeName()+"       "+addressesList.get(position).getConsigneeNum()+"[默认地址]");
            }else {
                viewHolder.tv_name_number.setText(addressesList.get(position).getConsigneeName()+"       "+addressesList.get(position).getConsigneeNum());
            }
        }catch (Exception e){

        }

        viewHolder.tv_address.setText(addressesList.get(position).getConsigneeAddress());
        viewHolder.itemView.setTag(position);
    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        return addressesList.size();
    }
    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ischoose;
        public RelativeLayout rl_edit;
        public TextView tv_name_number;
        public TextView tv_address;

        int position;
        public ViewHolder(final View view){
            super(view);
            ischoose = (ImageView) view.findViewById(R.id.ischoose);
            rl_edit = (RelativeLayout) view.findViewById(R.id.rl_edit);
            tv_name_number = (TextView) view.findViewById(R.id.tv_name_number);
            tv_address = (TextView) view.findViewById(R.id.tv_address);
            rl_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnImageViewClickListener != null) {
                        //注意这里使用getTag方法获取position
                        try{
                            mOnImageViewClickListener.onImageViewClick(v,(int)view.getTag());
                        }catch (Exception e){
                        }

                    }
                }
            });
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
    public void setOnImageViewClickListener(OnImageViewClickListener listener) {
        this.mOnImageViewClickListener = listener;
    }
}
