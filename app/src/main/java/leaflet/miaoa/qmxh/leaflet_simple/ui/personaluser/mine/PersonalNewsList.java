package leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.base.BaseOtherActivity;
import leaflet.miaoa.qmxh.leaflet_simple.bean.ListActivityBean;
import leaflet.miaoa.qmxh.leaflet_simple.ui.adapter.SwipeMenuAdapter;
import leaflet.miaoa.qmxh.leaflet_simple.ui.adapter.base.SellerNewsAdapter;
import leaflet.miaoa.qmxh.leaflet_simple.ui.widget.LRecyclerView;

import static leaflet.miaoa.qmxh.leaflet_simple.Login.WelcomeActivity.Usertel;
import static leaflet.miaoa.qmxh.leaflet_simple.MyApplicition.MyApplication.getContext;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.deleteNewsByNewId;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.getQueryPagedNews;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.updateNewsByNewId;
import static leaflet.miaoa.qmxh.leaflet_simple.bean.Https.updateNewsIfRead;


public class PersonalNewsList extends BaseOtherActivity {
    private PullToRefreshListView lv_main;
    private ListView listView;
    private SellerNewsAdapter sellerNewsAdapter;
    /**
     * 每一页展示多少条数据
     */
    private static int REQUEST_COUNT = 5;
    /**
     * 已经获取到多少条数据了
     */
    private static int mCurrentCounter = 0;
    /**
     * 总页数
     */
    private static int    totalPage ;
    /**
     * 当前页数
     */
    private static int    page =1;

    View view;
    private static List<ListActivityBean.SellerNews> sellerNewsList = new ArrayList<ListActivityBean.SellerNews>();
    private LRecyclerView mRecyclerView = null;
    private RelativeLayout iv_back ;

    private SwipeMenuAdapter mDataAdapter = null;

    private PreviewHandler mHandler = new PreviewHandler(this);
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    private boolean isRefresh = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_personal_news_list);
        initView();
        initNewsData(1);
        sellerNewsList.clear();
        mDataAdapter = new SwipeMenuAdapter(getContext());

//        mDataAdapter.setDataList(dataList);
        mDataAdapter.setOnDelListener(new SwipeMenuAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
//                Toast.makeText(SwipeDeleteActivity.this, "删除:" + pos, Toast.LENGTH_SHORT).show();

                //RecyclerView关于notifyItemRemoved的那点小事 参考：http://blog.csdn.net/jdsjlzx/article/details/52131528

                deleteNewsByNewId(pos,sellerNewsList.get(pos).getNewId());

                //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
            }
            //标记未读
            @Override
            public void onUnread(int pos) {
                updateNewsIfRead(pos,sellerNewsList.get(pos).getNewId());
            }
            //标记已读
            @Override
            public void onRead(int pos) {
                updateNewsByNewId(pos,sellerNewsList.get(pos).getNewId());
            }

            @Override
            public void onTop(int pos) {//置顶功能有bug，后续解决
//                TLog.error("onTop pos = " + pos);
                ListActivityBean.SellerNews sellerNews = mDataAdapter.getDataList().get(pos);

                mDataAdapter.getDataList().remove(pos);
                mDataAdapter.notifyItemRemoved(pos);
                mDataAdapter.getDataList().add(0, sellerNews);
                mDataAdapter.notifyItemInserted(0);


                if(pos != (mDataAdapter.getDataList().size())){ // 如果移除的是最后一个，忽略
                    mDataAdapter.notifyItemRangeChanged(0, mDataAdapter.getDataList().size() - 1,"jdsjlzx");
                }

                mRecyclerView.scrollToPosition(0);

            }


        });
        mDataAdapter.setOnItemListener(new SwipeMenuAdapter.onItemListener() {
            @Override
            public void onItem(int pos) {
//                ToastUtils.showShort(PersonalNewsList.this,"1111111");
                //标记未已读
                updateNewsByNewId(pos,sellerNewsList.get(pos).getNewId());
                Intent intent =new Intent(PersonalNewsList.this, PersonalNewsDetailActivity.class);
                intent.putExtra("news_theme",sellerNewsList.get(pos).getNewsTheme());
                intent.putExtra("news_time",sellerNewsList.get(pos).getNewsTime());
                intent.putExtra("news_content",sellerNewsList.get(pos).getNewsContent());
                startActivity(intent);
            }
        });
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

//        mLRecyclerViewAdapter.addHeaderView(new SampleHeader(this));

        mRecyclerView.addItemDecoration();//设置分割线


        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mDataAdapter.clear();
                sellerNewsList.clear();
                mLRecyclerViewAdapter.notifyDataSetChanged();//fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
                mCurrentCounter = 0;
                isRefresh = true;
                page=1;
                initNewsData(1);
                mRecyclerView.setNoMore(false);
                mRecyclerView.setLoadMoreEnabled(true);
            }
        });

