package leaflet.miaoa.qmxh.leaflet_simple.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.bean.ListActivityBean;


/**

 */
public class ManagerAddressAdapter extends RecyclerView.Adapter<ManagerAddressAdapter.ViewHolder> implements View.OnClickListener{
    private List<ListActivityBean.Address> addressesList = new ArrayList<ListActivityBean.Address>();
    Context mContext;
    String addressId;
    private static OnChooseDefaultClickListener mOnChooseDefaultClickListener = null;
    private static OnEditClickListener mOnEditClickListener = null;
    private static OnDeleteClickListener mOnDeleteClickListener = null;
    public  interface OnChooseDefaultClickListener {
        void onChooseDefaultClick(View view, int position);
    }
    public  interface OnEditClickListener {
        void onEditClick(View view, int position);
    }
    public  interface OnDeleteClickListener {
        void onDeleteClick(View view, int position);
    }
    public ManagerAddressAdapter(Context mContext, List<ListActivityBean.Address > addressesList) {
        this.mContext=mContext;
        this.addressesList = addressesList;
    }
    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_address_manager,viewGroup,false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }
    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        viewHolder.position = position;
        try{

            if("true".equals(addressesList.get(position).getDefaultIf())){

                viewHolder.iv_choose_default.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.checked));
                viewHolder.set_default.setText("默认地址");
            }else {
                viewHolder.iv_choose_default.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.unchecked));
                viewHolder.set_default.setText("设为默认");
            }
        }catch (Exception e){

        }
        viewHolder.tv_name_number.setText(addressesList.get(position).getConsigneeName()+"       "+addressesList.get(position).getConsigneeNum());
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
        public ImageView iv_choose_default;
        public TextView set_default;
        public TextView tv_name_number;
        public TextView tv_address;
        public LinearLayout delete_address;
        public LinearLayout edit_address;

        int position;
        public ViewHolder(final View view){
            super(view);
            iv_choose_default = (ImageView) view.findViewById(R.id.iv_choose_default);
            set_default = (TextView) view.findViewById(R.id.set_default);
            tv_name_number = (TextView) view.findViewById(R.id.tv_name_number);
            tv_address = (TextView) view.findViewById(R.id.tv_address);
            delete_address = (LinearLayout) view.findViewById(R.id.delete_address);
            edit_address = (LinearLayout) view.findViewById(R.id.edit_address);
            iv_choose_default.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnChooseDefaultClickListener != null) {
                        //注意这里使用getTag方法获取position
                        try{
                            mOnChooseDefaultClickListener.onChooseDefaultClick(v,(int)view.getTag());
                        }catch (Exception e){
                        }

                    }
                }
            });
            edit_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnEditClickListener != null) {
                        //注意这里使用getTag方法获取position
                        try{
                            mOnEditClickListener.onEditClick(v,(int)view.getTag());
                        }catch (Exception e){
                        }

                    }
                }
            });
            delete_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnDeleteClickListener != null) {
                        //注意这里使用getTag方法获取position
                        try{
                            mOnDeleteClickListener.onDeleteClick(v,(int)view.getTag());
                        }catch (Exception e){
                        }

                    }
                }
            });
        }


    }
    @Override
    public void onClick(View v) {

    }
    public void setOnChooseDefaultClickListener(OnChooseDefaultClickListener listener) {
        this.mOnChooseDefaultClickListener = listener;
    }
    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.mOnDeleteClickListener = listener;
    }
    public void setmOnEditClickListener(OnEditClickListener listener) {
        this.mOnEditClickListener = listener;
    }
}
