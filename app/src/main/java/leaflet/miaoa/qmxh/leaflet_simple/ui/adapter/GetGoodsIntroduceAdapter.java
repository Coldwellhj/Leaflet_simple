package leaflet.miaoa.qmxh.leaflet_simple.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import leaflet.miaoa.qmxh.leaflet_simple.R;


/**

 */
public class GetGoodsIntroduceAdapter extends RecyclerView.Adapter<GetGoodsIntroduceAdapter.ViewHolder>{
    private List<String > introduceList = new ArrayList<String>();
    Context mContext;

    public GetGoodsIntroduceAdapter(Context mContext, List<String > introduceList) {
        this.mContext=mContext;
        this.introduceList = introduceList;
    }
    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_goods_introduce,viewGroup,false);
        ViewHolder vh = new ViewHolder(view);

        return vh;
    }
    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        viewHolder.position = position;
        viewHolder.good_introduce.setText(introduceList.get(position));

        viewHolder.itemView.setTag(position);
    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        return introduceList.size();
    }
    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView good_introduce;

        int position;
        public ViewHolder(View view){
            super(view);
            good_introduce = (TextView) view.findViewById(R.id.good_introduce);

        }


    }

}
