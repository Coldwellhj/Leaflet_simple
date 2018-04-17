package leaflet.miaoa.qmxh.leaflet_simple.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.ui.adapter.KeyBoardAdapter;


/**
 * 仿京东密码键盘
 *
 * @author Phoenix
 * @date 2016-10-9 11:06
 */
public class Keyboard extends RelativeLayout {
    private Context context;
    private GridView gvKeyboard;
    private ArrayList<Map<String, String>> valueList;
    private String[] key;
    private OnClickKeyboardListener onClickKeyboardListener;
    private RelativeLayout layoutBack;
    public Keyboard(Context context) {
        this(context, null);
    }

    public Keyboard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Keyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    /**
     * 初始化键盘的点击事件
     */
    private void initEvent() {
        gvKeyboard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onClickKeyboardListener != null && position >= 0) {
                    onClickKeyboardListener.onKeyClick(position, key[position]);
                }
            }
        });
    }

    /**
     * 初始化KeyboardView
     */
    private void initKeyboardView() {
        valueList = new ArrayList<>();
        initValueList();
        View view = View.inflate(context, R.layout.view_keyboard, this);
        gvKeyboard = (GridView) view.findViewById(R.id.gv_keyboard);
        layoutBack = (RelativeLayout) view.findViewById(R.id.layoutBack);
        KeyBoardAdapter keyBoardAdapter = new KeyBoardAdapter(context, valueList);
        gvKeyboard.setAdapter(keyBoardAdapter);
        initEvent();
    }

    public interface OnClickKeyboardListener {
        void onKeyClick(int position, String value);
    }
    private void initValueList() {

        // 初始化按钮上应该显示的数字
        for (int i = 1; i < 13; i++) {
            Map<String, String> map = new HashMap<>();
            if (i < 10) {
                map.put("name", String.valueOf(i));
            } else if (i == 10) {
                map.put("name", " ");
            } else if (i == 11) {
                map.put("name", String.valueOf(0));
            } else if (i == 12) {
                map.put("name", "");
            }
            valueList.add(map);
        }
    }
    /**
     * 对外开放的方法
     *
     * @param onClickKeyboardListener
     */
    public void setOnClickKeyboardListener(OnClickKeyboardListener onClickKeyboardListener) {
        this.onClickKeyboardListener = onClickKeyboardListener;
    }

    /**
     * 设置键盘所显示的内容
     *
     * @param key
     */
    public void setKeyboardKeys(String[] key) {
        this.key = key;
        initKeyboardView();
    }
    public RelativeLayout getLayoutBack() {
        return layoutBack;
    }

}
