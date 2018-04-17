package leaflet.miaoa.qmxh.leaflet_simple.ui.adapter.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.bean.ListActivityBean;
import leaflet.miaoa.qmxh.leaflet_simple.ui.widget.ZQImageViewRoundOval;


/**
 * Created by gaofeng on 2018/1/9.
 */

public class SellerNewsAdapter extends BaseAdapter{
    private List<ListActivityBean.SellerNews> list;
    private Context mContext;
    public SellerNewsAdapter(Context mContext, List<ListActivityBean.SellerNews> list) {
        this.mContext=mContext;
        this.list = list;
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public Object getItem(int position) {

        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {



        //打气筒
        ViewHolder viewHolder = null;

        final ListActivityBean.SellerNews sellerNews = list.get(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = View.inflate(mContext, R.layout.seller_news_detail, null);
            viewHolder.iv_photo = ((ZQImageViewRoundOval) convertView.findViewById(R.id.iv_photo));
            viewHolder.iv_photo .setType(ZQImageViewRoundOval.TYPE_ROUND);
            viewHolder.iv_photo .setRoundRadius(6);//矩形凹行大小
            viewHolder.newsTheme = ((TextView) convertView.findViewById(R.id.newsTheme));
            viewHolder.newsDetail = ((TextView) convertView.findViewById(R.id.newsDetail));

//


            convertView.setTag(viewHolder);//缓存对象
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        try {
            viewHolder.newsTheme.setText(sellerNews.getNewsTheme());
            viewHolder.newsDetail.setText(sellerNews.getNewsContent());
            //图片和时间
//            viewHolder.tv_name.setText(URLDecoder.decode(product.pName + "", "utf-8"));
//
//            Glide.with(getActivity()).load(product.pLogo).into(viewHolder.iv_photo);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return convertView;
    }

    private class ViewHolder {
        ZQImageViewRoundOval iv_photo;
        TextView newsTheme;
        TextView newsDetail;

    }
}
