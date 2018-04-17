package leaflet.miaoa.qmxh.leaflet_simple.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.bean.ListActivityBean;


/**

 */
public class GetGoodAttributeNameAdapter extends RecyclerView.Adapter<GetGoodAttributeNameAdapter.ViewHolder> implements View.OnClickListener{
    private List<ListActivityBean.Attribute > Attribute = new ArrayList<ListActivityBean.Attribute>();
    Context mContext;
    private OnItemClickListener mOnItemClickListener = null;


    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public GetGoodAttributeNameAdapter(Context mContext, List<ListActivityBean.Attribute > Attribute) {
        this.mContext=mContext;
        this.Attribute = Attribute;
    }
    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_goods_attribute,viewGroup,false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }
    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        viewHolder.position = position;
        if (Attribute.get(position).getInventory()>0){
            viewHolder.good_attribute.setTextColor(Color.parseColor("#000000"));
            viewHolder.good_attribute.setText(Attribute.get(position).getSpecification());

        }else {
            viewHolder.good_attribute.setTextColor(Color.parseColor("#e75356"));
            viewHolder.good_attribute.setText(Attribute.get(position).getSpecification());
            viewHolder.itemView.setClickable(false);
        }


        viewHolder.itemView.setTag(position);

    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        return Attribute.size();
    }
    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView good_attribute;

        int position;
        public ViewHolder(View view){
            super(view);
            good_attribute = (TextView) view.findViewById(R.id.good_attribute);

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
