package leaflet.miaoa.qmxh.leaflet_simple.base;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import leaflet.miaoa.qmxh.leaflet_simple.Login.LoginActivity;
import leaflet.miaoa.qmxh.leaflet_simple.utils.StatusBarCompat;

import static leaflet.miaoa.qmxh.leaflet_simple.base.BaseFragmentActivity.activityController_bf;


/**
 * Created by gaofeng on 2018/3/27.
 */

public class BaseOtherActivity extends AppCompatActivity {
    //定义一个内部类对象

    private ForceOfflineReceiver receiver;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StatusBarCompat.compat(this, Color.parseColor("#999999"));
            //将所有的Activity添加进来
        activityController_bf.addActivity(this);
        }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("forceOffline");
        receiver = new ForceOfflineReceiver();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        //在这个生命周期中销毁所有的Activity
        activityController_bf.removeActivity(this);
        super.onDestroy();
    }
//    /**
//     * 管理所有Activity的类
//     */
//    private class ActivityController {
//
//        private List<Activity> activities = new ArrayList<>();
//
//        /**
//         * 添加Activity
//         *
//         * @param activity
//         */
//        private void addActivity(Activity activity) {
//            activities.add(activity);
//        }
//
//        /**
//         * 移除Activity
//         *
//         * @param activity
//         */
//        private void removeActivity(Activity activity) {
//            activities.remove(activity);
//        }
//
//        /**
//         * 结束所有Activity
//         */
//        private void finishAll() {
//            for (Activity activity : activities) {
//                if (!activity.isFinishing()) {
//                    activity.finish();
//                }
//            }
//        }
//
//    }

    /**
     * 广播接收器
     */
    private class ForceOfflineReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("下线通知");
            builder.setMessage("您的账号在另一地点登录，您被迫下线了。如果这不是您本人的操作，那么您的密码可能已经泄露，建议您修改密码。");
            builder.setPositiveButton("重新登录", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                    //调用结束所有Activity的方法
                    activityController_bf.finishAll();

                }
            });
            builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    activityController_bf.finishAll();

                }
            });
            builder.setCancelable(false);
            builder.show();
        }
    }
}
