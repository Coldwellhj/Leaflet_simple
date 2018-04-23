package leaflet.miaoa.qmxh.leaflet_simple.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

import leaflet.miaoa.qmxh.leaflet_simple.Login.LoginActivity;
import leaflet.miaoa.qmxh.leaflet_simple.R;
import leaflet.miaoa.qmxh.leaflet_simple.utils.StatusBarCompat;


/**
 * Created by Administrator on 2017/9/11.
 */

public abstract class BaseFragmentActivity  extends FragmentActivity {

    private Context context;
    //定义一个内部类对象
    public  static ActivityController activityController_bf;
    private ForceOfflineReceiver receiver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        //清空Fragment
        if (savedInstanceState != null){
            savedInstanceState.putParcelable("android.support:fragments",null);
        }
        super.onCreate(savedInstanceState);

        context = BaseFragmentActivity.this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //竖屏
        setContentView();
        setFindViewById();
        setListener();
        setControl();
        initWindow();
        //创建出管理所有Activity类的对象
        activityController_bf = new ActivityController();
        //将所有的Activity添加进来
        activityController_bf.addActivity(this);
    }

    @TargetApi(19)
    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintColor(context.getResources().getColor(R.color.title));
            tintManager.setStatusBarTintEnabled(true);
        }
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
    /**设置布局xml*/
    protected abstract void setContentView();

    /**获取view组件*/
    protected abstract void setFindViewById();

    /**设置监听*/
    protected abstract void setListener();

    /**主代码逻辑*/
    protected abstract void setControl();

    /**
     * 管理所有Activity的类
     */
    public static class ActivityController {

        public    List<Activity> activities = new ArrayList<>();

        /**
         * 添加Activity
         *
         * @param activity
         */
        public void addActivity(Activity activity) {
            activities.add(activity);
        }

        /**
         * 移除Activity
         *
         * @param activity
         */
        public void removeActivity(Activity activity) {
            activities.remove(activity);
        }

        /**
         * 结束所有Activity
         */
        public  void finishAll() {
            for (Activity activity : activities) {
                if (!activity.isFinishing()) {
                    activity.finish();
                }
            }
        }

    }

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