//        mRecyclerView.refresh();
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if(page<totalPage){
                    initNewsData(++page);
                }else {
                    mRecyclerView.setNoMore(true);
                }


            }
        });

    }
    private void initView() {
        mRecyclerView = (LRecyclerView) findViewById(R.id.list);
        iv_back = (RelativeLayout) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void notifyDataSetChanged() {
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(List<ListActivityBean.SellerNews> list) {

        mDataAdapter.addAll(list);
        mCurrentCounter += list.size();

    }



    private static class PreviewHandler extends Handler {

        private WeakReference<PersonalNewsList> ref;

        PreviewHandler(PersonalNewsList personalNewsList) {
            ref = new WeakReference<>(personalNewsList);
        }

        @Override
        public void handleMessage(Message msg) {
            final PersonalNewsList personalNewsList = ref.get();
            if (personalNewsList == null) {
                return;
            }
            switch (msg.what) {

                case -1:
                    if (personalNewsList.isRefresh) {
                        personalNewsList.mDataAdapter.clear();
                        mCurrentCounter = 0;
                    }

                    int currentSize = personalNewsList.mDataAdapter.getItemCount();

//                    //模拟组装10个数据
//                    ArrayList<ItemModel> newList = new ArrayList<>();
//                    for (int i = 0; i < 10; i++) {
//                        if (newList.size() + currentSize >= TOTAL_COUNTER) {
//                            break;
//                        }
//
//                        ItemModel item = new ItemModel();
//                        item.id = currentSize + i;
//                        item.title = "item" + (item.id);
//
//                        newList.add(item);
//                    }


                    personalNewsList.addItems(sellerNewsList);
                    personalNewsList.mRecyclerView.refreshComplete(REQUEST_COUNT);
                    personalNewsList.notifyDataSetChanged();
                    break;
                case -2:
                    personalNewsList.notifyDataSetChanged();
                    break;
                case -3:
                    personalNewsList.mRecyclerView.refreshComplete(REQUEST_COUNT);
                    personalNewsList.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }


    //网络请求
    private void initNewsData(final int page) {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                getQueryPagedNews + "?newNum=" + Usertel+"&curPage="+page,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            ListActivityBean listActivityBean = new ListActivityBean();
                            totalPage = jsonObject.getInt("totalPage");
                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                            for(int j=0;j<jsonArray1.length();j++){
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                String newId = jsonObject1.getString("newId");
                                String newTheme = jsonObject1.getString("newTheme");
                                String unContent = jsonObject1.getString("newContent");
                                String unStatus = jsonObject1.getString("newReadIf");
                                Long unTime = jsonObject1.getLong("newTime");

                                ListActivityBean.SellerNews sellerNews = listActivityBean.new SellerNews();
                                sellerNews.setIsRead(unStatus);
                                sellerNews.setNewId(newId);
//                                  sellerNews.setNewsTheme(mnKinds);
                                sellerNews.setNewsTheme(newTheme);
                                sellerNews.setNewsContent(unContent);
                                sellerNews.setNewsTime(unTime);
                                sellerNewsList.add(j, sellerNews);
                            }
//                                String mnKinds = jsonObject.getString("mnKinds");
//                                Long unTime = jsonObject.getLong("unTime");
//                                String mnStatus = jsonObject.getString("mnStatus");
//                                ListActivityBean.SellerNews sellerNews = listActivityBean.new SellerNews();
//                                sellerNews.setIsRead(mnStatus);
//                                sellerNews.setNewsTheme(mnKinds);
//                                sellerNews.setNewsContent(unContent);
//                                sellerNews.setNewsTime(unTime);

                            REQUEST_COUNT = sellerNewsList.size();

//                                mDataAdapter.notifyDataSetChanged();
                            mHandler.sendEmptyMessage(-1);





                        } catch (Exception e) {
                            Toast.makeText(PersonalNewsList.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PersonalNewsList.this, "网络加载失败", Toast.LENGTH_SHORT).show();
                mHandler.sendEmptyMessage(-3);
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
    //删除
    private void deleteNewsByNewId(final int pos,String newId) {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                deleteNewsByNewId + "?newId=" +newId ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {


                                if("true".equals(response)){
                                    sellerNewsList.remove(pos);
                                    mDataAdapter.getDataList().remove(pos);
                                    mDataAdapter.notifyItemRemoved(pos);//推荐用这个

                                    if(pos != (mDataAdapter.getDataList().size())){ // 如果移除的是最后一个，忽略 注意：这里的mDataAdapter.getDataList()不需要-1，因为上面已经-1了
                                        mDataAdapter.notifyItemRangeChanged(pos, mDataAdapter.getDataList().size() - pos);
                                    }

                            }






                        } catch (Exception e) {
                            Toast.makeText(PersonalNewsList.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PersonalNewsList.this, "网络加载失败", Toast.LENGTH_SHORT).show();
                mHandler.sendEmptyMessage(-3);
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
    //标记已读
    private void updateNewsByNewId(final int pos,String newId) {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                updateNewsByNewId + "?newId=" +newId ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {


                            if("true".equals(response)){
                                mDataAdapter.getDataList().get(pos).setIsRead("true");
                                mDataAdapter.notifyDataSetChanged();//推荐用这个


                            }






                        } catch (Exception e) {
                            Toast.makeText(PersonalNewsList.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PersonalNewsList.this, "网络加载失败", Toast.LENGTH_SHORT).show();
                mHandler.sendEmptyMessage(-3);
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
    //标记已读
    private void updateNewsIfRead(final int pos,String newId) {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                updateNewsIfRead + "?newId=" +newId ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {


                            if("true".equals(response)){
                                mDataAdapter.getDataList().get(pos).setIsRead("false");
                                mDataAdapter.notifyDataSetChanged();//推荐用这个
                            }






                        } catch (Exception e) {
                            Toast.makeText(PersonalNewsList.this, "数据异常", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PersonalNewsList.this, "网络加载失败", Toast.LENGTH_SHORT).show();
                mHandler.sendEmptyMessage(-3);
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000,// 默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,// 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(stringRequest);
    }
}
