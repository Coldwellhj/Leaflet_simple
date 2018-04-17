package leaflet.miaoa.qmxh.leaflet_simple.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import leaflet.miaoa.qmxh.leaflet_simple.MyApplicition.MyApplication;
import leaflet.miaoa.qmxh.leaflet_simple.utils.NetWorkUtils;

/**
 * Created by gaofeng on 2018/3/23.
 */

public class NetWorkStatusReceiver extends BroadcastReceiver {

    public NetWorkStatusReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
//            Toast.makeText(context, "network changed", Toast.LENGTH_LONG).show();
            MyApplication.isNetWorkConnected = NetWorkUtils.getAPNType(context)>0;
        }
    }
}