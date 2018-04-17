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


/**

 */
public class HomeProductAdapter extends RecyclerView.Adapter<HomeProductAdapter.ViewHolder> implements View.OnClickListener{
    private List<ListActivityBean.Product> productList = new ArrayList<ListActivityBean.Product>();
    Context mContext;
    private  OnItemClickListener mOnItemClickListener = null;
    public  interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public HomeProductAdapter(Context mContext, List<ListActivityBean.Product> productList) {
        this.mContext=mContext;
        this.productList = productList;
    }
    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_home_product,viewGroup,false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }
    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Glide.with(mContext)
                .load(productList.get(position).getcCover())
                .into(viewHolder.iv_home_goods);
        viewHolder.position = position;
        viewHolder.tv_name.setText(productList.get(position).getcName());
        String[] str=productList.get(position).getcIntro().split("<-->");
        String introStr="";
        for(int j=0;j<str.length;j++){
            introStr+=str[j]+" ";
        }
        viewHolder.tv_introduce.setText(introStr);
        viewHolder.tv_cNowPrice.setText(productList.get(position).getcNowPrice());
//        viewHolder.tv_cFormerPrice.setText("¥"+productList.get(position).getcFormerPrice());
        viewHolder.tv_cSales.setText("已售"+productList.get(position).getcSales());
        viewHolder.itemView.setTag(position);
    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        return productList.size();
    }
    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_home_goods;
        public TextView tv_name;
        public TextView tv_introduce;
        public TextView tv_cNowPrice;
        public TextView tv_cFormerPrice;
        public TextView tv_cSales;
        int position;
        public ViewHolder(View view){
            super(view);
            iv_home_goods = (ImageView) view.findViewById(R.id.iv_home_goods);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_introduce = (TextView) view.findViewById(R.id.tv_introduce);
            tv_cNowPrice = (TextView) view.findViewById(R.id.tv_cNowPrice);
            tv_cFormerPrice = (TextView) view.findViewById(R.id.tv_cFormerPrice);
            tv_cSales = (TextView) view.findViewById(R.id.tv_cSales);
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
