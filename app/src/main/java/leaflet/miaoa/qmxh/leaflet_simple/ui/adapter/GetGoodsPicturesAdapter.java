package leaflet.miaoa.qmxh.leaflet_simple.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import leaflet.miaoa.qmxh.leaflet_simple.R;


/**

 */
public class GetGoodsPicturesAdapter extends RecyclerView.Adapter<GetGoodsPicturesAdapter.ViewHolder>{
    private List<String > introduceList = new ArrayList<String>();
    Context mContext;

    public GetGoodsPicturesAdapter(Context mContext, List<String > introduceList) {
        this.mContext=mContext;
        this.introduceList = introduceList;
    }
    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_goods_picture,viewGroup,false);
        ViewHolder vh = new ViewHolder(view);

        return vh;
    }
    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        viewHolder.position = position;
        //保持比例的加载框架和图片选择的加载框架冲突
//        Common.loadIntoUseFitWidth(mContext, introduceList.get(position), R.mipmap.erweima, viewHolder.good_picture);
        Glide.with(mContext)
                .load(introduceList.get(position))
                .into(viewHolder.good_picture);
        viewHolder.itemView.setTag(position);
    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        return introduceList.size();
    }
    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView good_picture;

        int position;
        public ViewHolder(View view){
            super(view);
            good_picture = (ImageView) view.findViewById(R.id.good_picture);

        }


    }

}
