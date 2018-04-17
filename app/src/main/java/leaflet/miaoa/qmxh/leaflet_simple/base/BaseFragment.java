package leaflet.miaoa.qmxh.leaflet_simple.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/9/11.
 */

public abstract class BaseFragment extends Fragment {

    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView != null){
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null){
                parent.removeView(mView);
            }
            return mView;
        }
        View view = setContentView(inflater, container, savedInstanceState);
        setFindViewById(view);
        setListener();
        setControl();
        mView = view;
        return view;
    }


    /**设置布局xml*/
    protected abstract View setContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**获取view组件*/
    protected abstract void setFindViewById(View view);

    /**设置监听*/
    protected abstract void setListener();

    /**主代码逻辑*/
    protected abstract void setControl();
}
