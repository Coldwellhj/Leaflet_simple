package leaflet.miaoa.qmxh.leaflet_simple.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.bean.ListActivityBean;
import leaflet.miaoa.qmxh.leaflet_simple.ui.adapter.base.ListBaseAdapter;
import leaflet.miaoa.qmxh.leaflet_simple.ui.adapter.base.SuperViewHolder;
import leaflet.miaoa.qmxh.leaflet_simple.ui.widget.SwipeMenuView;

import static leaflet.miaoa.qmxh.leaflet_simple.utils.DateUtils.getFormatTimeFromNow;
import static leaflet.miaoa.qmxh.leaflet_simple.utils.DateUtils.getTimeFromLong;


public class SwipeMenuAdapter extends ListBaseAdapter<ListActivityBean.SellerNews> {
    Context context;
    public SwipeMenuAdapter(Context context) {
        super(context);
        this.context=context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.seller_news_detail;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        View contentView = holder.getView(R.id.swipe_content);
        ImageView iv_photo = holder.getView(R.id.iv_photo);
        TextView newsTheme = holder.getView(R.id.newsTheme);
        TextView newsDetail = holder.getView(R.id.newsDetail);
        TextView tv_time = holder.getView(R.id.tv_time);
        Button btnDelete = holder.getView(R.id.btnDelete);
        Button btnUnRead = holder.getView(R.id.btnUnRead);
        Button btnRead = holder.getView(R.id.btnRead);
        Button btnTop = holder.getView(R.id.btnTop);

        //这句话关掉IOS阻塞式交互效果 并依次打开左滑右滑
        ((SwipeMenuView)holder.itemView).setIos(false).setLeftSwipe(true);
        if("true".equals(getDataList().get(position).getIsRead())){
            iv_photo.setImageDrawable(context.getResources().getDrawable(R.drawable.message_read));
            btnRead.setVisibility(View.GONE);
            btnUnRead.setVisibility(View.VISIBLE);
        }else if("false".equals(getDataList().get(position).getIsRead())){
            iv_photo.setImageDrawable(context.getResources().getDrawable(R.drawable.message_unread));
            btnUnRead.setVisibility(View.GONE);
            btnRead.setVisibility(View.VISIBLE);
        }
//      title.setText(getDataList().get(position).title + (position % 2 == 0 ? "我只能右滑动" : "我只能左滑动"));
        newsTheme.setText(getDataList().get(position).getNewsTheme());
        newsDetail.setText(getDataList().get(position).getNewsContent());
        tv_time.setText(getFormatTimeFromNow(getTimeFromLong(getDataList().get(position).getNewsTime())));

//        //隐藏控件
        btnTop.setVisibility(View.GONE);

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {

                    mOnSwipeListener.onRead(position);
                }
            }
        });
        btnUnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {

                    mOnSwipeListener.onUnread(position);
                }
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    //((CstSwipeDelMenu) holder.itemView).quickClose();
                    mOnSwipeListener.onDel(position);
                }
            }
        });
        //注意事项，设置item点击，不能对整个holder.itemView设置咯，只能对第一个子View，即原来的content设置，这算是局限性吧。
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AppToast.makeShortToast(mContext, getDataList().get(position).title);

                mOnItemListener.onItem(position);
//                Log.d("TAG", "onClick() called with: v = [" + v + "]");
            }
        });
        //置顶：
        btnTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null!=mOnSwipeListener){
                    mOnSwipeListener.onTop(position);
                }

            }
        });
    }

    /**
     * 和Activity通信的接口
     */
    public interface onSwipeListener {
        void onDel(int pos);
        void onUnread(int pos);
        void onRead(int pos);
        void onTop(int pos);

    }
    public interface onItemListener{
        void onItem(int pos);
    }

    private onSwipeListener mOnSwipeListener;
    private onItemListener mOnItemListener;

    public void setOnDelListener(onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }
    public void setOnItemListener(onItemListener OnItemListener) {
        this.mOnItemListener = OnItemListener;
    }



}

