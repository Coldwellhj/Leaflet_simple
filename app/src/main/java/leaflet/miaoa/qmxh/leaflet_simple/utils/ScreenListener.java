package leaflet.miaoa.qmxh.leaflet_simple.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * Created by gaofeng on 2018/4/25.
 */
public class ScreenListener {
    private Context mContext;
    private ScreenBroadcastReceiver receiver;
    private ScreenStateListener mScreenStateListener;

    public ScreenListener(Context context) {
        mContext = context;
        receiver = new ScreenBroadcastReceiver();
    }

    public void register(ScreenStateListener screenStateListener) {
        if (screenStateListener != null) {
            mScreenStateListener = screenStateListener;
        }
        if (receiver != null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_USER_PRESENT);
            mContext.registerReceiver(receiver, filter);
        }
    }

    public void unregister() {
        if (receiver != null) {
            mContext.unregisterReceiver(receiver);
        }
    }


    private class ScreenBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                if (Intent.ACTION_SCREEN_ON.equals(action)) {
                    if (mScreenStateListener != null) {
                        Log.e("zhang", "ScreenBroadcastReceiver --> ACTION_SCREEN_ON");
                        mScreenStateListener.onScreenOn();
                    }
                } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                    if (mScreenStateListener != null) {
                        Log.e("zhang", "ScreenBroadcastReceiver --> ACTION_SCREEN_OFF");
                        mScreenStateListener.onScreenOff();
                    }
                } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                    if (mScreenStateListener != null) {
                        Log.e("zhang", "ScreenBroadcastReceiver --> ACTION_USER_PRESENT");
                        mScreenStateListener.onUserPresent();
                    }
                }
            }
        }
    }

    public interface ScreenStateListener {
        void onScreenOn();

        void onScreenOff();

        void onUserPresent();
    }
}