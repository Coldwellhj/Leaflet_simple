package leaflet.miaoa.qmxh.leaflet_simple.ui.personaluser.mall;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import leaflet.miaoa.qmxh.leaflet_simple.R;


/**
 * 淘宝特惠 页面
 */
public class PersonalMallTaobaoFragment extends Fragment {
    View view;
    Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal_home_receiver2, null, false);

//        main_bottom=getActivity().findViewById(R.id.main_bottom);



        return view;
    }



}
