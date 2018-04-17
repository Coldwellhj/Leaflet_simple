package leaflet.miaoa.qmxh.leaflet_simple.ui.widget;

/**
 * Created by gaofeng on 2018/1/11.
 */

import android.support.v7.widget.RecyclerView;

/**
 * 最大化的RecyclerView，嵌套于ScrollView之中使用
 */
public class MaxRecyclerView extends RecyclerView {

    public MaxRecyclerView(android.content.Context context, android.util.AttributeSet attrs){
        super(context, attrs);
    }
    public MaxRecyclerView(android.content.Context context){
        super(context);
    }
    /**
     * 设置不滚动
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
